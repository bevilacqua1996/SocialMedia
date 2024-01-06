package com.social.media.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class FollowerPerUserResponse {
    private Integer followerCount;
    private List<FollowerResponse> followerResponseList;
}
