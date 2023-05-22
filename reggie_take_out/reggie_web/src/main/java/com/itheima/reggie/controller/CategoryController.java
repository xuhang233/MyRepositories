package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增菜品
     *
     * @param category
     * @return
     */
    @PostMapping
    public R save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加成功");
    }


    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<Page<Category>> page(Integer page, Integer pageSize) {
        Page<Category> page1 = new Page(page, pageSize);
        categoryService.lambdaQuery()
                //排序字段升序
                .orderByAsc(Category::getSort)
                //根据修改时间降序
                .orderByDesc(Category::getUpdateTime)
                //分页查询
                .page(page1);
        return R.success(page1);
    }


    /**
     * 修改信息
     *
     * @param category
     * @return
     */
    @PutMapping
    public R update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功");
    }


    /**
     * //删除菜品
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R delete(Long id) {
        categoryService.deleteByid(id);
        return R.success("删除成功");
    }


    /**
     * //新建菜品 查询列表
     *
     * @param type
     * @return
     */
    @GetMapping("list")
    public R<List<Category>> list(Integer type) {
/*        List<Category> list = categoryService.query()
                .eq(type != null, "type", type)
                //根据修改排序为降序
                .orderByDesc("sort")
                //返回集合
                .list();*/
        List<Category> list = categoryService.lambdaQuery()
                .eq(type != null, Category::getType, type)
                .orderByAsc(Category::getSort)
                .list();
        return R.success(list);
    }


}
