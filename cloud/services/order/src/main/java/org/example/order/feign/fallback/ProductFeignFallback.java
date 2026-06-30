package org.example.order.feign.fallback;

import org.example.model.Product;
import org.example.order.feign.ProductFeignClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductFeignFallback implements ProductFeignClient {
    @Override
    public Product getProduct(Long id) {
        System.out.println("兜底回调");
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Fallback Product");
        product.setPrice(new BigDecimal("1000.00"));
        product.setQuantity(0);
        return product;
    }
}
