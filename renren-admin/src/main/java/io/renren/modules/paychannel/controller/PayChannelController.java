package io.renren.modules.paychannel.controller;

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
import io.renren.modules.paychannel.dto.PayChannelDTO;
import io.renren.modules.paychannel.service.PayChannelService;
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
 * 支付渠道表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@RestController
@RequestMapping("paychannel")
@Api(tags="支付渠道表")
public class PayChannelController {
    @Autowired
    private PayChannelService payChannelService;

    @GetMapping("page")
    @ApiOperation("支付渠道分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = "chargeorwithdraw", value = "类型(1充值配置2提现配置)", paramType = "query", dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("sys:paychannel:page")
    public Result<PageData<PayChannelDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){

        PageData<PayChannelDTO> page = payChannelService.page(params);

        return new Result<PageData<PayChannelDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @RequiresPermissions("sys:paychannel:info")
    public Result<PayChannelDTO> get(@PathVariable("id") Long id){
        PayChannelDTO data = payChannelService.get(id);

        return new Result<PayChannelDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("sys:paychannel:save")
    public Result save(@RequestBody PayChannelDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        payChannelService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("sys:paychannel:update")
    public Result update(@RequestBody PayChannelDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        payChannelService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("sys:paychannel:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        payChannelService.delete(ids);

        return new Result();
    }


}