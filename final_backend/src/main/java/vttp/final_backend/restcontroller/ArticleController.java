package vttp.final_backend.restcontroller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vttp.final_backend.model.Article;
import vttp.final_backend.model.Section;
import vttp.final_backend.service.ArticleService;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    
    @Autowired
    private ArticleService articleService;

    @GetMapping("/sections")
    public ResponseEntity<List<Section>> getSections(){
        return ResponseEntity.ok().body(articleService.getSectionsFromMongo());
    }

    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<Article>> getFeed(@PathVariable String userId, @RequestParam String page) throws IOException, ParseException{
        return ResponseEntity.ok().body(articleService.collateArticles(userId, page));
    }


}
