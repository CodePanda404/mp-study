package com.example.mpstudy;

import com.example.mpstudy.entity.CustomsDeclaration;
import com.example.mpstudy.mapper.CustomsDeclarationMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CustomsDeclarationTest {

    @Autowired
    private CustomsDeclarationMapper customsDeclarationMapper;

    @Test
    public void testQuerySingle() {
        // mybatis-plus 单表查询
        CustomsDeclaration declaration = customsDeclarationMapper.selectById(3001L);
        System.out.println("成功读取报关单主表数据: " + declaration);
        assertNotNull(declaration);
    }
}
