package org.example.mybatisdemo.model.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Clazz {
    Long id;
    String name;
    int status; // 0 正在上学, 1已毕业
    Date createTime;
    List<Student> students; // 一对多，一个班级对应多个学生
}
