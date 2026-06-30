package org.example.shop.mapper;

import org.example.shop.model.entity.Follow;

import java.util.List;

public interface FollowMapper {

    int insert(Follow follow);

    int delete(Long userId, Long followUserId);

    int getCount(Long userId, Long followUserId);

    List<Long> getAllUserId(Long followUserId);
}
