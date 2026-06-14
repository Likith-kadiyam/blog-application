package com.Likith.BlogApplication.restController;
import com.Likith.BlogApplication.Dto.PostsDto;
import com.Likith.BlogApplication.Entity.Posts;
import com.Likith.BlogApplication.Repository.BlogRepository;
import com.Likith.BlogApplication.Service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
public class BlogRestController {
    private final BlogRepository blogRepository;
    private final BlogService blogService;

    @Autowired
    public BlogRestController(BlogRepository blogRepository, BlogService blogService){
        this.blogRepository = blogRepository;
        this.blogService = blogService;
    }

    // GET blog by id (for pre-filling update form)
    @GetMapping("/{id}")
    public ResponseEntity<Posts> getBlog(@PathVariable Long id){
        Posts post = blogRepository.findById(id).orElse(null);
        if(post == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    // UPDATE blog by id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody PostsDto postsDto, Authentication authentication){
        Posts existing = blogRepository.findById(id).orElse(null);
        String email = authentication.getName();
        if(existing == null){
            return ResponseEntity.notFound().build();
        }

        blogService.updateBlogPost(postsDto, id);
        return ResponseEntity.ok().build();
    }
}