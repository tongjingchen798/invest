package io.renren.modules.member.controller;

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
import io.renren.modules.member.dto.UserLogDTO;
import io.renren.modules.member.service.UserLogService;
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
 * 用户登录日志表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@RestController
@RequestMapping("logininfo")
@Api(tags="用户登录日志")
public class UserLogController {
    @Autowired
    private UserLogService userLogService;

    @GetMapping("list")
    @ApiOperation("登录日志分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "flag", value = "是否相同IP 1:否(不同IP) 2:是(相同IP)", paramType = "query", dataType="int"),
        @ApiImplicitParam(name = "loginIp", value = "登录IP地址", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "logout", value = "登出状态 0:未登出 1:已登出", paramType = "query", dataType="int"),
        @ApiImplicitParam(name = "biaoqian", value = "标签", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "biaoqianFlag", value = "标签状态 0:无标签 1:有标签", paramType = "query", dataType="int"),
        @ApiImplicitParam(name = "startTime", value = "开始时间(时间戳)", paramType = "query", dataType="long"),
        @ApiImplicitParam(name = "endTime", value = "结束时间(时间戳)", paramType = "query", dataType="long")
    })
//    @RequiresPermissions("sys:userlog:page")
    public Result<PageData<UserLogDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        
        PageData<UserLogDTO> page = userLogService.customPage(params);

        return new Result<PageData<UserLogDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @RequiresPermissions("sys:userlog:info")
    public Result<UserLogDTO> get(@PathVariable("id") Long id){
        UserLogDTO data = userLogService.get(id);

        return new Result<UserLogDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("sys:userlog:save")
    public Result save(@RequestBody UserLogDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        userLogService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("sys:userlog:update")
    public Result update(@RequestBody UserLogDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        userLogService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("sys:userlog:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        userLogService.delete(ids);

        return new Result();
    }


}