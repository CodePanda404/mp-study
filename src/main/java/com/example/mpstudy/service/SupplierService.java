package com.example.mpstudy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mpstudy.entity.Supplier;

/**
 * IService 是 MP 提供的超强 Service 基类，内置了如 saveBatch（批量插入）、page（分页）、getOne 等超级方便的方法！
 */
public interface SupplierService extends IService<Supplier> {
    /**
     * 分页多条件查询供应商
     */
    Page<Supplier> getSupplierPage(int pageNum, int pageSize, String supplierName, Integer status);
}
