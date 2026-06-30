package org.example.product.service.impl;

import org.example.model.Product;
import org.example.product.mapper.ProductMapper;
import org.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> findAll() {
        return productMapper.selectAll();
    }

    @Override
    public Product findById(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setPrice(new BigDecimal(10000));
        product.setDescription("Iphone 17");
        product.setQuantity(1000);
        return product;
    }
}
