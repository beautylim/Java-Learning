package org.example.shop.service;

import org.example.auth.model.entity.SysUser;

import java.util.List;

public interface FollowService {

    boolean follow(Long followUserId, boolean isFollow);

    boolean isFollow(Long id);

    List<SysUser> commonFollow(Long id);
}
