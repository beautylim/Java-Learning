package org.example.shop.clients;

import org.example.shop.model.entity.Blog;
import org.springframework.stereotype.Component;

@Component
public class RedisBlogClient extends RedisCacheClient<Blog> {
    public RedisBlogClient() {
        super(Blog.class);
    }
}
