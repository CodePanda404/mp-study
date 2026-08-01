package com.example.mpstudy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpstudy.entity.Supplier;
import com.example.mpstudy.mapper.SupplierMapper;
import com.example.mpstudy.service.SupplierService;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    /**
     * 分页查询
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param supplierName 供应商名称
     * @param status 状态
     * @return 分页数据
     */
    @Override
    public Page<Supplier> getSupplierPage(int pageNum, int pageSize, String supplierName, Integer status) {
        // 1.构建 MP 分页对象
        Page<Supplier> page = new Page<>(pageNum, pageSize);

        // 2.构建查询条件
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(supplierName), Supplier::getSupplierName, supplierName)
                .eq(status != null, Supplier::getStatus, status)
                .orderByDesc(Supplier::getCreatedAt);

        // 3.执行 MP 的底层分页查询（会自动线执行 COUNT(*) 再执行分页 SELECT LIMIT）
        return this.page(page, queryWrapper);
    }
}
