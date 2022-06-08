package cn.annna.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.annna.dao.UserMapper;
import cn.annna.elasticsearch.UserRepository;
import cn.annna.entity.User;
import cn.annna.util.OSSUtil;
import cn.annna.vo.MonthCountVO;
import cn.annna.vo.UserCountDataVO;
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

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private UserRepository userRepository;

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
            userRepository.save(user);
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
            OSSUtil.deleteFile(u.getHeadImg());
            userMapper.deleteByPrimaryKey(id);
            userRepository.deleteById(id);
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
            userRepository.save(user);
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
    @Transactional(propagation = Propagation.SUPPORTS)
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

    @Override
    public Map<String, Object> uploadHeadImg(MultipartFile headImg,String oldHeadImg) {
        System.out.println("文件类型: " + headImg.getContentType());
        Map<String, Object> map = new HashMap<>();
        try {
            if (!Objects.equals(headImg.getContentType(), "image/jpeg") && !Objects.equals(headImg.getContentType(), "image/png")){
                throw new RuntimeException("只允许上传 jpeg|png 格式的头像,请规范操作");
            }
            if (headImg.getSize() > 2097152){
                throw new RuntimeException("头像大小大于 2MB ,请调整");
            }
            if (!oldHeadImg.equals("")){
                OSSUtil.deleteFile(oldHeadImg);
                String headPath = OSSUtil.uploadFile(headImg,"yingxue/images/head/");
                map.put("message","头像更新成功");
                map.put("fileName",headPath);
                map.put("status",200);
            }else {
                String headPath = OSSUtil.uploadFile(headImg,"yingxue/images/head/");
                map.put("message","头像上传成功");
                map.put("fileName",headPath);
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
    public Map<String, Object> deleteHead(String oldHeadImg) {
        Map<String, Object> map = new HashMap<>();
        try {
            OSSUtil.deleteFile(oldHeadImg);
            map.put("message","头像删除成功");
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    public List<User> searchUser(String content) {
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.queryStringQuery(content)
                    .field("username")
                    .field("sign"));
            // 设置高亮显示
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder
                    .preTags("<font color='red'>")
                    .postTags("</font>")
                    .requireFieldMatch(false)
                    .field("*");
            sourceBuilder.highlighter(highlightBuilder);
            // 查询的请求对象
            SearchRequest searchRequest = new SearchRequest("yingxue_users").types("user").source(sourceBuilder);
            // 执行查询，获取查询到的响应对象
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 结果处理
            List<User> list = new ArrayList<>();
            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                Map<String, HighlightField> highlightFieldsMap = hit.getHighlightFields();

                // 把查到的原文档 转换为 java对象
                int id = Integer.parseInt(sourceAsMap.get("id").toString());
                String username = sourceAsMap.get("username").toString();
                String password = sourceAsMap.get("password").toString();
                String sign = sourceAsMap.get("sign").toString();
                String headImg = sourceAsMap.get("headImg").toString();
                String phone = sourceAsMap.get("phone").toString();
                String wechat = null;
                if (sourceAsMap.get("wechat") != null) {
                    wechat = sourceAsMap.get("wechat").toString();
                }
                String status = sourceAsMap.get("status").toString();

                String createTime = sourceAsMap.get("createTime").toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date utilDate = sdf.parse(createTime);

                String sex = sourceAsMap.get("sex").toString();
                String city = sourceAsMap.get("city").toString();


                User user = new User(id, username, password, sign, headImg, phone, wechat, status, utilDate, sex, city);
                // 判断是否需要高亮显示
                if (highlightFieldsMap.get("username") != null) {
                    username = highlightFieldsMap.get("username").fragments()[0].toString();
                    user.setUsername(username);
                }
                if (highlightFieldsMap.get("sign") != null) {
                    sign = highlightFieldsMap.get("sign").fragments()[0].toString();
                    user.setSign(sign);
                }
                // 添加到list
                list.add(user);
            }
            System.out.println(list);
            return list;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> exportUser() {
        Map<String, Object> map = new HashMap<>();
        try {
            String fileName = "应学用户列表-" + System.currentTimeMillis() + ".xls";
            ExportParams exportParams = new ExportParams("应学平台用户表","users");
            List<User> list = userMapper.selectAll();

            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, list);
            FileOutputStream fileOutputStream = new FileOutputStream("c:\\" + fileName);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            map.put("message","用户信息下载成功");
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    public UserCountDataVO userCount() {
        UserCountDataVO userCountDataVO = new UserCountDataVO();
        try {
            List<MonthCountVO> monthCountVO =  userMapper.selectUserCount();
            for (MonthCountVO m : monthCountVO) {
                userCountDataVO.getMonths().add(m.getMonth());
                userCountDataVO.getBoyCount().add(m.getBoyCount());
                userCountDataVO.getGirlCount().add(m.getGirlCount());
            }
            userCountDataVO.setMessage("信息获取成功");
            userCountDataVO.setStatus(200);
            return userCountDataVO;
        }catch (Exception e){
            userCountDataVO.setMessage(e.getMessage());
            userCountDataVO.setStatus(400);
            return userCountDataVO;
        }
    }
}
