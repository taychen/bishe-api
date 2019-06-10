package com.bishe.user.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.bishe.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User Entity
 *
 * @author chentay
 * @date 2019/05/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** 用户编号 */
  private String userId;

  /** 用户姓名 */
  private String username;

  /** 用户密码 */
  @JSONField(serialize = false)
  @JsonIgnore
  private String password;

  /** 用户手机号 */
  private String mobileCode;

  /** 学院编号 */
  private String collegeId;

  /** 学院名称 */
  private String collegeName;

  /** 专业 */
  private String major;

  /**
   * 角色
   */
  @ManyToMany(targetEntity = SysRole.class, fetch = FetchType.EAGER)
  @BatchSize(size = 20)
  private Set<SysRole> roles = new HashSet<>();

}
