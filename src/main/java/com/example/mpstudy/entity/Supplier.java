package com.example.mpstudy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private LocalDateTime createdAt;

}
