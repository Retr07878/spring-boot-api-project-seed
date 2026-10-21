package com.retr0.apispringboot.health;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;   // 模拟 HTTP 请求的工具，不用真启动服务器

    @Test
    void healthReturnsUnifiedResponse() throws Exception {
        mockMvc.perform(get("/health"))            // 发一个 GET /health
                .andExpect(status().isOk())         // 断言 HTTP 状态码 200
                .andExpect(jsonPath("$.code").value("OK"))          // 断言 body.code = "OK"
                .andExpect(jsonPath("$.message").value("success"))  // 断言 body.message
                .andExpect(jsonPath("$.data.status").value("UP"));  // 断言 body.data.status
    }
}