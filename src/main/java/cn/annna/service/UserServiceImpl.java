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

    @Override
    public Map<String, Object> update(User user) {
        Map<String, Object> map = new HashMap<>();
        try {
            User u = userMapper.selectByPrimaryKey(user.getId());
            if (u == null){
                throw new RuntimeException("用户不存在,请规范操作");
            }
            userMapper.updateByPrimaryKeySelective(user);
            map.put("message",u.getUsername() + " 信息修改成功");
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    public Map<String, Object> delete(Integer id) {
        Map<String, Object> map = new HashMap<>();
        try {
            User u = userMapper.selectByPrimaryKey(id);
            if (u == null){
                throw new RuntimeException("用户不存在,请规范操作");
            }
            userMapper.deleteByPrimaryKey(id);
            map.put("message",u.getUsername() + " 账户删除成功");
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    public Map<String, Object> add(User user) {
        Map<String, Object> map = new HashMap<>();
        try {
            User u = new User();
            u.setUsername(user.getUsername());
            User u2 = userMapper.selectOne(u);
            if (u2 != null){
                throw new RuntimeException("用户名已存在,请更换一个");
            }
            userMapper.insertSelective(user);
            map.put("message",user.getUsername() + " 账户添加成功");
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    public User queryById(Integer id) {
        try {
            User u = userMapper.selectByPrimaryKey(id);
            System.out.println("queryById：" + u);
            if (u == null){
                throw new RuntimeException("用户不存在,请规范操作");
            }
            return u;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}