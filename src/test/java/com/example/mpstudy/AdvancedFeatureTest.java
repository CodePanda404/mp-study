package com.example.mpstudy;

import com.example.mpstudy.entity.DeclarationItem;
import com.example.mpstudy.entity.Warehouse;
import com.example.mpstudy.mapper.DeclarationItemMapper;
import com.example.mpstudy.mapper.WarehouseMapper;
import com.example.mpstudy.service.DeclarationItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AdvancedFeatureTest {
    @Autowired
    private WarehouseMapper warehouseMapper;

    @Test
    @DisplayName("1. 测试防全表更新拦截器：尝试不加 WHERE 条件更新整张表")
    void testBlockAttack() {
        Warehouse warehouse = new Warehouse();
        warehouse.setCapacity(99999);

        // 不传任何 Wrapper（即没有 WHERE 条件），防全表插件会直接抛出 MybatisPlusException
        assertThrows(RuntimeException.class, () -> {
            warehouseMapper.update(warehouse, null);
        }, "应该拦截无 WHERE 条件的全表更新！");

        System.out.println("✅ 防全表更新拦截器生效！成功阻止了一次潜在的生产环境数据毁灭事故！");
    }

    @Test
    @DisplayName("2. 模拟并发冲突：测试 @Version 乐观锁机制")
    void testOptimisticLocker() {
        // 1. 管理员 A 查出数据 (id = 2001, version = 1)
        Warehouse warehouseA = warehouseMapper.selectById(2001L);

        // 2. 管理员 B 也查出同一条数据 (id = 2001, version = 1)
        Warehouse warehouseB = warehouseMapper.selectById(2001L);

        // 3. 管理员 A 先修改容量并提交
        warehouseA.setCapacity(60000);
        int rowsA = warehouseMapper.updateById(warehouseA); // 执行：UPDATE sys_warehouse SET capacity=60000, version=2 WHERE id=2001 AND version=1
        System.out.println("管理员 A 更新结果: " + (rowsA > 0 ? "成功" : "失败"));

        // 4. 管理员 B 随后修改容量并提交（此时数据库里的 version 已经是 2 了，而 warehouseB 手里的 version 还是 1）
        warehouseB.setCapacity(70000);
        int rowsB = warehouseMapper.updateById(warehouseB); // 执行：UPDATE sys_warehouse SET capacity=70000, version=2 WHERE id=2001 AND version=1
        System.out.println("管理员 B 更新结果: " + (rowsB > 0 ? "成功" : "失败"));

        // 断言：管理员 B 更新必然失败（影响行数为 0），从而完美保护了数据不被覆盖！
        org.junit.jupiter.api.Assertions.assertEquals(0, rowsB);
    }

    @Autowired
    private DeclarationItemService declarationItemService;

    @Test
    @DisplayName("3. 测试海量数据高吞吐批量插入 (Batch Insert)")
    void testBatchInsert() {
        List<DeclarationItem> list = new ArrayList<>();
        // 构造 5000 条海量商品明细
        for (int i = 0; i < 5000; i++) {
            DeclarationItem item = new DeclarationItem();
            item.setDeclarationId(3001L);
            item.setProductName("集成电路芯片 NV-" + i);
            item.setHsCode("8542310000");
            item.setQty(100);
            item.setUnitPrice(new BigDecimal("15.50"));
            item.setTotalPrice(new BigDecimal("1550.00"));
            list.add(item);
        }

        long start = System.currentTimeMillis();
        // 执行 MP 的批量插入（默认每批次 1000 条）
        declarationItemService.saveBatch(list, 1000);
        long end = System.currentTimeMillis();

        System.out.println("🚀 5000 条数据批量插入耗时: " + (end - start) + " ms");
    }
}
