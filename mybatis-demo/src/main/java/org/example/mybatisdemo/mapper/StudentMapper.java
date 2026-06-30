package org.example.mybatisdemo.mapper;

import org.example.mybatisdemo.model.entity.Student;

import java.util.List;

public interface StudentMapper {

    Student getStudentById(Long id); //多对一，查询学生以及对应的班级

    List<Student> getStudentsByClassId(Long cid);

    List<Student> selectAll();
}
