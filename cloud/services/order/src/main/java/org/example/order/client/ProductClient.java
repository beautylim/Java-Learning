package org.example.order.client;

import org.example.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    public Product getProduct(Long productId){
        return restTemplate.getForObject("http://product/product/"+productId, Product.class);
    }
}
