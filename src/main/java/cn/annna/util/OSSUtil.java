package cn.annna.util;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class OSSUtil {
    private static final String endpoint = "https://oss-cn-hongkong.aliyuncs.com";
    private static final String accessKeyId = "lQQhuNkGenZIZOGO";
    private static final String accessKeySecret = "rObkKPtUW2ROaPwKZB2qG38PZspp8u";
    private static final String bucketName = "cat-hk";
    private static final String domain = "https://cat-hk.oss-cn-hongkong.aliyuncs.com/";

    public static String uploadFile(MultipartFile file,String objectPath){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            String objectName = objectPath + new Date().getTime() + "-" + file.getOriginalFilename();
            System.out.println("objectName: " + objectName);
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(file.getBytes()));
            return domain + objectName;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public static void deleteFile(String objectName){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            if (objectName.equals("")){
                throw new RuntimeException("文件路径不允许为空");
            }
            objectName = objectName.replace("https://cat-hk.oss-cn-hongkong.aliyuncs.com/","");
            System.out.println("objectName: " + objectName);
            ossClient.deleteObject(bucketName, objectName);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public static String frameAndUpdate(String objectName){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            if (objectName.equals("")){
                throw new RuntimeException("文件路径不允许为空");
            }
            objectName = objectName.replace("https://cat-hk.oss-cn-hongkong.aliyuncs.com/","");
            //System.out.println(objectName);
            String style = "video/snapshot,t_5000,f_jpg,w_800,h_600";
            Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
            req.setExpiration(expiration);
            req.setProcess(style);
            URL signedUrl = ossClient.generatePresignedUrl(req);
            System.out.println("signedUrl: " + signedUrl);
            objectName = objectName.replace("yingxue/videos/video/","yingxue/videos/cover/");
            objectName = objectName.replace(".mp4",".jpg");
            System.out.println(objectName);
            InputStream inputStream = signedUrl.openStream();
            ossClient.putObject(bucketName,objectName,inputStream);
            return domain + objectName;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


}
