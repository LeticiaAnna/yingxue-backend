package cn.annna.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "yx_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "yingxue_users",type = "user")
public class User implements Serializable {

    @Id
    //用在主键上,代表执行 insert 操作后,自动把生成的主键值存到 实体类的该属性上
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(name = "id")
    private Integer id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    @Excel(name = "用户名")
    private String username;

    @Field(type = FieldType.Keyword)
    @ExcelIgnore
    private String password;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    @Excel(name = "签名")
    private String sign;

    @Column(name = "head_img")
    @Excel(name = "头像", type = 2 ,width = 20 , height = 20)

    private String headImg;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    @Excel(name = "手机号")
    private String phone;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    @Excel(name = "微信号")
    private String wechat;

    @Field(type = FieldType.Keyword)
    @Excel(name = "状态",replace = {"冻结_0","正常_1"})
    private String status;

    @Column(name = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, name = "create_time",format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd'T'HH:mm:ss'+08:00' || strict_date_optional_time || epoch_millis")
    @Excel(name = "创建时间",format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Field(type = FieldType.Keyword)
    @Excel(name = "性别")
    private String sex;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    @Excel(name = "城市")
    private String city;

    }