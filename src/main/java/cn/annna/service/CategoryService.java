package cn.annna.service;

import cn.annna.entity.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    public Map<String,Object> queryOnePage(Integer page,Integer pageSize);

    public Map<String,Object> queryTwoPage(Integer page,Integer pageSize,Integer categoryId);

    public Map<String,Object> add(Category category);

    public Map<String,Object> delete(Category category);

    public List<Category> queryByLevelsCategory(Integer levels);

    public Category queryById(Integer id);

    public Map<String,Object> update(Category category);

}
