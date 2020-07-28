package com.leyou.search.client;

import com.leyou.item.pojo.Category;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;
    @Test
    public void queryCategoryByIds() {
        List<Category> categries=categoryClient.queryCategoryByIds(Arrays.asList(1l,2l,3l));
        Assert.assertEquals(3,categries.size());
        for (Category category : categries) {
            System.out.println("category = " + category);
        }


    }
}