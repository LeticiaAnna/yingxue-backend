package cn.annna.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "yx_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Integer id;

    private String username;

    private String password;

    private String sign;

    @Column(name = "head_img")
    private String headImg;

    private String phone;

    private String wechat;

    private String status;

    @Column(name = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String sex;

    private String city;

    }