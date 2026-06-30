package org.example.mybatisdemo.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Student {
    Long id;
    String name;
    Date birthday;
    Clazz clazz; //一个学生对应一个班级
    int status; //0 正在上学,1 已经毕业, 2休学肄业
    Date createTime;
}
