package com.bishe.user.entity;

import com.bishe.common.base.BaseEntity;
import com.bishe.common.validated.ValidatedGroups;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
  @NotBlank(
      message = "用户编号不能为空",
      groups = {ValidatedGroups.Default.class})
  @Pattern(
      regexp = "^[0-9]{6,10}$",
      message = "格式错误，用户编号为6-10位数字",
      groups = {ValidatedGroups.Default.class})
  private String userId;

  /** 用户姓名 */
  @NotBlank(
      message = "真实姓名不能为空",
      groups = {ValidatedGroups.Default.class})
  private String username;

  /** 用户密码 */
  @NotBlank(
      message = "用户密码不能为空",
      groups = {ValidatedGroups.Default.class})
  private String password;

  /** 用户手机号 */
  @NotBlank(
      message = "用户手机号不能为空",
      groups = {ValidatedGroups.Default.class})
  @Pattern(
      regexp = "^([1][3-9][0-9]{9})$",
      message = "格式错误，请输入正确的手机号",
      groups = {ValidatedGroups.Default.class})
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
