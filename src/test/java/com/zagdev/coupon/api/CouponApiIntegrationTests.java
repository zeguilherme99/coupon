package com.zagdev.coupon.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CouponApiIntegrationTests {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void post_get_delete_and_deleteAgain() throws Exception {
        var req = new java.util.HashMap<String, Object>();
        req.put("code", "ABC-123");
        req.put("description", "string");
        req.put("expirationDate", Instant.now().plusSeconds(3600).toString());
        req.put("discountValue", 0.8);

        var create = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.published").value(false))
                .andExpect(jsonPath("$.redeemed").value(false))
                .andReturn();

        String id = objectMapper.readTree(create.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("coupon already deleted"));

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELETED"));
    }

    @Test
    void invalidPost_returns400_withProblemDetails() throws Exception {
        var req = new java.util.HashMap<String, Object>();
        req.put("code", "A-1");
        req.put("description", "");
        req.put("expirationDate", Instant.now().minusSeconds(3600).toString());
        req.put("discountValue", 0.1);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
