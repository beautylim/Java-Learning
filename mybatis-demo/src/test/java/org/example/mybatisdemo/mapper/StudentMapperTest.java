package org.example.mybatisdemo.mapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.mybatisdemo.model.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StudentMapperTest {

    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void testFindStudentById() { //测试延迟加载
        Student student = studentMapper.getStudentById(1L);
        System.out.println(student.getName());
        System.out.println(student.getClazz().getName());
    }

    @Test
    public void testFindStudentByName() {
        PageHelper.startPage(2, 3);
        List<Student> students = studentMapper.selectAll();
        PageInfo<Student> pageInfo = new PageInfo<>(students);
        System.out.println(pageInfo);
    }



}