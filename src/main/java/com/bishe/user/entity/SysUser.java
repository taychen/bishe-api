package com.bishe.user.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * User Entity
 *
 * @author chentay
 * @date 2019/05/23
 */
@Data
@Document(collection = "sys_user")
public class SysUser {

    @Id
    private String userId;

    private String username;

    private String password;

    private String createAt = String.valueOf(Instant.now().toEpochMilli() / 1000);

    private String updateAt = createAt;
}
