package com.Likith.BlogApplication.Repository;

import com.Likith.BlogApplication.Entity.Comments;
import com.Likith.BlogApplication.Entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {

    List<Comments> findAllByPosts(Posts posts);
}
