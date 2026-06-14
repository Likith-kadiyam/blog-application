package com.Likith.BlogApplication.Service;

import com.Likith.BlogApplication.Dto.PostsDto;
import com.Likith.BlogApplication.Dto.TagDto;
import com.Likith.BlogApplication.Entity.PostTags;
import com.Likith.BlogApplication.Entity.Posts;
import com.Likith.BlogApplication.Entity.Tags;
import com.Likith.BlogApplication.Repository.BlogRepository;
import com.Likith.BlogApplication.Repository.TagRepository;
import com.Likith.BlogApplication.exception.PostNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final BlogRepository blogRepository;

    @Autowired
    public TagService(TagRepository tagRepository, BlogRepository blogRepository){
        this.tagRepository = tagRepository;
        this.blogRepository = blogRepository;
    }

    @Transactional
    public void addTagPost(TagDto tagDto){
        Tags tag = new Tags();
        tag.setTagName(tagDto.getTagName());
        tagRepository.save(tag);
    }
    public List<Tags> getAllTags(){
        List<Tags> list = tagRepository.findAll();
        return list;
    }
    public Tags getTagById(Long id) throws Exception {
        return tagRepository.findById(id).orElseThrow(() -> new Exception());
    }
    @Transactional
    public void addTagToBlog(List<Tags> tagsList, Long id) {
        Posts post = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        List<PostTags> postTags = post.getPostTags();
        for (Tags tag : tagsList) {
            boolean alreadyExists = postTags.stream()
                    .anyMatch(pt -> pt.getTags().getId() == (tag.getId()));
            if (!alreadyExists) {
                PostTags postTag = new PostTags();
                postTag.setPosts(post);
                postTag.setTags(tag);
                postTags.add(postTag);
            }
        }
        blogRepository.save(post);
    }

}
