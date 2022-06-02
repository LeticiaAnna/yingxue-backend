package cn.annna.controller;


import cn.annna.entity.Category;
import cn.annna.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/queryOnePage")
    @ResponseBody
    public Map<String, Object> queryOnePage(Integer page,Integer pageSize){
        try {
            return categoryService.queryOnePage(page,pageSize);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/queryTwoPage")
    @ResponseBody
    public Map<String,Object> queryTwoPage(Integer page, Integer pageSize, Integer categoryId){
        try {
            return categoryService.queryTwoPage(page,pageSize,categoryId);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map<String,Object> add(@RequestBody Category category){
        try {
            return categoryService.add(category);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Object> delete(@RequestBody Category category){
        System.out.println(category);
        try {
            return categoryService.delete(category);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/queryByLevelsCategory")
    @ResponseBody
    public List<Category> queryByLevelsCategory(Integer levels){
        return categoryService.queryByLevelsCategory(levels);
    }

    @RequestMapping("/queryById")
    @ResponseBody
    public Category queryById(Integer id){
        try {
            return categoryService.queryById(id);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public Map<String,Object> update(@RequestBody Category category){
        try {
            return categoryService.update(category);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
