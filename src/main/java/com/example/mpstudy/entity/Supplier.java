package com.example.mpstudy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_supplier")
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String supplierCode;
    private String supplierName;
    private String contactPerson;
    private String phone;
    // 0-禁用 1-启用
    private Integer status;
    /** 自动填充创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 自动填充更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除注解
     * 执行 deleteById 时，底层会自动变为 UPDATE sys_supplier SET deleted = 1 WHERE id = ?
     * 执行 select 时，底层会自动带上 WHERE deleted = 0
     */
    @TableLogic
    private Integer deleted;

}
