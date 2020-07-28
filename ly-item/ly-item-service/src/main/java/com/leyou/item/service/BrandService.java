package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            //过滤条件
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());

        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " desc" : " asc");
            example.setOrderByClause(orderByClause);

        }
        //查询
        List<Brand> list = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //解析分页结果
        PageInfo<Brand> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);


    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if (count != 1)
            throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        //新增中间表
        for (Long cid : cids) {
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (count != 1)
                throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
    }

    @Transactional
    public void editBrand(Brand brand, List<Long> cids) {
        //编辑品牌
        System.out.println(brand.getId());
        int count = brandMapper.updateByPrimaryKey(brand);
        if (count != 1)
            throw new LyException(ExceptionEnum.BRAND_EDIT_ERROR);
        //查出数据库中的中间表集合
        List<Long> former = brandMapper.queryCategoryBrand(brand.getId());
        //编辑中间表
        for (Long cid : cids) {
            if (!former.contains(cid)) {
                if (cids.size() > former.size()) {

                    count = brandMapper.insertCategoryBrand(cid, brand.getId());
                    if (count != 1)
                        throw new LyException(ExceptionEnum.BRAND_EDIT_ERROR);


                } else if (cids.size() < former.size()) {
                    count = brandMapper.deleteCategoryBrandByCategoryIdAndBrandId(cid, brand.getId());
                    if (count != 1)
                        throw new LyException(ExceptionEnum.BRAND_EDIT_ERROR);
                }


            }
        }


    }

    @Transactional
    public void removeBrand(Long bid) {
        int count = brandMapper.deleteByPrimaryKey(new Brand(bid, null, null, null));
        if (count != 1)
            throw new LyException(ExceptionEnum.REMOVE_BRAND_ERROR);
        count = brandMapper.deleteCategoryBrandByBrandId(bid);
        if (count < 1)
            throw new LyException(ExceptionEnum.REMOVE_BRAND_ERROR);


    }

    public Brand queryById(Long bid){
        Brand brand= brandMapper.selectByPrimaryKey(bid);
        if(brand==null)
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        return brand;
    }

    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> list = brandMapper.queryBrandByCid(cid);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }

    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }
}
