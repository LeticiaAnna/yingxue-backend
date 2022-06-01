package cn.annna.controller;

import cn.annna.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


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
}
