package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/9 9:38
 */
public interface DepartmentService {
    void saveDepartment(Map<String, Object> map);

    Page<Department> page(Integer page, Integer limit, DepartmentQueryVo queryVo);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode, String depcode);
}
