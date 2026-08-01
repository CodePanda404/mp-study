package com.example.mpstudy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("biz_customs_declaration")
public class CustomsDeclaration implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String declarationNo;
    private Long supplierId;
    private Long warehouseId;
    private BigDecimal totalAmount;
    private String currency;
    private String status;
    private LocalDateTime createdAt;
}
