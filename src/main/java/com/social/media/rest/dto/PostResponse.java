package com.social.media.rest.dto;

import com.social.media.domain.model.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private String postText;
    private LocalDateTime dateTime;

    public static PostResponse fromEntity(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setPostText(post.getText());
        postResponse.setDateTime(post.getDateTime());

        return postResponse;
    }
}
