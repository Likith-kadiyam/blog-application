package com.Likith.BlogApplication.Service;

import com.Likith.BlogApplication.Dto.CommentsDto;
import com.Likith.BlogApplication.Entity.Comments;
import com.Likith.BlogApplication.Entity.Posts;
import com.Likith.BlogApplication.Repository.BlogRepository;
import com.Likith.BlogApplication.Repository.CommentRepository;
import com.Likith.BlogApplication.exception.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository, BlogRepository blogRepository){
        this.commentRepository = commentRepository;
        this.blogRepository = blogRepository;
    }
    @Transactional
    public void blogDetailsComments(Posts post, CommentsDto commentsDto){
        Comments comment = new Comments();
        comment.setName(commentsDto.getName());
        comment.setEmail(commentsDto.getEmail());
        comment.setComment(commentsDto.getComment());
        comment.setPosts(post);

        commentRepository.save(comment);
    }
    public List<Comments> getAllCommentsByPostId(Long postId){
        Posts post = blogRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        return commentRepository.findAllByPosts(post);
    }

}
