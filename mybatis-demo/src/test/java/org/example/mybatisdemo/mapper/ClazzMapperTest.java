package org.example.mybatisdemo.mapper;

import org.example.mybatisdemo.model.entity.Clazz;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ClazzMapperTest {

    @Autowired
    private ClazzMapper clazzMapper;

    @Test
    @Transactional
    void getClazzAndStudentById() {
        Clazz clazz = clazzMapper.getClazzAndStudentById(1L);
        System.out.println(clazz.getName());
        clazz = clazzMapper.getClazzAndStudentById(1L);
        System.out.println(clazz.getStudents().size());
    }
}