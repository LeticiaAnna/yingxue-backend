package cn.annna.service;

import cn.annna.dao.AdminDao;
import cn.annna.entity.Admin;
import cn.annna.util.ImageCodeUtil;
import cn.annna.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String,String> login(Admin admin,String token) {
        Map<String,String> map = new HashMap<>();
        try {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Object o = valueOperations.get("CODE_" + token);
            if (o == null){
                throw new RuntimeException("验证码已过期");
            }
            if (!admin.getEnCode().equals(o.toString())){
                throw new RuntimeException("验证码不正确");
            }
            Admin a = adminDao.selectByUsername(admin.getUsername());
            if (a == null){
                throw new RuntimeException("账号不存在");
            }
            if (!MD5Util.getPassword(admin.getPassword(),a.getSalt()).equals(a.getPassword())){
                throw new RuntimeException("账号密码不正确");
            }
            if (a.getState() != 1){
                throw new RuntimeException("账户也被禁止登录,如有疑问请联系客服");
            }
            map.put("state","success");
            map.put("message",admin.getUsername() + " 欢迎回家");
            valueOperations.set("TOKEN_" + token,admin);
            System.out.println(admin.getUsername() +" 欢迎回家");
            return map;
        }catch (Exception e){
            map.put("state","error");
            map.put("message",e.getMessage());
            return map;
        }
    }

    @Override
    public Map<String,String> addAdmin(Admin admin) {
        Map<String,String> map = new HashMap<>();
        try {
            Admin a = adminDao.selectByUsername(admin.getUsername());
            if (a != null){
                throw new RuntimeException("用户名已存在,请尝试更换一个注册");
            }
            if (admin.getUsername().equals("") || admin.getPassword().equals("")){
                throw new RuntimeException("用户名或密码不允许为空");
            }
            String salt = MD5Util.getSalt();
            admin.setSalt(salt);
            admin.setPassword(MD5Util.getPassword(admin.getPassword(),salt));
            admin.setCreateTime(new Date());
            admin.setState(1);
            adminDao.insert(admin);
            map.put("state","success");
            map.put("message",admin.getUsername() + " 注册成功");
            return map;
        }catch (Exception e){
            map.put("state","error");
            map.put("message",e.getMessage());
            return map;
        }
    }

    @Override
    public Map<String,String> delete(Integer id) {
        Map<String,String> map = new HashMap<>();
        try {
            Admin a = adminDao.selectById(id);
            if (a == null){
                throw new RuntimeException("账号不存在,请规范操作");
            }
            adminDao.delete(id);
            map.put("state","success");
            map.put("message",a.getUsername() + " 删除成功");
            return map;
        }catch (Exception e){
            map.put("state","error");
            map.put("message",e.getMessage());
            return map;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Map<String, String> getImageCodes() {
        try {
            Map<String,String> map = new HashMap<>();
            String code = ImageCodeUtil.getSecurityCode();
            String base64 = ImageCodeUtil.careateImgBase64(code);
            map.put("imgCode",base64);
            String token = UUID.randomUUID().toString();
            map.put("token",token);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set("CODE_" + token,code,1, TimeUnit.MINUTES);
            return map;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
