package ai.verse;


import ai.verse.repo.PostEntity;
import ai.verse.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("api")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @GetMapping("/check")
    public String check() {
        return "OK";
    }


    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public ResponseEntity<List> getPosts() {
        List<PostEntity> list = postRepository.findAll();
        System.out.println("-------------------LIST IS :" + list);
        return new ResponseEntity(list, HttpStatus.OK);
    }
    @RequestMapping(value = "/graphdata", method = RequestMethod.GET)
    public ResponseEntity<List> getGraphData() {
        List list = postRepository.findAggregateData();
        System.out.println("-------------------LIST IS :" + list);
        return new ResponseEntity(list, HttpStatus.OK);
    }
}


