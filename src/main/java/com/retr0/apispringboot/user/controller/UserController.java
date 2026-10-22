package com.retr0.apispringboot.user.controller;

import com.retr0.apispringboot.common.ApiResponse;
import com.retr0.apispringboot.common.PageResult;
import com.retr0.apispringboot.user.dto.UserCreateRequest;
import com.retr0.apispringboot.user.dto.UserQuery;
import com.retr0.apispringboot.user.dto.UserResponse;
import com.retr0.apispringboot.user.dto.UserUpdateRequest;
import com.retr0.apispringboot.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口（REST 风格）。
 *
 * <p>Controller 只做三件事：接收参数、调 Service、包装响应。
 * 业务规则、异常、事务都在 Service 层。
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** 分页 + 条件查询。GET /users?page=1&size=10&username=alice */
    @GetMapping
    public ApiResponse<PageResult<UserResponse>> list(@Valid UserQuery query) {
        return ApiResponse.success(userService.page(query));
    }

    /** 详情。GET /users/1 */
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> detail(@PathVariable Long id) {
        return ApiResponse.success(userService.getById(id));
    }

    /** 新增。POST /users，成功返回 201。 */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(userService.create(request)));
    }

    /** 修改。PUT /users/1，只更新传入的字段。 */
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(@PathVariable Long id,
                                            @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.success(userService.update(id, request));
    }

    /** 删除。DELETE /users/1，成功返回 204（无响应体）。 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
