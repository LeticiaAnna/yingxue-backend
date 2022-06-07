package cn.annna.controller;


import cn.annna.entity.User;
import cn.annna.entity.Video;
import cn.annna.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping("/video")
@CrossOrigin
@Controller
public class VideoController {
    @Autowired
    private VideoService videoService;

    @RequestMapping("/queryAllPage")
    @ResponseBody
    public Map<String, Object> queryAllPage(Integer page, Integer pageSize) {
        System.out.println(page);
        System.out.println(pageSize);
        try {
            return videoService.queryAllPage(page, pageSize);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public Map<String, Object> update(@RequestBody Video video) {
        try {
            return videoService.update(video);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestBody Video video) {
        try {
            return videoService.delete(video.getId());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map<String, Object> add(@RequestBody Video video) {
        System.out.println("video: " + video);
        try {
            return videoService.add(video);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/queryById")
    @ResponseBody
    public Video queryById(Integer id) {
        try {
            return videoService.queryById(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/uploadVideo")
    @ResponseBody
    public Map<String, Object> uploadVideo(MultipartFile videoFile, String oldVideo) {
        //System.out.println("videoFile" + videoFile);
        try {
            return videoService.uploadVideo(videoFile, oldVideo);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/searchVideo")
    @ResponseBody
    public List<Video> searchVideo(String content){
        System.out.println(content);
        try {
            return videoService.searchVideo(content);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
