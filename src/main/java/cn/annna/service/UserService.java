package cn.annna.service;

import cn.annna.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    public Map<String,Object> queryAllPage(Integer page, Integer pageSize);

}
