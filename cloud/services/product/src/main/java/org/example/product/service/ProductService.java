package org.example.product.service;


import org.example.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(Long id);
}
