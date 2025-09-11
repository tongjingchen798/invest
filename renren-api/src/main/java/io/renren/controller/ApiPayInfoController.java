
package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.utils.Result;
import io.renren.common.utils.VerificationCodeUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dto.PayInfoDTO;
import io.renren.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import io.renren.service.PayInfoService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.ApiParam;
import io.renren.dto.PayInfoPageData;
import io.renren.common.constant.Constant;

import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import io.renren.entity.PayInfoEntity;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.ConvertUtils;
import io.renren.dao.PayInfoDao;

import javax.annotation.Resource;

/**
 * 支付信息接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api/payinfo")
@Api(tags = "支付信息接口")
public class ApiPayInfoController {

    @Autowired
    private PayInfoService payInfoService;

    @Resource
    private PayInfoDao PayInfoDao;

    @Autowired
    private VerificationCodeUtils verificationCodeUtils;

    @Login
    @PostMapping
    @ApiOperation("新增支付方式")
    public Result<Map<String, Object>> addPayInfo(@RequestBody PayInfoDTO dto, @LoginUser UserEntity user) {
        // 参数校验
        ValidatorUtils.validateEntity(dto);

        if(StringUtils.isBlank(dto.getCode())){
            throw new RenException(ErrorCode.VERIFICATION_CODE_EMPTY);
        }
        if (!verificationCodeUtils.hasBankCardCode(dto.getMobile())) {
            throw new RenException(ErrorCode.VERIFICATION_CODE_NOT_FOUND);
        }
        //短信校验
        if (!verificationCodeUtils.verifyBankCardCode(dto.getMobile(), dto.getCode())) {
            throw new RenException(ErrorCode.VERIFICATION_CODE_INCORRECT);
        }

        // 检查是否已存在相同的银行账号
        QueryWrapper<PayInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pay_no", dto.getPayNo())
                   .eq("user_id", user.getId());
        List<PayInfoEntity> existList = PayInfoDao.selectList(queryWrapper);
        if (existList != null && !existList.isEmpty()) {
            throw new RenException(ErrorCode.BANK_ACCOUNT_EXISTS);
        }

        // 转换为实体对象
        PayInfoEntity entity = ConvertUtils.sourceToTarget(dto, PayInfoEntity.class);
        
        // 设置用户ID
        entity.setUserId(user.getId());
        
        // 设置创建时间
        entity.setCreateTime(new Date());
        entity.setOperTime(new Date());
        entity.setOperCode("sys");
        
        // 设置默认状态
        entity.setState(1);
        
        // 保存支付信息
        payInfoService.savePayInfo(entity);
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("payInfoId", entity.getId());
        result.put("status", "success");
        result.put("message", "支付信息添加成功");
        
        return new Result<Map<String, Object>>().ok(result);
    }

    @Login
    @GetMapping("page")
    @ApiOperation("查询支付列表")
    public Result<PayInfoPageData<PayInfoDTO>> page(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
            @ApiParam(value = "排序方式，可选值(asc、desc)") @RequestParam(value = Constant.ORDER, required = false) String order,
            @ApiParam(value = "排序字段") @RequestParam(value = Constant.ORDER_FIELD, required = false) String orderField,
            @LoginUser UserEntity user) {
        
        // 直接使用MyBatis-Plus分页，无需构建Map
        PayInfoPageData<PayInfoDTO> pageData = payInfoService.queryPageData(user.getId(), page, limit, order, orderField);
        
        return new Result<PayInfoPageData<PayInfoDTO>>().ok(pageData);
    }

    @Login
    @DeleteMapping
    @ApiOperation("删除支付信息")
    public Result delete(@RequestBody List<Long> ids, @LoginUser UserEntity user) {
        // 参数校验
        if (ids == null || ids.isEmpty()) {
            throw new RenException(ErrorCode.PAYMENT_INFO_DELETE_ERROR);
        }
        
        // TODO: 可以添加权限校验，确保只能删除自己的支付信息
        // 这里可以根据业务需求添加额外的校验逻辑
        
        // 执行删除
        payInfoService.delete(ids);
        
        return new Result().ok("success");
    }
    
}
