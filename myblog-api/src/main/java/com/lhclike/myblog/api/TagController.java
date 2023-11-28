package com.lhclike.myblog.api;


import com.lhclike.myblog.service.TagsService;
import com.lhclike.myblog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagController {

    @Autowired
    private TagsService tagsService;

    @GetMapping
    public Result listCategory() {
        return tagsService.findAll();
    }
    @GetMapping("detail")
    public Result findAllDetail(){
        return tagsService.findAllDetail();
    }
    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagsService.findDetailById(id);
    }

    @GetMapping("hot")
    public Result hotTags(){
        int limit=2;
        return tagsService.findHotTag(limit);
    }
}