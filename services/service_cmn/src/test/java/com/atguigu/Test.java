package com.atguigu;

import com.alibaba.excel.EasyExcel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/6 11:05
 */

public class Test {


    public static void main(String[] args) {
        List<UserData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserData userData = new UserData();
            userData.setId(i);
            userData.setUsername("test" + i);
            list.add(userData);
        }
        String fileName = "classpath:01.xlsx";

        EasyExcel.write(fileName, UserData.class).sheet("用户信息")
                .doWrite(list);
    }
}
