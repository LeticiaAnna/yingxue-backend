package cn.annna.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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

    public static void deleteFile(String objectName) throws Exception {
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


}
