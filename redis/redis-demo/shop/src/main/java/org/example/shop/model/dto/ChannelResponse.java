package org.example.shop.model.dto;

import lombok.Data;
import org.example.shop.model.entity.ChannelType;

import java.util.List;

@Data
public class ChannelResponse {
    private List<ChannelType> data;
}
