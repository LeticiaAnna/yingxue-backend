package cn.annna.service;

import cn.annna.annotation.DeleteCache;
import cn.annna.dao.CategoryMapper;
import cn.annna.entity.Category;
import cn.annna.entity.CategoryExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String, Object> queryOnePage(Integer page,Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        try {
            if (page <= 0){
                throw new RuntimeException("请求页码不正确,请规范操作");
            }
            Category category = new Category();
            category.setLevels(1);
            int total = categoryMapper.selectCount(category);
            CategoryExample categoryExample = new CategoryExample();
            categoryExample.createCriteria().andLevelsEqualTo(1);
            List<Category> list = categoryMapper.selectByExampleAndRowBounds(categoryExample, new RowBounds((page - 1) * pageSize, pageSize));
            map.put("total",total);
            map.put("page",page);
            map.put("rows",list);
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String, Object> queryTwoPage(Integer page, Integer pageSize, Integer categoryId) {
        Map<String,Object> map = new HashMap<>();
        try {
            if (page <= 0){
                throw new RuntimeException("请求页码不正确,请规范操作");
            }
            Category category = new Category();
            category.setParentId(categoryId);
            int total = categoryMapper.selectCount(category);
            CategoryExample categoryExample = new CategoryExample();
            categoryExample.createCriteria().andParentIdEqualTo(categoryId);
            List<Category> list = categoryMapper.selectByExampleAndRowBounds(categoryExample, new RowBounds((page - 1) * pageSize, pageSize));
            map.put("total",total);
            map.put("page",page);
            map.put("rows",list);
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    @DeleteCache
    public Map<String, Object> add(Category category) {
        Map<String,Object> map = new HashMap<>();
        try {
            CategoryExample categoryExample = new CategoryExample();
            if (category.getParentId() == null){
                categoryExample.createCriteria().andLevelsEqualTo(1).andCateNameEqualTo(category.getCateName());
                List<Category> list = categoryMapper.selectByExample(categoryExample);
                System.out.println(" 一级类别: " + list + "，长度: " + list.size());
                if (list.size() != 0){
                    throw new RuntimeException("一级类别中已存在 " + category.getCateName() + " ,请更换一个名称");
                }
                category.setLevels(1);
                categoryMapper.insertSelective(category);
                map.put("message",category.getCateName() + " 一级类别添加成功");
            }else {
                categoryExample.createCriteria().andLevelsEqualTo(2).andCateNameEqualTo(category.getCateName());
                List<Category> list = categoryMapper.selectByExample(categoryExample);
                if (list.size() != 0){
                    throw new RuntimeException("二级类别中已存在 " + category.getCateName() + " ,请更换一个名称");
                }
                category.setLevels(2);
                category.setParentId(category.getParentId());
                categoryMapper.insertSelective(category);
                map.put("message",category.getCateName() + " 二级类别添加成功");
            }
            map.put("status",200);
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    @DeleteCache
    public Map<String, Object> delete(Category category) {
        Map<String, Object> map = new HashMap<>();
        try {
            CategoryExample categoryExample = new CategoryExample();
            Category c = categoryMapper.selectByPrimaryKey(category.getId());
            if (c == null){
                throw new RuntimeException("类别不存在,请规范操作");
            }
            if (category.getParentId() == null){
                categoryExample.createCriteria().andLevelsEqualTo(2).andParentIdEqualTo(category.getId());
                List<Category> list = categoryMapper.selectByExample(categoryExample);
                if (list.size() != 0){
                    throw new RuntimeException("当前一级类别 " + c.getCateName() + "下存在二级类别,请先删除.类别名称: " + list.get(0).getCateName());
                }
                categoryMapper.deleteByPrimaryKey(category.getId());
                map.put("message",c.getCateName() + " 一级类别删除成功");
                map.put("status",200);
            }else {
                //等做完视频模块和图文模块再添加
                categoryMapper.deleteByPrimaryKey(category.getId());
                map.put("message",c.getCateName() + " 二级类别删除成功");
                map.put("status",200);
            }
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Category> queryByLevelsCategory(Integer levels) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andLevelsEqualTo(levels);
        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public Category queryById(Integer id) {
        try {
            Category c = categoryMapper.selectByPrimaryKey(id);
            if (c == null){
                throw new RuntimeException("类别不存在,请规范操作");
            }
            return c;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @DeleteCache
    public Map<String, Object> update(Category category) {
        Map<String, Object> map = new HashMap<>();
        try {
            Category c = categoryMapper.selectByPrimaryKey(category.getId());
            if (c == null){
                throw new RuntimeException("类别不存在,请规范操作");
            }
            if (category.getParentId() == null){
                categoryMapper.updateByPrimaryKeySelective(category);
                map.put("message",category.getCateName() + " 一级类别名称修改成功");
                map.put("status",200);
            }else {
                categoryMapper.updateByPrimaryKeySelective(category);
                map.put("message",category.getCateName() + " 二级类别名称修改成功");
                map.put("status",200);
            }
            return map;
        }catch (Exception e){
            map.put("message",e.getMessage());
            map.put("status",400);
            return map;
        }
    }

}
