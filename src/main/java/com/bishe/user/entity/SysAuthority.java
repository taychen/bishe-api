package com.bishe.user.entity;

import com.bishe.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Authority Entity
 *
 * @author chentay
 * @date 2019/05/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_authority")
public class SysAuthority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * 权限名称，中文
     */
    private String name;

    /**
     * 权限属性，英文
     */
    private String value;

    /** 描述 */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGTEXT")
    private String description;

}
