package cn.annna.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "yx_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private Integer id;

    @Column(name = "cate_name")
    private String cateName;

    private Integer levels;

    @Column(name = "parent_id")
    private Integer parentId;

}