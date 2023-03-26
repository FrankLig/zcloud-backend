package com.bom.zcloudbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel("树节点VO")
public class TreeNodeVO {

    @ApiModelProperty("节点Id")
    private Long id;

    @ApiModelProperty("节点名称")
    private String label;

    @ApiModelProperty("节点深度")
    private Long depth;

    @ApiModelProperty("是否关闭")
    private String state = "closed";

    @ApiModelProperty("属性集合")
    private Map<String, String> attributes = new HashMap<>();

    @ApiModelProperty("子节点列表")
    private List<TreeNodeVO> child=new ArrayList<>();

}
