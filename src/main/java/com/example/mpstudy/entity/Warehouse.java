package com.example.mpstudy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_warehouse")
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String warehouseCode;

    private String warehouseName;

    private String location;
    /**
     * 乐观锁注解：MP 会自动在更新时自动拼接 WHERE version = ? 并自动将 version + 1
     */
    @Version
    private Integer capacity;

    private LocalDateTime createdAt;


}
