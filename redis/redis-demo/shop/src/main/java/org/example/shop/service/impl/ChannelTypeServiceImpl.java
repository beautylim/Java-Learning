package org.example.shop.service.impl;

import org.example.shop.mapper.ChannelTypeMapper;
import org.example.shop.model.entity.ChannelType;
import org.example.shop.service.ChannelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelTypeServiceImpl implements ChannelTypeService {

    @Autowired
    private ChannelTypeMapper channelTypeMapper;

    @Override
    public List<ChannelType> getArticleTypeList() {
        return channelTypeMapper.getArticleTypeList();
    }
}
