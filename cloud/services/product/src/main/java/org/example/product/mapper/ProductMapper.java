package org.example.product.mapper;


import org.example.model.Product;

import java.util.List;

public interface ProductMapper {

    List<Product> selectAll();

    Product selectById(Long id);
}
