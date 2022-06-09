package cn.annna.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Set;

@Aspect
@Component
public class CacheAspect{
    @Autowired
    private RedisTemplate redisTemplate;

    //添加缓存数据
    @Around("execution(* cn.annna.service.CategoryServiceImpl.query*(..))")
    public Object addCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("缓存处理");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        ValueOperations valueOperations = redisTemplate.opsForValue();
        StringBuilder stringBuilder = new StringBuilder();
        String classname = proceedingJoinPoint.getTarget().getClass().getName();
        stringBuilder.append(classname);
        stringBuilder.append("-");
        String methodName = proceedingJoinPoint.getSignature().getName();
        stringBuilder.append(methodName);
        stringBuilder.append("-");
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object a : args) {
            stringBuilder.append(a);
            stringBuilder.append("-");
        }
        String key = stringBuilder.toString();
        System.out.println(key);
        Boolean bn = redisTemplate.hasKey(key);
        Object result;
        if (bn){
            System.out.println("缓存已存在直接调用");
            result = valueOperations.get(key);
        }else {
            System.out.println("缓存不存在写入缓存");
            result = proceedingJoinPoint.proceed();
            valueOperations.set(key,result);
        }
        return result;
    }

    //删除缓存数据
    //@After("execution(* cn.annna.service.CategoryServiceImpl.add(..))")
    @After("@annotation(cn.annna.annotation.DeleteCache)")
    public void deleteCache(JoinPoint joinPoint){
        System.out.println("删除缓存");
        String classname = joinPoint.getTarget().getClass().getName();
        Set list = redisTemplate.keys("*");
        for (Object key : list) {
            if (key.toString().startsWith(classname)){
                redisTemplate.delete(key);
            }
        }

    }

}
