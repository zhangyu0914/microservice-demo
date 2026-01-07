package com.example.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("product")
@Schema(description = "产品信息")
public class Product {
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键")
    private Long id;
    
    @Schema(description = "产品名")
    private String name;
}
