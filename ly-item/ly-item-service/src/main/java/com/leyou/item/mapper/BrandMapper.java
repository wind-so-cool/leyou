package com.leyou.item.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BrandMapper extends BaseMapper<Brand> {

    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid,@Param("bid") Long bid);
    @Update("update tb_category_brand set category_id=#{cid} where brand_id=#{bid}")
    int updateCategoryBrand(@Param("cid") Long cid,@Param("bid")Long bid);

    @Select("select category_id from tb_category_brand where brand_id=#{bid}")
    List<Long> queryCategoryBrand(Long bid);
    @Delete("delete from tb_category_brand where category_id=#{cid} and brand_id=#{bid}")
    int deleteCategoryBrandByCategoryIdAndBrandId(@Param("cid")Long cid, @Param("bid") Long bid);

    @Delete("delete from tb_category_brand where brand_id=#{bid}")
    int deleteCategoryBrandByBrandId(Long bid);
    @Select("select b.* from tb_brand b inner join tb_category_brand cb on b.id=cb.brand_id where cb.category_id=#{cid}")
    List<Brand> queryBrandByCid(Long cid);
}
