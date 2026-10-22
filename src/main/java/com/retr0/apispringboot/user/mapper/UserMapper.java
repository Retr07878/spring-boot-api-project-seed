package com.retr0.apispringboot.user.mapper;

import com.retr0.apispringboot.user.dto.UserQuery;
import com.retr0.apispringboot.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户持久层。
 *
 * <p>只负责和数据库打交道，SQL 全部手写在
 * {@code src/main/resources/mapper/UserMapper.xml} 里，不在 Java 里拼 SQL。
 */
@Mapper
public interface UserMapper {

    /** 按主键查用户，查不到返回 null。 */
    User findById(@Param("id") Long id);

    /**
     * 分页查询。
     *
     * @param query  查询条件（username / nickName / sex）
     * @param offset 跳过多少条 = (page - 1) * size
     * @param limit  每页多少条
     */
    List<User> findPage(@Param("q") UserQuery query,
                        @Param("offset") long offset,
                        @Param("limit") int limit);

    /** 统计符合条件的总数，用于计算总页数。 */
    long count(@Param("q") UserQuery query);

    /** 用户名是否已存在（返回条数，> 0 表示存在）。 */
    int countByUsername(@Param("username") String username);

    /** 新增，插入成功后自增 id 会回填到 user.id。 */
    int insert(User user);

    /** 按 id 动态更新（只更新非空字段），返回影响行数。 */
    int update(User user);

    /** 按 id 删除，返回影响行数。 */
    int deleteById(@Param("id") Long id);
}
