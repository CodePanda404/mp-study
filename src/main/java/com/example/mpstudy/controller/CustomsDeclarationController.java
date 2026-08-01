package com.example.mpstudy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpstudy.common.Result;
import com.example.mpstudy.service.CustomsDeclarationService;
import com.example.mpstudy.vo.DeclarationDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/declaration")
public class CustomsDeclarationController {

    @Autowired
    private CustomsDeclarationService customsDeclarationService;


    @GetMapping("/detailPage")
    public Result<Page<DeclarationDetailVO>> getDetailPage(@RequestParam(defaultValue = "1") int pageNum,
                                                           @RequestParam(defaultValue = "10") int pageSize,
                                                           @RequestParam(required = false) String declarationNo,
                                                           @RequestParam(required = false) String status) {
        Page<DeclarationDetailVO> detailPage = customsDeclarationService.getDeclarationDetailPageA(pageNum, pageSize, declarationNo, status);
        return Result.success(detailPage);
    }

    @GetMapping("/updateStatus")
    public Result<Boolean> updateDeclarationStatus(@RequestParam(required = true) Long id, @RequestParam(required = true) String status) {
        return Result.success(customsDeclarationService.updateStatusChain(id,status));
    }
}
