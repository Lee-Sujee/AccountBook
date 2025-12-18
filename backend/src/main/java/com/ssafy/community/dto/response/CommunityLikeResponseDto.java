package com.ssafy.community.dto.response;

import lombok.Data;

@Data
public class CommunityLikeResponseDto {
	public CommunityLikeResponseDto(boolean liked, int likes) {
        this.liked = liked;
        this.likes = likes;
    }
	private boolean liked;
	private int likes;
}
