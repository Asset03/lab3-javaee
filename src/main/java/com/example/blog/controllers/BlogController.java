package com.example.blog.controllers;

import com.example.blog.models.Post;
import com.example.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts",posts);
        return  "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model){
        return  "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title,@RequestParam String anons,@RequestParam String full_text, Model model){
        Post post = new Post(title,anons,full_text);
        postRepository.save(post);
        return "";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(Model model,@PathVariable(value = "id") long id){
        if(!postRepository.existsById(id)){
            return "redirect:/blog" ;
        }
        Optional<Post> post = postRepository.findById(id);
        postRepository.findById(id).ifPresent(el->{
            int view = el.getViews();
            el.setViews(view+1);
            postRepository.save(el);
        });
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post",res);
        return  "blog-details";
    }
    @GetMapping("/blog/{id}/edit")
    public String blogEdit(Model model, @PathVariable(value = "id") long id){
        if(!postRepository.existsById(id)){
            return "redirect:/blog" ;
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post",res);
        return  "blog-edit";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(Model model, @PathVariable(value = "id") long id){
        if(!postRepository.existsById(id)){
            return "redirect:/blog" ;
        }
        postRepository.deleteById(id);
        return  "redirect:/blog";
    }

}
