package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/9 9:38
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void saveDepartment(Map<String, Object> map) {
        String jsonString = JSONObject.toJSONString(map);
        Department department = JSONObject.parseObject(jsonString, Department.class);

        Department departmentExist =
                departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());

        department.setIsDeleted(0);
        department.setUpdateTime(new Date());
        department.setCreateTime(new Date());

        if (departmentExist != null) {
            department.setCreateTime(departmentExist.getCreateTime());
            department.setIsDeleted(departmentExist.getIsDeleted());
        }
        departmentRepository.save(department);
    }

    @Override
    public Page<Department> page(Integer page, Integer limit, DepartmentQueryVo queryVo) {

        Department department = new Department();
        BeanUtils.copyProperties(queryVo, department);

        Pageable pageable = PageRequest.of(page, limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<Department> example = Example.of(department, matcher);
        return departmentRepository.findAll(example, pageable);
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department == null) {
            return;
        }
        departmentRepository.deleteById(department.getId());
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        ArrayList<DepartmentVo> result = new ArrayList<>();
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example<Department> example = Example.of(departmentQuery);
        List<Department> departmentList = departmentRepository.findAll(example);

        Map<String, List<Department>> collection = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));

        for (Map.Entry<String, List<Department>> entry: collection.entrySet()) {
            String key = entry.getKey();
            List<Department> list = entry.getValue();
            DepartmentVo vo = new DepartmentVo();
            vo.setDepcode(key);
            vo.setDepname(list.get(0).getBigname());

            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : list) {
                DepartmentVo departmentVo = new DepartmentVo();
                departmentVo.setDepcode(department.getDepcode());
                departmentVo.setDepname(department.getDepname());
                children.add(departmentVo);
            }
            vo.setChildren(children);
            result.add(vo);
        }
        return result;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            return department.getDepname();
        }
        return null;
    }
}
