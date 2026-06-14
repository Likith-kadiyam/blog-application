package com.Likith.BlogApplication.Controller;

import com.Likith.BlogApplication.Dto.PostsDto;
import com.Likith.BlogApplication.Entity.Posts;
import com.Likith.BlogApplication.Repository.BlogRepository;
import com.Likith.BlogApplication.Service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class BlogController {

    private final BlogRepository blogRepository;
    private final BlogService blogService;

    @Autowired
    public BlogController(BlogRepository blogRepository, BlogService blogService){
        this.blogRepository = blogRepository;
        this.blogService = blogService;
    }

    @GetMapping("/updateBlog/{id}")
    public String updateBlogGet(@PathVariable Long id, Model model){
        Posts post = blogRepository.findById(id).orElse(null);
        if(post == null){

        }
        model.addAttribute("post", post);
        return "updateBlog";
    }

    @PostMapping("/updateBlog/{id}")
    public String updateBlogPost(@RequestParam String title, @RequestParam String author, @RequestParam String excerpt, @RequestParam String content, @PathVariable Long id){
        PostsDto postsDto = new PostsDto();
        postsDto.setAuthor(author);
        postsDto.setExcerpt(excerpt);
        postsDto.setContent(content);
        postsDto.setTitle(title);
        blogService.updateBlogPost(postsDto, id);

        return "redirect:/home/blog/" + id;
    }
}
