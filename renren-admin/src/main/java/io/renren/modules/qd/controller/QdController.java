package io.renren.modules.qd.controller;

import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.modules.qd.dto.QdRecordDTO;
import io.renren.modules.qd.service.QdService;
import io.renren.modules.signconfig.dto.SignRewardConfigDTO;
import io.renren.modules.signconfig.service.SignRewardConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 用户签到记录管理
 *
 * @author renren
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/qd")
@Api(tags = "用户签到记录管理")
public class QdController {
    @Autowired
    private QdService qdService;


    @Autowired
    private SignRewardConfigService signRewardConfigService;

    @GetMapping("")
    @ApiOperation("获取签到配置")
    public Result<SignRewardConfigDTO> getQdConfig() {
        try {
            SignRewardConfigDTO config = signRewardConfigService.get(1L);
            if (config != null) {
                return new Result<SignRewardConfigDTO>().ok(config);
            } else {
                return new Result<SignRewardConfigDTO>().error("签到配置不存在");
            }
        } catch (Exception e) {
            return new Result<SignRewardConfigDTO>().error("获取签到配置失败: " + e.getMessage());
        }
    }

    @GetMapping("list")
    @ApiOperation("查询用户签到记录")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页码，从1开始", required = true, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "每页显示记录数", required = true, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "orderField", value = "排序字段", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "order", value = "排序方式，可选值(asc、desc)", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "startTime", value = "开始日期:时间戳", required = false, dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "endTime", value = "结束日期:时间戳", required = false, dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "mobile", value = "用户账号", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "username", value = "用户姓名", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "agent", value = "代理下拉框", required = false, dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "salesmanid", value = "业务员下拉框", required = false, dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "biaoqian", value = "标签筛选：传标签", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "biaoqianFlag", value = "标签筛选：1有 0无 查全部，不传参", required = false, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "liebian", value = "裂变筛选：是=1，否=0，查全部，不传参", required = false, dataType = "int", paramType = "query")
    })
    public Result<PageData<QdRecordDTO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {
        try {
            // 参数验证
            if (params.get("page") == null || params.get("limit") == null) {
                return new Result<PageData<QdRecordDTO>>().error("页码和每页记录数不能为空");
            }
            
            PageData<QdRecordDTO> pageData = qdService.getQdRecordList(params);
            return new Result<PageData<QdRecordDTO>>().ok(pageData);
        } catch (Exception e) {
            return new Result<PageData<QdRecordDTO>>().error("查询签到记录失败: " + e.getMessage());
        }
    }
}
