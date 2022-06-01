package cn.annna;

import cn.annna.dao.UserMapper;
import cn.annna.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MasterApplication.class)
public class TestUser {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectAll() throws Exception {
        List<User> list = userMapper.selectAll();
        for (User user : list) {
            System.out.println(user);
        }
    }

}
