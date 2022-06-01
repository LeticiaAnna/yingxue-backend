package cn.annna.controller;

import cn.annna.entity.Admin;
import cn.annna.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/login")
    @ResponseBody
    public Map<String,String> login(@RequestBody Admin admin, String token){
        return adminService.login(admin, token);
    }

    @RequestMapping("/register")
    @ResponseBody
    public Map<String,String> register(@RequestBody Admin admin){
        return adminService.addAdmin(admin);
    }

    @RequestMapping("/getImageCodes")
    @ResponseBody
    public Map<String,String> getImageCodes(){
        return adminService.getImageCodes();
    }

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public Admin getUserInfo(String token){
        try {
            return adminService.getUserInfo(token);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/logout")
    @ResponseBody
    public void logout(String token){
        try {
            adminService.logout(token);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/temp")
    @ResponseBody
    public String temp(){
        try {
            Admin admin = new Admin(null,"smart_cattt",null,"peiqi1314",null,null);
            adminService.addAdmin(admin);
            return "注册成功";
        }catch (Exception e){
            return e.getMessage();
        }
    }

}
