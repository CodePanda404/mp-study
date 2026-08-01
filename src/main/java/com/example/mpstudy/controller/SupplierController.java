package com.example.mpstudy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpstudy.common.Result;
import com.example.mpstudy.entity.Supplier;
import com.example.mpstudy.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 分页查询供应商列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param supplierName 供应商名称
     * @param status 供应商状态
     * @return 供应商分页数据
     */
    @GetMapping("/page")
    public Result<Page<Supplier>> getPage(@RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) String supplierName,
                                          @RequestParam(required = false) Integer status) {

        Page<Supplier> supplierPage = supplierService.getSupplierPage(pageNum, pageSize, supplierName, status);
        return Result.success(supplierPage);
    }

    /**
     * 新增供应商
     * @param supplier 供应商数据
     * @return boolean
     */
    @PostMapping("/create")
    public Result<Boolean> createSupplier(@RequestBody Supplier supplier) {
        // 使用 IService 自带的 save 方法
        // @TableFiled 会自动填充 createdAt/updatedAt
        boolean save = supplierService.save(supplier);
        return Result.success(save);
    }

    @GetMapping ("/delete")
    public Result<Boolean> removeSupplier(@RequestParam Long id) {
        // 测试 @TableLogic：
        // 观察 SQL 是否变成了 UPDATE sys_supplier SET deleted=1 WHERE id=? AND deleted=0
        boolean success = supplierService.removeById(id);
        return Result.success(success);
    }
}
