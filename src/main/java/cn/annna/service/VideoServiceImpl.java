package cn.annna.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.annna.dao.VideoMapper;
import cn.annna.elasticsearch.VideoRepository;
import cn.annna.entity.Video;
import cn.annna.util.OSSUtil;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VideoServiceImpl implements VideoService{
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private VideoRepository videoRepository;

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
            videoRepository.save(video);
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
            videoRepository.deleteById(id);
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
            if (video.getVideoPath().equals("")){
                throw new RuntimeException("视频路径不允许为空,请检查视频是否上传");
            }
            video.setCreateTime(new Date());
            video.setStatus("1");
            videoMapper.insertSelective(video);
            videoRepository.save(video);
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
    @Transactional(propagation = Propagation.SUPPORTS)
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
            if (videoFile.getSize() > 104857600){
                throw new RuntimeException("视频大小大于 100MB ,请调整");
            }
            // !oldVideo.equals("") ||
            if (oldVideo != null){
                System.out.println("视频更新");
                OSSUtil.deleteFile(oldVideo);
                String videoCover = oldVideo.replace(".mp4",".jpg");
                System.out.println("videoCover: " + videoCover);
                videoCover = videoCover.replace("yingxue/videos/video/","yingxue/videos/cover/");
                System.out.println("videoCover: " + videoCover);
                OSSUtil.deleteFile(videoCover);
                String fileName = OSSUtil.uploadFile(videoFile,"yingxue/videos/video/");
                String coverPath = OSSUtil.frameAndUpdate(fileName);
                map.put("message","视频更新成功");
                map.put("fileName",fileName);
                map.put("coverName",coverPath);
                map.put("status",200);
            }else {
                System.out.println("视频上传");
                String fileName = OSSUtil.uploadFile(videoFile,"yingxue/videos/video/");
                String coverPath = OSSUtil.frameAndUpdate(fileName);
                map.put("message","视频上传成功");
                map.put("fileName",fileName);
                map.put("coverName",coverPath);
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
            //VideoExample videoExample = new VideoExample();
            //videoExample.createCriteria().andTitleLike("%" + content + "%");
            //return videoMapper.selectByExample(videoExample);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.queryStringQuery(content)
                    .field("title")
                    .field("description"));
            // 设置高亮显示
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder
                    .preTags("<font color='red'>")
                    .postTags("</font>")
                    .requireFieldMatch(false)
                    .field("*");
            sourceBuilder.highlighter(highlightBuilder);
            // 查询的请求对象
            SearchRequest searchRequest = new SearchRequest("yingxue_videos").types("video").source(sourceBuilder);
            // 执行查询，获取查询到的响应对象
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 结果处理
            List<Video> list = new ArrayList<>();
            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                Map<String, HighlightField> highlightFieldsMap = hit.getHighlightFields();

                // 把查到的原文档 转换为 java对象
                int id = Integer.parseInt(sourceAsMap.get("id").toString());
                String title = sourceAsMap.get("title").toString();
                String description = sourceAsMap.get("description").toString();
                String videoPath = sourceAsMap.get("videoPath").toString();
                String coverPath = sourceAsMap.get("coverPath").toString();
                String status = sourceAsMap.get("status").toString();
                String createTime = sourceAsMap.get("createTime").toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date utilDate = sdf.parse(createTime);
                int categoryId = Integer.parseInt(sourceAsMap.get("categoryId").toString());
                int userId = Integer.parseInt(sourceAsMap.get("userId").toString());
                int groupId = 0;
                if (sourceAsMap.get("groupId") != null) {
                    groupId = Integer.parseInt(sourceAsMap.get("groupId").toString());
                }

                Video video = new Video(id, title, description, videoPath, coverPath, status, utilDate, categoryId, userId, groupId);

                // 判断是否需要高亮显示
                if (highlightFieldsMap.get("title") != null) {
                    title = highlightFieldsMap.get("title").fragments()[0].toString();
                    video.setTitle(title);
                }
                if (highlightFieldsMap.get("description") != null) {
                    description = highlightFieldsMap.get("description").fragments()[0].toString();
                    video.setDescription(description);
                }
                // 添加到list
                list.add(video);
            }
            System.out.println(list);
            return list;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> exportVideo() {
        Map<String, Object> map = new HashMap<>();
        try {
            String fileName = "应学视频列表-" + System.currentTimeMillis() + ".xls";
            ExportParams exportParams = new ExportParams("应学平台视频表","videos");
            List<Video> list = videoMapper.selectAll();
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Video.class, list);
            FileOutputStream fileOutputStream = new FileOutputStream("c:\\" + fileName);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            map.put("message","视频信息下载成功");
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }
}
