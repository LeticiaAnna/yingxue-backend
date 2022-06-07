package cn.annna;

import cn.annna.dao.UserMapper;
import cn.annna.dao.VideoMapper;
import cn.annna.elasticsearch.UserRepository;
import cn.annna.elasticsearch.VideoRepository;
import cn.annna.entity.User;
import cn.annna.entity.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MasterApplication.class)
public class TestElasticsearch {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoMapper videoMapper;

    @Test
    public void insert() throws Exception {
        User user = new User(1,"test001","test001","test001 Sign","null","110","test001","1",null,"男","北京");
        Date date = new Date();
        System.out.println(date);
        user.setCreateTime(date);
        userRepository.save(user);
    }

    @Test
    public void selectAll() throws Exception {
        Iterable<User> list = userRepository.findAll();

        for (User user : list) {
            System.out.println(user);
        }
    }

    @Test
    public void delete() throws Exception {
        userRepository.deleteById(1);
    }

    @Test
    public void insertAll() throws Exception {
        List<User> list = userMapper.selectAll();
        userRepository.saveAll(list);

    }


}
