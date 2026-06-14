package com.Likith.BlogApplication.restDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostsRequestDto {
    private String title;
    private String author;
    private String excerpt;
    private String content;
    private List<Long> tagIds;

}