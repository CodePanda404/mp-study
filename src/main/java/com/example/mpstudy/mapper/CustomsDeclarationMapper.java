package com.example.mpstudy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpstudy.entity.CustomsDeclaration;
import com.example.mpstudy.vo.DeclarationDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomsDeclarationMapper extends BaseMapper<CustomsDeclaration> {
    /**
     * 多表联查 + MP 动态条件 + 分页
     * @param page MP 分页参数
     * @param queryWrapper 动态条件构造器. 使用 %{ew.customSqlSegment} 自动拼装 Where 条件
     */
    IPage<DeclarationDetailVO> selectDeclarationPage(Page<DeclarationDetailVO> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<CustomsDeclaration> queryWrapper);
}
