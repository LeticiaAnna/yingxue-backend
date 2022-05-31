package cn.annna;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.annna.dao")
public class MasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MasterApplication.class, args);
    }

}
