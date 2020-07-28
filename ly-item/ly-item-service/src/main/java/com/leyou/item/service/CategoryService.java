package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(Long pid) {
        Category t=new Category();
        t.setParentId(pid);
        List<Category> list = categoryMapper.select(t);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }

        return list;
    }
    @Transactional
    public void saveCategory(Category category) {

       categoryMapper.insert(category);
       int count=categoryMapper.insertCategoryBrand(category.getId());
       if(count!=1){
           throw new LyException(ExceptionEnum.CATEGORY_SAVE_ERROR);

       }


    }
    @Transactional
    public void EditCategory(Category category) {


        int count=categoryMapper.updateByPrimaryKeySelective(category);
        if(count!=1)
            throw new LyException(ExceptionEnum.EDIT_CATEGORY_ERROR);

    }
    @Transactional
    public void removeCategory(Long id) {
        Category category=new Category();
        category.setId(id);
        int count=categoryMapper.deleteByPrimaryKey(category);
        if(count!=1)
            throw new LyException(ExceptionEnum.REMOVE_CATEGORY_ERROR);
        count=categoryMapper.deleteCategoyBrand(id);
        if(count<1)
            throw new LyException(ExceptionEnum.REMOVE_CATEGORY_ERROR);
    }

    public List<Category> queryByBrandId(Long bid) {
        return categoryMapper.queryByBrandId(bid);
    }

    public List<Category> queryByIds(List<Long> ids){
        List<Category> list=categoryMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;

    }

    public List<Category> queryAllByCid3(Long cid3) {
        Category c3 = categoryMapper.selectByPrimaryKey(cid3);
        if(c3==null){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        Category c2 = categoryMapper.selectByPrimaryKey(c3.getParentId());
        if(c2==null){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        Category c1=categoryMapper.selectByPrimaryKey(c2.getParentId());
        if(c1==null){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return Arrays.asList(c1,c2,c3);


    }
}
