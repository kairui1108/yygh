package com.atguigu;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/6 11:05
 */
@Data
public class UserData {

    @ExcelProperty("用户编号")
    private int id;

    @ExcelProperty("用户名称")
    private String username;
}
