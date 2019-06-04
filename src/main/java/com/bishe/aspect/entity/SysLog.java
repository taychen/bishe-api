package com.bishe.aspect.entity;

import com.bishe.common.util.DateTimeUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志
 *
 * @author chentay
 * @date 2019/06/04
 */
@Entity
@Table(name = "sys_log")
@Data
public class SysLog implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** 用户ID */
  private String userId;

  /** 操作 */
  private String operation;

  /** 方法名 */
  private String method;

  /** 参数 */
  private String params;

  /** 返回结果 */
  private String response;

  /** ip地址 */
  private String ip;

  /** 创建时间 */
  @Column(updatable = false, nullable = false)
  private String createAt;

  @PrePersist
  public void prePersist() {
    createAt  = DateTimeUtils.getInstance().format(LocalDateTime.now());
  }

}
