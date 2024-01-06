package com.social.media.quarkus.social.rest.dto;

import com.social.media.quarkus.social.domain.model.Post;
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
