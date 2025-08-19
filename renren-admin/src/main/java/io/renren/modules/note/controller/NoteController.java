package io.renren.modules.note.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.note.dao.NoteDao;
import io.renren.modules.note.dto.NoteDTO;
import io.renren.modules.note.entity.NoteEntity;
import io.renren.modules.note.service.NoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 公告表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@RestController
@RequestMapping("note")
@Api(tags="公告管理")
public class NoteController {
    @Autowired
    private NoteDao noteDao;

    @Autowired
    private NoteService noteService;

    @GetMapping("list")
//    @RequiresPermissions("sys:note:page")
    public Result<List<NoteEntity>> page(){
        QueryWrapper<NoteEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        List<NoteEntity> list=noteDao.selectList(queryWrapper);
        return new Result().ok(list);
    }


    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("sys:note:save")
    public Result save(@RequestBody NoteDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        noteService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("sys:note:update")
    public Result update(@RequestBody NoteDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        noteService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("sys:note:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        noteService.delete(ids);

        return new Result();
    }


}