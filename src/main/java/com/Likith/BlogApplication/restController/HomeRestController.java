package com.Likith.BlogApplication.restController;

import com.Likith.BlogApplication.Dto.CommentsDto;
import com.Likith.BlogApplication.Dto.PostsDto;
import com.Likith.BlogApplication.Entity.Comments;
import com.Likith.BlogApplication.Entity.Posts;
import com.Likith.BlogApplication.Entity.Tags;
import com.Likith.BlogApplication.Repository.BlogRepository;
import com.Likith.BlogApplication.Service.CommentService;
import com.Likith.BlogApplication.Service.HomeService;
import com.Likith.BlogApplication.Service.TagService;
import com.Likith.BlogApplication.restDto.PostsRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeRestController {

    private final HomeService homeService;
    private final BlogRepository blogRepository;
    private final TagService tagService;
    private final CommentService commentService;

    @Autowired
    public HomeRestController(HomeService homeService, BlogRepository blogRepository, TagService tagService, CommentService commentService){
        this.homeService = homeService;
        this.blogRepository = blogRepository;
        this.tagService = tagService;
        this.commentService = commentService;
    }

    // GET all posts with search, filter, sort, pagination
    @GetMapping("/posts")
    public ResponseEntity<Page<Posts>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String date
    ) {
        String searchParam = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        String authorParam = (author != null && !author.trim().isEmpty()) ? author.trim() : null;
        LocalDate dateParam = (date != null && !date.trim().isEmpty()) ? LocalDate.parse(date) : null;

        Sort sortOrder = "oldest".equals(sort)
                ? Sort.by("published_at").ascending()
                : Sort.by("published_at").descending();

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Posts> blogPage = blogRepository.searchPosts(searchParam, authorParam, dateParam, pageable);

        return ResponseEntity.ok(blogPage);
    }

    // GET single blog by id (post + comments)
    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getBlogDetails(@PathVariable Long id){
        Posts post = homeService.blogDetails(id);
        if(post == null){
            return ResponseEntity.notFound().build();
        }
        List<Comments> commentList = commentService.getAllCommentsByPostId(id);
        return ResponseEntity.ok(new BlogDetailsResponse(post, commentList));
    }

    // DELETE blog by id
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // POST a comment on a blog
    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody CommentsDto commentsDto){
        Posts post = homeService.blogDetails(id);
        if(post == null){
            return ResponseEntity.notFound().build();
        }
        commentService.blogDetailsComments(post, commentsDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // GET all tags
    @GetMapping("/tags")
    public ResponseEntity<List<Tags>> getAllTags(){
        return ResponseEntity.ok(tagService.getAllTags());
    }

    // POST create a new blog
    @PostMapping("/posts")
    public ResponseEntity<?> createBlog(@RequestBody PostsRequestDto requestDto) throws Exception {
        List<Tags> tagsList = new ArrayList<>();
        if (requestDto.getTagIds() != null) {
            for (Long id : requestDto.getTagIds()) {
                Tags tag = tagService.getTagById(id);
                if (tag != null) tagsList.add(tag);
            }
        }

        PostsDto postsDto = new PostsDto(
                requestDto.getTitle(),
                requestDto.getAuthor(),
                requestDto.getExcerpt(),
                requestDto.getContent(),
                tagsList
        );

        homeService.createBlog(postsDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public static class BlogDetailsResponse {
        private Posts post;
        private List<Comments> comments;

        public BlogDetailsResponse(Posts post, List<Comments> comments) {
            this.post = post;
            this.comments = comments;
        }

        public Posts getPost() { return post; }
        public void setPost(Posts post) { this.post = post; }

        public List<Comments> getComments() { return comments; }
        public void setComments(List<Comments> comments) { this.comments = comments; }
    }
}