package com.Likith.BlogApplication.Dto;

import com.Likith.BlogApplication.Entity.Tags;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostsDto {

    private String title;
    private String author;
    private String excerpt;
    private String content;
    private List<Tags> tagsList;

}
