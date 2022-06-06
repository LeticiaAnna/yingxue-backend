package cn.annna;

import cn.annna.dao.VideoMapper;
import cn.annna.entity.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MasterApplication.class)
public class TestVideo {
    @Autowired
    private VideoMapper videoMapper;

    @Test
    public void selectAll() throws Exception {
        List<Video> list = videoMapper.selectAll();
        for (Video video : list) {
            System.out.println(video);
        }
    }

}
