package com.Likith.BlogApplication.Controller;

import com.Likith.BlogApplication.Entity.Comments;
import com.Likith.BlogApplication.Repository.CommentRepository;
import com.Likith.BlogApplication.Service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class commentController {
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    public commentController(CommentRepository commentRepository, CommentService commentService){
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }
    @PostMapping("/comments/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        Comments comment = commentRepository.findById(id)
                .orElseThrow();
        Long postId = comment.getPosts().getId();
        commentRepository.deleteById(id);
        return "redirect:/home/blog/" + postId;
    }

    @GetMapping("/comments/edit/{id}")
    public String editCommentPage(@PathVariable Long id,
                                  Model model) {
        Comments comment = commentRepository.findById(id)
                .orElseThrow();
        model.addAttribute("comment", comment);
        return "editComment";
    }

    @PostMapping("/comments/update/{id}")
    public String updateComment(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam String comment) {

        Comments existing = commentRepository.findById(id)
                .orElseThrow();

        existing.setName(name);
        existing.setComment(comment);

        commentRepository.save(existing);

        Long postId = existing.getPosts().getId();

        return "redirect:/home/blog/" + postId;
    }
}
