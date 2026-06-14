package com.Likith.BlogApplication.Controller;

import com.Likith.BlogApplication.Dto.PostsDto;
import com.Likith.BlogApplication.Dto.TagDto;
import com.Likith.BlogApplication.Entity.Tags;
import com.Likith.BlogApplication.Service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tag")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping("/add")
    public String addTag(){
        return "addTag";
    }

    @PostMapping("/add")
    public String addTagPost(@RequestParam String name){
        TagDto tagDto = new TagDto(name);
        tagService.addTagPost(tagDto);
        return "addTag";
    }

    @GetMapping("")
    public String tag(Model model){
        List<Tags> tagsList = tagService.getAllTags();
        model.addAttribute("tagsList", tagsList);
        return "tags";
    }

    @GetMapping("/addTagToBlog/{id}")
    public String addTagToBlog(Model model, @PathVariable Long id){
        List<Tags> tagsList = tagService.getAllTags();
        model.addAttribute("tagsList", tagsList);
        model.addAttribute("blogId", id);
        return "addTagToBlog";
    }

    @PostMapping("/addTagToBlog/{id}")
    public String addTagToBlogPost(@RequestParam List<Long> tagIds, @PathVariable Long id) throws Exception {
        List<Tags> tagsList = new ArrayList<>();
        if (tagIds != null) {
            for (Long Id : tagIds) {
                Tags tag = tagService.getTagById(Id);
                if (tag != null) tagsList.add(tag);
            }
        }
        tagService.addTagToBlog(tagsList, id);
        return "redirect:/home/blog/"+id;
    }
}
