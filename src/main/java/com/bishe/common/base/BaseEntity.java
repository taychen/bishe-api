package com.bishe.common.base;

import com.bishe.common.util.DateTimeUtils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 时间字段基类
 *
 * @author chentay
 * @date 2019/01/16
 */
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

  /** 创建时间 */
  @Column(updatable = false, nullable = false)
  private String createAt;

  /** 更新时间 */
  @Column(nullable = false)
  private String updateAt;

  @PrePersist
  public void prePersist() {
    createAt = updateAt = DateTimeUtils.getInstance().format(LocalDateTime.now());
  }

  @PreUpdate
  public void preUpdate() {
    updateAt = DateTimeUtils.getInstance().format(LocalDateTime.now());
  }
}
