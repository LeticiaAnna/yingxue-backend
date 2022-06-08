package cn.annna.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//后台给前台传递的信息
public class UserCountDataVO {
    private List<String> months = new ArrayList<>();
    private List<Integer> boyCount = new ArrayList<>();
    private List<Integer> girlCount = new ArrayList<>();
    private String message;
    private Integer status;
}
