package org.example.order.feign;

import org.example.model.Product;
import org.example.order.feign.fallback.ProductFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "product", fallback =  ProductFeignFallback.class)
public interface ProductFeignClient {

    @GetMapping("/product/{id}")
    Product getProduct(@PathVariable Long id);
}
