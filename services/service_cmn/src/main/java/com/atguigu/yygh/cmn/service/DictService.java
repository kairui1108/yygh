package com.atguigu.yygh.cmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/6 9:49
 */

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    void exportDict(HttpServletResponse response);

    void importData(MultipartFile file);

    String getDictName(String dictCode, String value);

    List<Dict> findByDictCode(String dictCode);
}
