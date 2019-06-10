package com.bishe.user.entity;

import com.bishe.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Role Entity
 *
 * @author chentay
 * @date 2019/05/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_role")
public class SysRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * 角色名称，中文
     */
    private String name;

    /**
     * 角色属性，英文
     */
    private String value;

    /** 描述 */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    /**
     * 权限
     */
    @JsonIgnore
    @ManyToMany(targetEntity = SysAuthority.class, fetch = FetchType.EAGER)
    @BatchSize(size = 20)
    private Set<SysAuthority> authorities = new HashSet<>();
}
