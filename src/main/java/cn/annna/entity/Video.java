package cn.annna.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
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

@Table(name = "yx_video")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "yingxue_videos",type = "video")
public class Video implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(name = "id")
    private Integer id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    @Excel(name = "标题")
    private String title;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    @Excel(name = "描述")
    private String description;

    @Field(type = FieldType.Keyword)
    @Column(name = "video_path")
    @Excel(name = "视频地址")
    private String videoPath;

    @Field(type = FieldType.Keyword)
    @Column(name = "cover_path")
    @Excel(name = "视频封面", type = 2 ,width = 20 , height = 20)
    private String coverPath;

    @Field(type = FieldType.Keyword)
    @Excel(name = "状态",replace = {"冻结_0","正常_1"})
    private String status;

    @Column(name = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, name = "create_time",format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd'T'HH:mm:ss'+08:00' || strict_date_optional_time || epoch_millis")
    @Excel(name = "创建时间",format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "category_id")
    @Excel(name = "分类ID")
    private Integer categoryId;

    @Column(name = "user_id")
    @Excel(name = "用户ID")
    private Integer userId;

    @Column(name = "group_id")
    @Excel(name = "分组ID")
    private Integer groupId;

}