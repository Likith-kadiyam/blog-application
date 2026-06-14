package com.Likith.BlogApplication.Controller;

import com.Likith.BlogApplication.Dto.CommentsDto;
import com.Likith.BlogApplication.Dto.PostsDto;
import com.Likith.BlogApplication.Entity.Comments;
import com.Likith.BlogApplication.Entity.Posts;
import com.Likith.BlogApplication.Entity.Tags;
import com.Likith.BlogApplication.Repository.BlogRepository;
import com.Likith.BlogApplication.Service.CommentService;
import com.Likith.BlogApplication.Service.HomeService;
import com.Likith.BlogApplication.Service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;
    private final BlogRepository blogRepository;
    private final TagService tagService;
    private final CommentService commentService;

    public HomeController(HomeService homeService, BlogRepository blogRepository, TagService tagService, CommentService commentService){
        this.homeService = homeService;
        this.blogRepository = blogRepository;
        this.tagService = tagService;
        this.commentService = commentService;
    }

    @GetMapping("")
    public String home(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String date,
            Model model
    ) {
        String searchParam = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        String authorParam = (author != null && !author.trim().isEmpty()) ? author.trim() : null;
        LocalDate dateParam = (date != null && !date.trim().isEmpty()) ? LocalDate.parse(date) : null;

        Sort sortOrder = "oldest".equals(sort)
                ? Sort.by("published_at").ascending()
                : Sort.by("published_at").descending();

        Pageable pageable = PageRequest.of(page, 10, sortOrder);

        Page<Posts> blogPage = blogRepository.searchPosts(searchParam, authorParam, dateParam, pageable);

        model.addAttribute("blogPage", blogPage);
        model.addAttribute("search", search);
        model.addAttribute("author", author);
        model.addAttribute("sort", sort);
        model.addAttribute("date", date);
        return "home";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable Long id, Model model){
       Posts post =  homeService.blogDetails(id);
       List<Comments> commentList = commentService.getAllCommentsByPostId(id);
       if(post == null){
           return "redirect:/home";
       }
       model.addAttribute("post", post);
       model.addAttribute("commentList", commentList);
       return "blogDetails";
    }
    @PostMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable Long id) {

        blogRepository.deleteById(id);

        return "redirect:/home";
    }
    @PostMapping("/blog/{id}/comment")
    public String blogDetailsComments(@PathVariable Long id, Model model, @RequestParam String name, @RequestParam String email,  @RequestParam String comment){
        Posts post =  homeService.blogDetails(id);
        CommentsDto commentsDto = new CommentsDto(name, email, comment);
        commentService.blogDetailsComments(post, commentsDto);

        return "redirect:/home/blog/" + id;
    }

    @GetMapping("/createBlog")
    public String createBlogPage(Model model){
        List<Tags> tagsList = tagService.getAllTags();
        model.addAttribute("tagsList", tagsList);
        return "createBlog";
    }

    @PostMapping("/createBlog")
    public String createBlog( @RequestParam String title, @RequestParam String author,  @RequestParam String excerpt, @RequestParam String content, @RequestParam List<Long> tagIds) throws Exception {
        List<Tags> tagsList = new ArrayList<>();
        if (tagIds != null) {
            for (Long id : tagIds) {
                Tags tag = tagService.getTagById(id);
                if (tag != null) tagsList.add(tag);
            }
        }

        PostsDto postsDto = new PostsDto(title, author, excerpt, content, tagsList);
        homeService.createBlog(postsDto);
        return "redirect:/home";
    }
}
