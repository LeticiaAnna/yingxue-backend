package cn.annna.dao;

import cn.annna.entity.Admin;

import java.util.List;

public interface AdminDao {

    public void insert(Admin admin);

    public void delete(Integer id);

    public void update(Admin admin);

    public Admin selectById(Integer id);

    public Admin selectByUsername(String username);

    public List<Admin> selectAll();

}
