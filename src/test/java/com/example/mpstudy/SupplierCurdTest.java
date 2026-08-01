package com.example.mpstudy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.mpstudy.entity.CustomsDeclaration;
import com.example.mpstudy.entity.Supplier;
import com.example.mpstudy.entity.Warehouse;
import com.example.mpstudy.mapper.CustomsDeclarationMapper;
import com.example.mpstudy.mapper.SupplierMapper;
import com.example.mpstudy.mapper.WarehouseMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SupplierCurdTest {

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private CustomsDeclarationMapper customsDeclarationMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Test
    @DisplayName("1. 测试新增：自动使用雪花算法生成分布式 ID")
    void testInsert() {
        Supplier supplier = new Supplier();
        supplier.setSupplierCode("SUP_003");
        supplier.setSupplierName("德国博世集团供应链");
        supplier.setContactPerson("汉斯经理");
        supplier.setPhone("021-88889999");
        supplier.setStatus(1);

        // 执行自增
        int rows = supplierMapper.insert(supplier);
        assertEquals(1, rows);
        System.out.println("新增成功，MP 自动生成的雪花 ID 为: " + supplier.getId());
    }

    @Test
    @DisplayName("2.1 高频查询：多条件组合 + 动态条件判断")
    void testQueryWrapper() {
        // 模拟前端传递的参数
        String supplierName = "半导体";
        Integer status = 1;

        // 构建查询条件对象 LambdaQueryWrapper
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        // 1.等值查询（eq） => 条件是 status != null 时才会触发
        queryWrapper.eq(status != null, Supplier::getStatus, status)
                // 2.模糊查询（like） => 条件是 StringUtils.isNotBlank(supplierName) = true 才会触发
                .like(StringUtils.isNotBlank(supplierName), Supplier::getSupplierName, supplierName)
                // 3.按创建时间倒序
                .orderByDesc(Supplier::getCreatedAt);

        // 4.执行查询，传入 queryWrapper 对象
        List<Supplier> suppliers = supplierMapper.selectList(queryWrapper);
        suppliers.forEach(System.out::println);
    }

    @Test
    @DisplayName("2.2 常见查询：数值范围与集合 IN 查询")
    void testQueryByRangeAndIn() {
        // 查询总金额在 100,000 ~ 500,000 之间的USD报关单，且报关单状态为（SUBMITTED，CUSTOMS_PASS）
        BigDecimal minAmount = BigDecimal.valueOf(100000L);
        BigDecimal maxAmount = BigDecimal.valueOf(500000L);
        ArrayList<String> statusList = Lists.newArrayList("SUBMITTED", "CUSTOMS_PASS");

        // 1.构建查询条件对象 LambdaQueryWrapper
        LambdaQueryWrapper<CustomsDeclaration> queryWrapper = new LambdaQueryWrapper<>();
        // 范围查询 between
        queryWrapper.between(CustomsDeclaration::getTotalAmount, minAmount, maxAmount)
                 // 等值查询 =
                .eq(CustomsDeclaration::getCurrency, "USD")
                 // IN 查询
                .in(CustomsDeclaration::getStatus, statusList);
        // 2.执行范围查询和IN查询
        List<CustomsDeclaration> customsDeclarations = customsDeclarationMapper.selectList(queryWrapper);
        customsDeclarations.forEach(System.out::println);
    }

    @Test
    @DisplayName("2.3 投影查询：只查询指定字段")
    void testSelectSpecificFields() {
        // 查询状态为启动(1)的供应商id,供应商名称,供应商编号(id,supplier_name, supplier_code)
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Supplier::getId, Supplier::getSupplierName, Supplier::getSupplierCode)
                .eq(Supplier::getStatus, 1);
        // 执行查询
        List<Supplier> suppliers = supplierMapper.selectList(queryWrapper);
        suppliers.forEach(System.out::println);
    }

    @Test
    @DisplayName("3.1 按 id 更新单条数据（非空更新）")
    void testUpdateById() {
        // 修改 id = 1001 的供应商联系人为 熊大经理
        // 1.构建更新对象
        Supplier supplier = new Supplier();
        supplier.setId(1001L);
        supplier.setContactPerson("熊大经理");

        // 2.执行更新
        int rows = supplierMapper.updateById(supplier);
        assertEquals(1, rows);
    }

    @Test
    @DisplayName("3.2 条件直接更新：将某个供应商下所有草稿状态(DRAFT)的报关单修改为 SUBMITTED")
    void testUpdateByWrapper() {
        // 将供应商id=1001的所有报关单（状态为DRAFT）修改为 SUBMITTED 状态
        // 1.构造更新条件对象
        LambdaUpdateWrapper<CustomsDeclaration> updateWrapper = new LambdaUpdateWrapper<>();
        // // SET status = 'SUBMITTED' WHERE supplier_id = 1001 AND status = 'DRAFT'
        updateWrapper.set(CustomsDeclaration::getStatus, "SUBMITTED")
                .eq(CustomsDeclaration::getSupplierId, 1001L)
                .eq(CustomsDeclaration::getStatus, "DRAFT");

        // 2.执行批量更新
        int rows = customsDeclarationMapper.update(updateWrapper);
        System.out.println("条件批量更新受影响行数: " + rows);
    }

    @Test
    @DisplayName("4.1 条件删除与批量 ID 集合删除")
    void testDelete() {
        // 1.根据id批量删除
        supplierMapper.deleteBatchIds(Lists.newArrayList(111L, 112L));

        // 2.根据 Lambda 条件删除
        // 删除状态为禁用且编号以TEST开头的的供应商
        LambdaQueryWrapper<Supplier> deleteWrapper = new LambdaQueryWrapper<>();
        // delete from supplier where status = 0 and supplier_code like "TEST_%"
        deleteWrapper.eq(Supplier::getStatus, 0)
                .likeRight(Supplier::getSupplierCode, "TEST_");

        // 3.执行删除
        int rows = supplierMapper.delete(deleteWrapper);
        System.out.println("条件删除影响行数: " + rows);
    }

    @Test
    @DisplayName("保税仓库表的条件查询")
    void testWarehouseSelect() {
        // 模拟前端传递数据
        Integer capacity = 40000;
        String location = "上海";

        // 1.构建条件查询对象
        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.gt(Warehouse::getCapacity, capacity)
                .like(Warehouse::getLocation, location);

        // 2.执行条件查询
        List<Warehouse> warehouses = warehouseMapper.selectList(queryWrapper);
        warehouses.forEach(System.out::println);
    }
}
