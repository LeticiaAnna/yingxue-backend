package cn.annna.util;

import org.springframework.util.DigestUtils;

import java.util.Random;

public class MD5Util {
    public static String getSalt(){
        return getSalt(20);
    }

    //1. 获取盐 (某个长度的随机字符串)
    public static String getSalt(int size){
        char[] pool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0 ; i < size ; i++){
            stringBuffer.append(pool[random.nextInt(pool.length)]);
        }
        return stringBuffer.toString();
    }
    //2. 使用盐和原来的密码,返回MD5加盐加密后的密码
    public static String getPassword(String password,String salt){
        password = password + salt;
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
