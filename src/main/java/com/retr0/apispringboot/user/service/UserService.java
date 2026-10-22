package com.retr0.apispringboot.user.service;

import com.retr0.apispringboot.common.BusinessException;
import com.retr0.apispringboot.common.PageResult;
import com.retr0.apispringboot.user.dto.UserCreateRequest;
import com.retr0.apispringboot.user.dto.UserQuery;
import com.retr0.apispringboot.user.dto.UserResponse;
import com.retr0.apispringboot.user.dto.UserUpdateRequest;
import com.retr0.apispringboot.user.entity.User;
import com.retr0.apispringboot.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户业务层。
 *
 * <p>这一层负责三件事：
 * <ol>
 *   <li>业务规则：存在性校验、用户名唯一性；</li>
 *   <li>数据转换：Entity ⇄ DTO；</li>
 *   <li>事务边界：写操作加 @Transactional。</li>
 * </ol>
 *
 * <p><b>下面 5 个方法留给你实现</b>（TODO 1~5）。每个 TODO 都写了步骤提示，
 * 对应的接口测试已经写好，实现一个方法就会有一部分测试变绿。
 */
@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // =====================================================================
    // TODO 1：查询单个用户
    // =====================================================================
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        // 步骤：
        //   1) 调用 userMapper.findById(id)
        //   2) 结果为 null → 抛 BusinessException.notFound("用户不存在：" + id)
        //   3) 不为 null → 用 UserResponse.from(user) 转成 DTO 返回
        //
        // 提示：可以用下面的 requireUser(id) 少写两行
        throw new UnsupportedOperationException("TODO 1：实现 getById");
    }

    // =====================================================================
    // TODO 2：分页 + 条件查询
    // =====================================================================
    @Transactional(readOnly = true)
    public PageResult<UserResponse> page(UserQuery query) {
        // 步骤：
        //   1) long total = userMapper.count(query);
        //   2) total == 0 时 items 直接给空列表（不要再去查一次）
        //      否则 items = userMapper.findPage(query, query.offset(), query.getSize())
        //   3) 把 List<User> 转成 List<UserResponse>（提示：users.stream().map(UserResponse::from).toList()）
        //   4) return PageResult.of(items, total, query.getPage(), query.getSize())
        throw new UnsupportedOperationException("TODO 2：实现 page");
    }

    // =====================================================================
    // TODO 3：新增用户
    // =====================================================================
    @Transactional
    public UserResponse create(UserCreateRequest request) {
        // 步骤：
        //   1) 用户名唯一性校验：userMapper.countByUsername(request.username()) > 0
        //      → 抛 BusinessException.conflict("用户名已存在：" + request.username())
        //   2) new 一个 User，把 request 的字段塞进去
        //   3) registerDate 用 LocalDateTime.now()（服务端生成，不信任客户端）
        //   4) userMapper.insert(user)（插入后自增 id 会回填到 user.getId()）
        //   5) 返回时再查一次：UserResponse.from(userMapper.findById(user.getId()))
        //
        // 注意：密码这一阶段先按原样存；阶段 5 接入认证后会改成 BCrypt 哈希。
        throw new UnsupportedOperationException("TODO 3：实现 create");
    }

    // =====================================================================
    // TODO 4：修改用户（只更新传入的字段）
    // =====================================================================
    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        // 步骤：
        //   1) 先确认用户存在（不存在 → 404），可以用 requireUser(id)
        //   2) new 一个 User 作为 patch 对象：只 setId(id) 和"有值"的字段
        //      （nickName != null 才 setNickName；StringUtils.hasText(password) 才 setPassword）
        //   3) 如果三个字段都没传 → 抛 BusinessException.invalidParam("没有需要更新的字段")
        //   4) userMapper.update(patch)
        //   5) 返回最新数据：UserResponse.from(requireUser(id))
        throw new UnsupportedOperationException("TODO 4：实现 update");
    }

    // =====================================================================
    // TODO 5：删除用户
    // =====================================================================
    @Transactional
    public void delete(Long id) {
        // 步骤：
        //   1) 先确认用户存在（不存在 → 404）
        //   2) userMapper.deleteById(id)
        throw new UnsupportedOperationException("TODO 5：实现 delete");
    }

    // =====================================================================
    // 已实现的辅助方法：按 id 查用户，查不到抛 404
    // =====================================================================
    private User requireUser(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw BusinessException.notFound("用户不存在：" + id);
        }
        return user;
    }
}
