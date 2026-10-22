package com.retr0.apispringboot.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 用户模块的接口测试 —— 这就是你实现 UserService 的"验收标准"。
 *
 * <p>现在这些测试是<b>红色</b>的（Service 还是 TODO），每实现一个方法就会变绿。
 * 全部变绿 = 阶段 3 完成。
 *
 * <p>测试方法带 @Transactional，结束后自动回滚，不会往数据库里留脏数据。
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /* ---------------- TODO 3：新增 ---------------- */

    @Test
    @DisplayName("新增用户返回 201，且响应中不含密码")
    void createReturns201() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"tom@example.com","password":"secret123","nickName":"汤姆","sex":1}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.username").value("tom@example.com"))
                .andExpect(jsonPath("$.data.nickName").value("汤姆"))
                .andExpect(jsonPath("$.data.password").doesNotExist())
                .andExpect(jsonPath("$.data.registerDate").exists());
    }

    @Test
    @DisplayName("参数不合法返回 400 + 字段明细")
    void createRejectsInvalidBody() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"","password":"1"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAM"))
                .andExpect(jsonPath("$.data.username").exists())
                .andExpect(jsonPath("$.data.password").exists());
    }

    @Test
    @DisplayName("用户名重复返回 409")
    void createRejectsDuplicateUsername() throws Exception {
        createUser("dup@example.com", "重复");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"dup@example.com","password":"secret123"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT"));
    }

    /* ---------------- TODO 1：详情 ---------------- */

    @Test
    @DisplayName("查询详情返回 200")
    void detailReturnsUser() throws Exception {
        long id = createUser("detail@example.com", "详情");

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.username").value("detail@example.com"))
                .andExpect(jsonPath("$.data.nickName").value("详情"));
    }

    @Test
    @DisplayName("查询不存在的用户返回 404")
    void detailReturns404() throws Exception {
        mockMvc.perform(get("/users/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    /* ---------------- TODO 2：分页 ---------------- */

    @Test
    @DisplayName("分页查询返回统一分页结构")
    void listReturnsPage() throws Exception {
        createUser("p1@example.com", "分页1");
        createUser("p2@example.com", "分页2");
        createUser("p3@example.com", "分页3");

        mockMvc.perform(get("/users").param("page", "1").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(2))
                .andExpect(jsonPath("$.data.total").value(3))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.size").value(2))
                .andExpect(jsonPath("$.data.totalPages").value(2));
    }

    @Test
    @DisplayName("按用户名模糊查询")
    void listFiltersByUsername() throws Exception {
        createUser("alice@example.com", "爱丽丝");
        createUser("bob@example.com", "鲍勃");

        mockMvc.perform(get("/users").param("username", "alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.items[0].username").value("alice@example.com"));
    }

    @Test
    @DisplayName("分页参数越界返回 400")
    void listRejectsInvalidPaging() throws Exception {
        mockMvc.perform(get("/users").param("size", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAM"))
                .andExpect(jsonPath("$.data.size").exists());
    }

    /* ---------------- TODO 4：修改 ---------------- */

    @Test
    @DisplayName("修改只更新传入的字段")
    void updateChangesGivenFields() throws Exception {
        long id = createUser("update@example.com", "旧昵称");

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nickName":"新昵称"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickName").value("新昵称"))
                .andExpect(jsonPath("$.data.username").value("update@example.com"));
    }

    @Test
    @DisplayName("修改不存在的用户返回 404")
    void updateReturns404() throws Exception {
        mockMvc.perform(put("/users/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nickName":"x"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    /* ---------------- TODO 5：删除 ---------------- */

    @Test
    @DisplayName("删除返回 204，再查询返回 404")
    void deleteThenGone() throws Exception {
        long id = createUser("delete@example.com", "待删除");

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("删除不存在的用户返回 404")
    void deleteReturns404() throws Exception {
        mockMvc.perform(delete("/users/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    /* ---------------- 测试辅助方法 ---------------- */

    /**
     * 通过接口创建一个用户，返回自增 id（从响应 JSON 里取）。
     *
     * <p>用接口创建而不是直接写 SQL，是为了让测试走完整链路（Controller → Service → Mapper）。
     */
    private long createUser(String username, String nickName) throws Exception {
        String body = """
                {"username":"%s","password":"secret123","nickName":"%s","sex":1}
                """.formatted(username, nickName);

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 简单取 "id":数字 —— 生产代码不要这么解析 JSON，测试图省事
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("\"id\"\\s*:\\s*(\\d+)").matcher(response);
        if (!matcher.find()) {
            throw new IllegalStateException("响应里没有 id：" + response);
        }
        return Long.parseLong(matcher.group(1));
    }
}
