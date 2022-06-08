package cn.annna.dao;

import cn.annna.entity.User;
import cn.annna.vo.MonthCountVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    //扩展查询各个月份的男女生人数
    List<MonthCountVO> selectUserCount();
}