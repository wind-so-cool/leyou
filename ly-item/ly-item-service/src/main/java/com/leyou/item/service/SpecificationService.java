package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;


    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup group=new SpecGroup();
        group.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(group);
        if(CollectionUtils.isEmpty(list)){
            //没查到
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }

        return list;
    }
    @Transactional
    public void addGroup(SpecGroup group) {
        int count=specGroupMapper.insert(group);
        if (count != 1)
            throw new LyException(ExceptionEnum.ADD_GROUP_ERROR);

    }
    @Transactional
    public void editGroup(SpecGroup group) {
        int count=specGroupMapper.updateByPrimaryKey(group);
        if(count!=1)
            throw new LyException(ExceptionEnum.EDIT_GROUP_ERROR);



    }
    @Transactional
    public void deleteGroup(Long id) {
        SpecGroup group=new SpecGroup();
        group.setId(id);
        int count=specGroupMapper.deleteByPrimaryKey(group);
        if(count!=1)
            throw new LyException(ExceptionEnum.DELETE_GROUP_ERROR);
    }

    public List<SpecParam> queryParamList(Long gid,Long cid,Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);
        List<SpecParam> list=specParamMapper.select(param);
        if(CollectionUtils.isEmpty(list)){
            //没查到
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;


    }
    @Transactional
    public void addParam(SpecParam param) {
        int count=specParamMapper.insert(param);
        if(count!=1)
            throw new LyException(ExceptionEnum.ADD_PARAM_ERROR);

    }
    @Transactional
    public void editParam(SpecParam param) {
        int count=specParamMapper.updateByPrimaryKey(param);
        if(count!=1)
            throw new LyException(ExceptionEnum.EDIT_PARAM_ERROR);

    }
    @Transactional
    public void deleteParam(Long id) {
        int count=specParamMapper.deleteByPrimaryKey(id);
        if(count!=1)
            throw new LyException(ExceptionEnum.DELETE_PARAM_ERROR);


    }

    public List<SpecGroup> queryListByCid(Long cid) {
        //查询规格组
        List<SpecGroup> specGroups=queryGroupByCid(cid);
        //查询当前分类下的参数
        List<SpecParam> specParams=queryParamList(null,cid,null);
        //先把规格参数变成Map，Map的key是规格组id，map的值是组下的所有参数
        Map<Long,List<SpecParam>> map=new HashMap<>();
        for (SpecParam param : specParams) {
            if(!map.containsKey(param.getGroupId()))
                map.put(param.getGroupId(),new ArrayList<SpecParam>());
            map.get(param.getGroupId()).add(param);

        }
        //填充param到group
        for (SpecGroup group : specGroups) {
            group.setParams(map.get(group.getId()));
        }
        return specGroups;


    }
}
