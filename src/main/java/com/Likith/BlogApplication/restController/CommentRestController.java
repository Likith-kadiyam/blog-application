package com.Likith.BlogApplication.restController;

import com.Likith.BlogApplication.restDto.CommentRequestDto;
import com.Likith.BlogApplication.Entity.Comments;
import com.Likith.BlogApplication.Repository.CommentRepository;
import com.Likith.BlogApplication.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentRepository commentRepository, CommentService commentService){
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }

    // GET single comment by id
    @GetMapping("/{id}")
    public ResponseEntity<Comments> getComment(@PathVariable Long id) {
        Comments comment = commentRepository.findById(id)
                .orElseThrow();
        return ResponseEntity.ok(comment);
    }

    // DELETE a comment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        Comments comment = commentRepository.findById(id)
                .orElseThrow();
        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // UPDATE a comment
    @PutMapping("/{id}")
    public ResponseEntity<Comments> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        Comments existing = commentRepository.findById(id)
                .orElseThrow();
        existing.setName(requestDto.getName());
        existing.setComment(requestDto.getComment());
        commentRepository.save(existing);
        return ResponseEntity.ok(existing);
    }
}