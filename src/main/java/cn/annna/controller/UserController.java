package cn.annna.controller;

import cn.annna.entity.User;
import cn.annna.service.UserService;
import cn.annna.vo.UserCountDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/queryAllPage")
    @ResponseBody
    public Map<String,Object> queryAllPage(Integer page, Integer pageSize){
        try {
            return userService.queryAllPage(page,pageSize);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public Map<String,Object> update(@RequestBody User user){
        try {
            return userService.update(user);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Object> delete(@RequestBody User user){
        try {
            return userService.delete(user.getId());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map<String,Object> add(@RequestBody User user){
        System.out.println("user: " + user);
        try {
            return userService.add(user);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/queryById")
    @ResponseBody
    public User queryById(Integer id){
        try {
            return userService.queryById(id);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/uploadHeadImg")
    @ResponseBody
    public Map<String,Object> uploadHeadImg(MultipartFile headImg,String oldHeadImg){
        try {
            return userService.uploadHeadImg(headImg,oldHeadImg);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/deleteHead")
    @ResponseBody
    public Map<String,Object> deleteHead(String oldHeadImg){
        try {
            return userService.deleteHead(oldHeadImg);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/searchUser")
    @ResponseBody
    public List<User> searchUser(String content){
        System.out.println(content);
        try {
            return userService.searchUser(content);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/exportUser")
    @ResponseBody
    public Map<String,Object> exportUser(){
        try {
            return userService.exportUser();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/userCount")
    @ResponseBody
    public UserCountDataVO userCount(){
        try {
            return userService.userCount();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
