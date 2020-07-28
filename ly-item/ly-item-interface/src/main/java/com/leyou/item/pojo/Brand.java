package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "tb_brand")
@NoArgsConstructor
@AllArgsConstructor
/**
 * @author:li
 *
 */
public class Brand implements Serializable {
    @Id
    @KeySql(useGeneratedKeys=true)
    private Long id;
    /**
     * 品牌名称keys
     */
    private String name;
    /**
     * 品牌图片
     */
    private String image;
    private Character letter;
    
    //省略get和set
}
