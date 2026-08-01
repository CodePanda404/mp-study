package com.example.mpstudy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("biz_declaration_item")
public class DeclarationItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long declarationId;
    private String hsCode;
    private String productName;
    private Integer qty;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
