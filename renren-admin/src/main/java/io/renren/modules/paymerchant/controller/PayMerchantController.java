package io.renren.modules.paymerchant.controller;

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
import io.renren.modules.paymerchant.dto.PayMerchantDTO;
import io.renren.modules.paymerchant.entity.PayMerchantEntity;
import io.renren.modules.paymerchant.service.PayMerchantService;
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
 * 支付商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@RestController
@RequestMapping("/paymerchant")
@Api(tags="支付商户配置表")
public class PayMerchantController {
    @Autowired
    private PayMerchantService payMerchantService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("sys:paymerchant:page")
    public Result<PageData<PayMerchantDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<PayMerchantDTO> page = payMerchantService.page(params);

        return new Result<PageData<PayMerchantDTO>>().ok(page);
    }
//
//    @GetMapping("{id}")
//    @ApiOperation("信息")
////    @RequiresPermissions("sys:paymerchant:info")
//    public Result<PayMerchantDTO> get(@PathVariable("id") Long id){
//        PayMerchantDTO data = payMerchantService.get(id);
//
//        return new Result<PayMerchantDTO>().ok(data);
//    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("sys:paymerchant:save")
    public Result save(@RequestBody PayMerchantDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        payMerchantService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("sys:paymerchant:update")
    public Result update(@RequestBody PayMerchantEntity dto){
        //效验数据
//        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        payMerchantService.updateById(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("sys:paymerchant:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        payMerchantService.delete(ids);

        return new Result();
    }

    @PutMapping("updatedegreeheat")
    @ApiOperation("商户优先级修改")
    @LogOperation("商户优先级修改")
//    @RequiresPermissions("sys:paymerchant:updatedegreeheat")
    public Result updateDegreeHeat(@RequestBody PayMerchantDTO dto){
        try {
            if (dto.getDegreeheat() == null) {
                return new Result().error("优先级不能为空");
            }
            // 更新优先级
            if (payMerchantService.updateDegreeHeat(dto)) {
                return new Result().ok("success");
            } else {
                return new Result().error("商户优先级修改失败");
            }
        } catch (Exception e) {
            return new Result().error("商户优先级修改失败：" + e.getMessage());
        }
    }


}