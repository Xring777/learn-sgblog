package com.liuzihan.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CategoryVo {
    private Long id;
    private String name;
    private String value;
    private Long pid;
    //描述
    private String description;
    //状态0:正常,1禁用
    private String status;
    private List<CategoryVo> children;
}
