package com.example.mpstudy.vo;


import com.example.mpstudy.entity.DeclarationItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报关单详情VO
 */
@Data
public class DeclarationDetailVO {
    /**
     * 报关单id
     */
    private Long id;
    /**
     * 报关单号
     */
    private String declarationNo;
    /**
     * 状态: DRAFT-草稿, SUBMITTED-已提交, CUSTOMS_PASS-海关放行, REJECTED-已退单
     */
    private String status;
    /**
     * 币种以及金额
     */
    private String currency;
    private BigDecimal totalAmount;
    private LocalDateTime createAt;

    /**
     * 供应商信息
     */
    private String supplierCode;
    private String supplierName;
    private String contactPerson;

    /**
     * 保税仓库信息
     */
    private String warehouseCode;
    private String warehouseName;
    private String location;

    /**
     * 商品明细表 一对多
     */
    private List<DeclarationItem> items;
}
