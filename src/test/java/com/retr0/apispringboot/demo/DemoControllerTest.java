package com.retr0.apispringboot.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 阶段 2 的验收测试：异常处理、参数校验、分页、追踪 ID。
 */
@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /* ---------------- 2.2 异常处理 ---------------- */

    @Test
    void businessExceptionBecomes404() throws Exception {
        mockMvc.perform(get("/demo/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("用户不存在：1"));
    }

    @Test
    void unexpectedExceptionBecomes500() throws Exception {
        mockMvc.perform(get("/demo/boom"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                // 内部异常信息不能泄露给前端
                .andExpect(jsonPath("$.message").value("服务器内部错误"));
    }

    /* ---------------- 2.3 参数校验 ---------------- */

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
                        .content("{name: 张三"))
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
    void bindingFailureGetsFriendlyMessage() throws Exception {
        mockMvc.perform(get("/demo/page").param("page", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAM"))
                // 类型转换失败不能把英文系统消息暴露出去
                .andExpect(jsonPath("$.data.page").value("参数格式不正确"));
    }

    /* ---------------- 2.4 分页 ---------------- */

    @Test
    void pageReturnsPagedStructure() throws Exception {
        mockMvc.perform(get("/demo/page").param("page", "2").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(2))
                .andExpect(jsonPath("$.data.total").value(5))
                .andExpect(jsonPath("$.data.page").value(2))
                .andExpect(jsonPath("$.data.size").value(2))
                .andExpect(jsonPath("$.data.totalPages").value(3));
    }

    @Test
    void defaultPageValuesWork() throws Exception {
        mockMvc.perform(get("/demo/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.items.length()").value(5));
    }

    @Test
    void sizeOverMaxReturns400() throws Exception {
        mockMvc.perform(get("/demo/page").param("size", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.size").value("size 不能超过 100"));
    }

    @Test
    void pageBeyondEndReturnsEmptyItems() throws Exception {
        mockMvc.perform(get("/demo/page").param("page", "99").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(0))
                .andExpect(jsonPath("$.data.total").value(5));
    }

    /* ---------------- 2.4 追踪 ID ---------------- */

    @Test
    void traceIdHeaderIsReturned() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Trace-Id"));
    }

    @Test
    void traceIdIsEchoedWhenProvided() throws Exception {
        mockMvc.perform(get("/health").header("X-Trace-Id", "trace-from-client"))
                .andExpect(header().string("X-Trace-Id", "trace-from-client"));
    }
}
