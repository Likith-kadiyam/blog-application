package com.Likith.BlogApplication.Service;

import com.Likith.BlogApplication.Dto.PostsDto;
import com.Likith.BlogApplication.Entity.PostTags;
import com.Likith.BlogApplication.Entity.Posts;
import com.Likith.BlogApplication.Entity.Tags;
import com.Likith.BlogApplication.Entity.User;
import com.Likith.BlogApplication.Repository.BlogRepository;
import com.Likith.BlogApplication.Repository.UserRepository;
import com.Likith.BlogApplication.exception.AccessDeniedException;
import com.Likith.BlogApplication.exception.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Autowired
    public HomeService(BlogRepository blogRepository, UserRepository userRepository){
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createBlog(PostsDto postsDto){

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow();

        boolean isAuthor =
                currentUser.getRole()
                        .equals("ROLE_AUTHOR");

        boolean isAdmin =
                currentUser.getRole()
                        .equals("ROLE_ADMIN");

        if(!isAuthor && !isAdmin){
            throw new AccessDeniedException(
                    "Only authors and admins can create blogs"
            );
        }
        Posts post = new Posts();
        post.setTitle(postsDto.getTitle());
        if(isAdmin){
            post.setAuthor(postsDto.getAuthor());
        }else{
            post.setAuthor(currentUser.getName());
        }
        post.setExcerpt(postsDto.getExcerpt());
        post.setContent(postsDto.getContent());
        List<PostTags> postTagsList = new ArrayList<>();
        for (Tags tag : postsDto.getTagsList()) {
            PostTags postTag = new PostTags();
            postTag.setPosts(post);
            postTag.setTags(tag);
            postTagsList.add(postTag);
        }
        post.setPostTags(postTagsList);
        blogRepository.save(post);
    }

    public Posts blogDetails(Long id) {
        return blogRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }
}
