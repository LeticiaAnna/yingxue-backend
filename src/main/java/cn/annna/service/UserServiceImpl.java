package cn.annna.service;

import cn.annna.dao.UserMapper;
import cn.annna.entity.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String,Object> queryAllPage(Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        try {
            if (page <= 0){
                throw new RuntimeException("请求页码不正确,请规范操作");
            }
            int total = userMapper.selectCount(null);
            List<User> list = userMapper.selectByRowBounds(new User(), new RowBounds((page - 1) * pageSize, pageSize));
            map.put("total",total);
            map.put("page",page);
            map.put("rows",list);
            return map;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
