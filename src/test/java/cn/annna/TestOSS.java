package cn.annna;

import cn.annna.util.OSSUtil;
import org.junit.Test;

public class TestOSS {

    @Test
    public void delete() throws Exception {
        OSSUtil.deleteFile("https://cat-hk.oss-cn-hongkong.aliyuncs.com/yingxue/videos/video/1585660250687-火花.mp4");
    }

}
