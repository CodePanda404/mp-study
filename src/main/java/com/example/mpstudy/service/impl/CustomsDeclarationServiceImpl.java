package com.example.mpstudy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpstudy.entity.CustomsDeclaration;
import com.example.mpstudy.entity.DeclarationItem;
import com.example.mpstudy.mapper.CustomsDeclarationMapper;
import com.example.mpstudy.mapper.DeclarationItemMapper;
import com.example.mpstudy.service.CustomsDeclarationService;
import com.example.mpstudy.vo.DeclarationDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomsDeclarationServiceImpl extends ServiceImpl<CustomsDeclarationMapper, CustomsDeclaration> implements CustomsDeclarationService {

    @Autowired
    private DeclarationItemMapper declarationItemMapper;
    
    @Autowired
    private CustomsDeclarationMapper customsDeclarationMapper;

    @Override
    public Page<DeclarationDetailVO> getDeclarationDetailPageA(int pageNum, int pageSize, String declarationNo, String status) {
        Page<DeclarationDetailVO> page = new Page<>(pageNum, pageSize);
        // 1.使用 QueryWrapper 构造主表查询条件（QueryWrapper 多表联查更灵活）
        QueryWrapper<CustomsDeclaration> wrapper = new QueryWrapper<>();
        // 显式指定表别名 d.
        wrapper.eq(StringUtils.isNotBlank(status), "d.status", status)
                .like(StringUtils.isNotBlank(declarationNo), "d.declaration_no", declarationNo)
                .orderByDesc("d.created_at");

        // 2. 调用自定义 Mapper 执行多表 JOIN 分页查询
        // 构造分页对象
        Page<DeclarationDetailVO> resultPage = (Page<DeclarationDetailVO>) baseMapper.selectDeclarationPageA(page, wrapper);

        // 3.回填一对多查询商品详情列表
        for (DeclarationDetailVO record : resultPage.getRecords()) {
            List<DeclarationItem> declarationItems = declarationItemMapper.selectList(new LambdaQueryWrapper<DeclarationItem>()
                    .eq(DeclarationItem::getDeclarationId, record.getId()));
            record.setItems(declarationItems);
        }
        return resultPage;
    }

    @Override
    public Page<DeclarationDetailVO> getDeclarationDetailPageB(int pageNum, int pageSize, String declarationNo, String status) {
        // 1.构建 MP 分页对象
        Page<DeclarationDetailVO> page = new Page<>(pageNum, pageSize);
        // 2.执行分页查询 => 传入 page 对象
        Page<DeclarationDetailVO> resultPage = (Page<DeclarationDetailVO>) customsDeclarationMapper.selectDeclarationPageB(page, declarationNo, status);
        List<DeclarationDetailVO> records = resultPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return resultPage;
        }

        // 3.回填一对多查询商品详情列表
        List<Long> declarationIds = records.stream()
                .map(DeclarationDetailVO::getId)
                .toList();
        // 3.1 根据 declarationIds 查询商品详情列表
        List<DeclarationItem> itemsList = declarationItemMapper.selectList(
                new LambdaQueryWrapper<DeclarationItem>()
                    .in(DeclarationItem::getDeclarationId, declarationIds));

        // 3.2 使用 stream 操作按 declarationId 进行分组
        Map<Long, List<DeclarationItem>> itemsMap = itemsList.stream()
                .collect(Collectors.groupingBy(DeclarationItem::getDeclarationId));

        // 3.3 回填数据
        resultPage.getRecords().forEach(record ->
                record.setItems(itemsMap.getOrDefault(record.getId(), Collections.emptyList()))
        );
        return resultPage;
    }

    @Override
    public boolean updateStatusChain(Long id, String targetStatus) {
        // UPDATE biz_customs_declaration SET status=? WHERE (id = ? AND status <> ?)
        return this.lambdaUpdate()
                .set(CustomsDeclaration::getStatus, targetStatus)
                .eq(CustomsDeclaration::getId, id)
                // 条件乐观锁更新
                .ne(CustomsDeclaration::getStatus, targetStatus)
                .update();
    }
}
