package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 演示接口
 */
@Controller
@Api(tags = "DemoController")
@RequestMapping("/demo")
public class DemoController {

    @ApiOperation("测试连通性接口")
    @GetMapping("/ping")
    @ResponseBody
    public CommonResult<String> ping() {
        return CommonResult.success("sumshine test superpoewes");
    }
}
