package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {
    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},0)")
    int insertCategoryBrand(Long cid);

    /**
     * 根据品牌id查询商品分类
     * @param bid
     * @return
     */
    @Select("select * from tb_category where id in (select category_id from tb_category_brand where brand_id=#{bid})")
    List<Category> queryByBrandId(Long bid);
    @Delete("delete from tb_category_brand where category_id=#{cid}")
    int deleteCategoyBrand(Long cid);
}
