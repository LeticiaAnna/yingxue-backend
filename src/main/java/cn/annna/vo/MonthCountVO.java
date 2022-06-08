package cn.annna.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthCountVO {
    private String month; //月份
    private Integer boyCount; //男生人数
    private Integer girlCount; //女生人数
}
