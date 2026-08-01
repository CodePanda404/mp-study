package com.example.mpstudy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpstudy.entity.DeclarationItem;
import com.example.mpstudy.mapper.DeclarationItemMapper;
import com.example.mpstudy.service.DeclarationItemService;
import org.springframework.stereotype.Service;

@Service
public class DeclarationItemServiceImpl extends ServiceImpl<DeclarationItemMapper, DeclarationItem> implements DeclarationItemService {
}
