package cn.annna.service;

import cn.annna.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    public Map<String,Object> queryAllPage(Integer page, Integer pageSize);

    public Map<String,Object> update(User user);

    public Map<String,Object> delete(Integer id);

    public Map<String,Object> add(User user);

    public User queryById(Integer id);


}
