package cn.annna.service;

import cn.annna.entity.Admin;

import java.util.Map;

public interface AdminService {

    public Map<String,String> login(Admin admin,String token);

    public Map<String,String> addAdmin(Admin admin);

    public Map<String,String> delete(Integer id);

    public Map<String,String> getImageCodes();

}
