package cn.annna.service;

import cn.annna.dao.VideoMapper;
import cn.annna.entity.User;
import cn.annna.entity.Video;
import cn.annna.entity.VideoExample;
import cn.annna.util.OSSUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class VideoServiceImpl implements VideoService{
    @Autowired
    private VideoMapper videoMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String, Object> queryAllPage(Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        try {
            if (page <= 0){
                throw new RuntimeException("请求页码不正确,请规范操作");
            }
            int total = videoMapper.selectCount(null);
            List<Video> list = videoMapper.selectByRowBounds(new Video(), new RowBounds((page - 1) * pageSize, pageSize));
            map.put("total",total);
            map.put("page",page);
            map.put("rows",list);
            return map;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> update(Video video) {
        Map<String, Object> map = new HashMap<>();
        try {
            Video v = videoMapper.selectByPrimaryKey(video.getId());
            if (v == null){
                throw new RuntimeException("视频不存在,请规范操作");
            }
            videoMapper.updateByPrimaryKeySelective(video);
            map.put("message",v.getTitle() + " 信息修改成功");
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
            Video v = videoMapper.selectByPrimaryKey(id);
            if (v == null){
                throw new RuntimeException("视频不存在,请规范操作");
            }
            OSSUtil.deleteFile(v.getVideoPath());
            OSSUtil.deleteFile(v.getCoverPath());
            videoMapper.deleteByPrimaryKey(id);
            map.put("message",v.getTitle() + " 视频删除成功");
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    public Map<String, Object> add(Video video) {
        Map<String, Object> map = new HashMap<>();
        try {
            video.setCreateTime(new Date());
            video.setStatus("1");
            videoMapper.insertSelective(video);
            map.put("message",video.getTitle() + " 视频添加成功");
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    public Video queryById(Integer id) {
        try {
            Video v = videoMapper.selectByPrimaryKey(id);
            System.out.println("queryById：" + v);
            if (v == null){
                throw new RuntimeException("视频不存在,请规范操作");
            }
            return v;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> uploadVideo(MultipartFile videoFile, String oldVideo) {
        System.out.println("文件类型: " + videoFile.getContentType());
        System.out.println("oldVideo: " + oldVideo);
        Map<String, Object> map = new HashMap<>();
        try {
            if (!Objects.equals(videoFile.getContentType(), "video/mp4")){
                throw new RuntimeException("只允许上传 mp4 格式的视频,请规范操作");
            }
            if (videoFile.getSize() > 20971520){
                throw new RuntimeException("视频大小大于 20MB ,请调整");
            }
            if (!oldVideo.equals("")){
                System.out.println("视频更新");
                OSSUtil.deleteFile(oldVideo);
                String fileName = OSSUtil.uploadFile(videoFile,"yingxue/videos/video/");
                map.put("message","视频更新成功");
                map.put("fileName",fileName);
                map.put("coverName","123");
                map.put("status",200);
            }else {
                System.out.println("视频上传");
                String fileName = OSSUtil.uploadFile(videoFile,"yingxue/videos/video/");
                map.put("message","视频上传成功");
                map.put("fileName",fileName);
                map.put("coverName","1231");
                map.put("status",200);
            }
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    public List<Video> searchVideo(String content) {
        try {
            VideoExample videoExample = new VideoExample();
            videoExample.createCriteria().andTitleLike("%" + content + "%");
            return videoMapper.selectByExample(videoExample);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
