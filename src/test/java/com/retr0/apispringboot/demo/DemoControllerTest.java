package com.retr0.apispringboot.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void businessExceptionBecomes404() throws Exception {
        mockMvc.perform(get("/demo/not-found"))
                .andExpect(status().isNotFound())                       // ← HTTP 状态码 404
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))       // ← 业务码
                .andExpect(jsonPath("$.message").value("用户不存在：1")); // ← 具体提示
    }

    @Test
    void unexpectedExceptionBecomes500() throws Exception {
        mockMvc.perform(get("/demo/boom"))
                .andExpect(status().isInternalServerError())            // ← HTTP 500
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"));
    }
    @Test
    void validBodyPasses() throws Exception {
        String body = """
                {"name":"张三","age":18,"email":"zhangsan@example.com"}
                """;
        mockMvc.perform(post("/demo/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"));
    }

    @Test
    void invalidBodyReturns400WithFieldErrors() throws Exception {
        // name 为空、age 超范围
        String body = """
                {"name":"  ","age":200}
                """;
        mockMvc.perform(post("/demo/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAM"))
                .andExpect(jsonPath("$.data.name").value("name 不能为空"))
                .andExpect(jsonPath("$.data.age").value("age 不能大于 150"));
    }

    @Test
    void malformedJsonReturns400() throws Exception {
        mockMvc.perform(post("/demo/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{name: 张三"))          // 不是合法 JSON
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAM"));
    }

    @Test
    void invalidPageParamReturns400() throws Exception {
        mockMvc.perform(get("/demo/page").param("size", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.size").value("size 必须大于等于 1"));
    }

    @Test
    void typeMismatchReturns400() throws Exception {
        mockMvc.perform(get("/demo/page").param("page", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAM"));
    }

    @Test
    void defaultPageValuesWork() throws Exception {
        mockMvc.perform(get("/demo/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("page=1, size=10"));
    }
}