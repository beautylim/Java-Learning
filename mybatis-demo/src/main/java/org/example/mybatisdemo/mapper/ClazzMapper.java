package org.example.mybatisdemo.mapper;

import org.example.mybatisdemo.model.entity.Clazz;

public interface ClazzMapper {

    Clazz getClazzById(Long id);

    Clazz getClazzAndStudentById(Long id);
}
