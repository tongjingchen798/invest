package io.renren.modules.sms.controller;

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
import io.renren.modules.sms.dto.SmsMerchantConfigDTO;
import io.renren.modules.sms.service.SmsMerchantConfigService;
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
 * 短信商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@RestController
@RequestMapping("captchaManager")
@Api(tags="短信商户配置表")
public class SmsMerchantConfigController {
    @Autowired
    private SmsMerchantConfigService smsMerchantConfigService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "captchaName", value = "商户名称", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("sys:smsmerchantconfig:page")
    public Result<PageData<SmsMerchantConfigDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<SmsMerchantConfigDTO> page = smsMerchantConfigService.page(params);

        return new Result<PageData<SmsMerchantConfigDTO>>().ok(page);
    }

//    @GetMapping("{id}")
//    @ApiOperation("信息")
////    @RequiresPermissions("sys:smsmerchantconfig:info")
//    public Result<SmsMerchantConfigDTO> get(@PathVariable("id") Long id){
//        SmsMerchantConfigDTO data = smsMerchantConfigService.get(id);
//
//        return new Result<SmsMerchantConfigDTO>().ok(data);
//    }

//    @PostMapping
//    @ApiOperation("保存")
//    @LogOperation("保存")
////    @RequiresPermissions("sys:smsmerchantconfig:save")
//    public Result save(@RequestBody SmsMerchantConfigDTO dto){
//        //效验数据
//        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
//
//        smsMerchantConfigService.save(dto);
//
//        return new Result();
//    }

//    @PutMapping
//    @ApiOperation("修改")
//    @LogOperation("修改")
////    @RequiresPermissions("sys:smsmerchantconfig:update")
//    public Result update(@RequestBody SmsMerchantConfigDTO dto){
//        //效验数据
//        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
//
//        smsMerchantConfigService.update(dto);
//
//        return new Result();
//    }

//    @DeleteMapping
//    @ApiOperation("删除")
//    @LogOperation("删除")
////    @RequiresPermissions("sys:smsmerchantconfig:delete")
//    public Result delete(@RequestBody Long[] ids){
//        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id");
//
//        smsMerchantConfigService.delete(ids);
//
//        return new Result();
//    }


}