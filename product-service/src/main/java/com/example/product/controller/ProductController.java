package com.example.product.controller;

import com.example.common.annotation.RequiresRoles;
import com.example.common.model.Result;
import com.example.product.entity.Product;
import com.example.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "产品管理")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @Operation(summary = "浏览产品列表")
    @GetMapping
    @RequiresRoles("USER")
    public Result<List<Product>> list() {
        return Result.success(productService.list());
    }

    @Operation(summary = "添加产品")
    @PostMapping
    @RequiresRoles("EDITOR")
    public Result<String> add(@RequestBody Product product) {
        productService.save(product);
        return Result.success("添加成功");
    }

    @Operation(summary = "修改产品")
    @PutMapping
    @RequiresRoles("EDITOR")
    public Result<String> update(@RequestBody Product product) {
        productService.updateById(product);
        return Result.success("修改成功");
    }

    @Operation(summary = "删除产品")
    @DeleteMapping("/{id}")
    @RequiresRoles("EDITOR")
    public Result<String> delete(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success("删除成功");
    }
}
