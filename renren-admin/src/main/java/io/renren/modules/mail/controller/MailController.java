package io.renren.modules.mail.controller;

import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.modules.mail.dto.MailDTO;
import io.renren.modules.mail.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 站内信
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@RestController
@RequestMapping("/mail")
@Api(tags="站内信")
public class MailController {
    @Autowired
    private MailService mailService;

    @GetMapping("list")
    @ApiOperation("站内信列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int"),
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType="int"),
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "theme", value = "标题（可选，支持模糊查询）", paramType = "query", dataType="String",required = false),
        @ApiImplicitParam(name = "fromUser", value = "发件人（可选，支持模糊查询）", paramType = "query", dataType="String",required = false),
        @ApiImplicitParam(name = "startTime", value = "开始日期:时间戳（可选）", paramType = "query", dataType="long"),
        @ApiImplicitParam(name = "endTime", value = "结束日期:时间戳（可选）", paramType = "query", dataType="long")
    })
    public Result<PageData<MailDTO>> list(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<MailDTO> page = mailService.page(params);
        return new Result<PageData<MailDTO>>().ok(page);
    }
}
