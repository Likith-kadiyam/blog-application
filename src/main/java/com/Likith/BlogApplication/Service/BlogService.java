package com.Likith.BlogApplication.Service;

import com.Likith.BlogApplication.Dto.PostsDto;
import com.Likith.BlogApplication.Entity.Posts;
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

@Service
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository, UserRepository userRepository){
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void updateBlogPost(PostsDto postsDto, Long id){
        Posts post = blogRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow();

        boolean isOwner =
                post.getAuthor().equals(currentUser.getName());

        boolean isAdmin = currentUser.getRole().equals("ROLE_ADMIN");

        if(!isOwner && !isAdmin){

            throw new AccessDeniedException(
                    "Not allowed");
        }
        System.out.println("Post Author = " + post.getAuthor());
        System.out.println("Current User = " + currentUser.getName());
        System.out.println("Is Owner = " + isOwner);
        System.out.println("Is Admin = " + isAdmin);

        post.setContent(postsDto.getContent());
        post.setExcerpt(postsDto.getExcerpt());
        post.setTitle(postsDto.getTitle());
        post.setAuthor(postsDto.getAuthor());

        blogRepository.save(post);
    }
}
