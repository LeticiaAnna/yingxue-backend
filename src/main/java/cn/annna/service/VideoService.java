package cn.annna.service;

import cn.annna.entity.User;
import cn.annna.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface VideoService {

    public Map<String,Object> queryAllPage(Integer page,Integer pageSize);

    public Map<String,Object> update(Video video);

    public Map<String,Object> delete(Integer id);

    public Map<String,Object> add(Video video);

    public Video queryById(Integer id);

    public Map<String,Object> uploadVideo(MultipartFile videoFile, String oldVideo);

    public List<Video> searchVideo(String content);

    public Map<String,Object> exportVideo();

}
