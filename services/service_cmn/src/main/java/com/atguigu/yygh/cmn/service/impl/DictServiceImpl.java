package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/6 9:50
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> list = baseMapper.selectList(wrapper);
        return list.stream().map(dict -> {
            dict.setHasChildren(hashChild(dict.getId()));
            return dict;
        }).collect(Collectors.toList());
    }

    @Override
    public void exportDict(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<Dict> list = baseMapper.selectList(null);

        List<DictEeVo> dictEeVoList = list.stream().map(dict -> {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            return dictEeVo;
        }).collect(Collectors.toList());

        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictEeVoList);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        if (StringUtils.isEmpty(dictCode)) {
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            return baseMapper.selectOne(wrapper).getName();
        }
        Dict dict = this.getDictByDictCode(dictCode);
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", dict.getId());
        wrapper.eq("value", value);
        return baseMapper.selectOne(wrapper).getName();
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict dict = getDictByDictCode(dictCode);
        if (dict == null){
            return null;
        }
        List<Dict> list = findChildData(dict.getId());
        return list;

    }

    private Dict getDictByDictCode(String code) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", code);
        return baseMapper.selectOne(wrapper);
    }

    private boolean hashChild(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return  count > 0;
    }
}
