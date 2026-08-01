package com.example.mpstudy.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mpstudy.entity.CustomsDeclaration;
import com.example.mpstudy.vo.DeclarationDetailVO;

public interface CustomsDeclarationService extends IService<CustomsDeclaration> {
    /**
     * 分页查询报关单详情 QueryMapper+xml模式
     * @param pageNum 页码
     * @param pageSize 每页数
     * @param declarationNo 报关单编号
     * @param status 状态
     * @return 报关单详情分页数据
     */
    Page<DeclarationDetailVO> getDeclarationDetailPageA(int pageNum, int pageSize, String declarationNo, String status);

    /**
     * 纯xml模式
     * @param pageNum 页码
     * @param pageSize 每页数
     * @param declarationNo 报关单编号
     * @param status 状态
     */
    Page<DeclarationDetailVO> getDeclarationDetailPageB(int pageNum, int pageSize, String declarationNo, String status);

    /**
     * MP 极致优雅地链式调用（Chain Call） API
     * @param id 报关单id
     * @param targetStatus 目标状态
     */
    boolean updateStatusChain(Long id, String targetStatus);
}
