package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/9 9:37
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {

    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
