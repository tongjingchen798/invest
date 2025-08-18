// package io.renren.controller;
//
// import io.renren.common.exception.RenException;
// import io.renren.common.utils.Result;
// import io.renren.utils.I18nUtils;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
//
// import java.util.HashMap;
// import java.util.Locale;
// import java.util.Map;
//
// /**
//  * 国际化测试控制器
//  *
//  * @author Mark sunlightcs@gmail.com
//  */
// @RestController
// @RequestMapping("/api/i18n")
// @Api(tags = "国际化测试接口")
// public class ApiI18nTestController {
//
//     @Autowired
//     private I18nUtils i18nUtils;
//
//     @GetMapping("/test")
//     @ApiOperation("测试国际化异常")
//     public Result<Map<String, Object>> testI18n(@RequestParam(defaultValue = "zh") String lang) {
//         // 根据参数设置语言
//         switch (lang.toLowerCase()) {
//             case "en":
//                 i18nUtils.setLocale(Locale.ENGLISH);
//                 break;
//             case "hi":
//                 i18nUtils.setLocale(new Locale("hi"));
//                 break;
//             default:
//                 i18nUtils.setLocale(Locale.SIMPLIFIED_CHINESE);
//                 break;
//         }
//
//         // 抛出不同语言的异常进行测试
//         throw new RenException(20001);
//     }
//
//     @GetMapping("/message")
//     @ApiOperation("获取国际化消息")
//     public Result<Map<String, Object>> getMessage(@RequestParam(defaultValue = "zh") String lang) {
//         // 根据参数设置语言
//         Locale locale;
//         switch (lang.toLowerCase()) {
//             case "en":
//                 locale = Locale.ENGLISH;
//                 break;
//             case "hi":
//                 locale = new Locale("hi");
//                 break;
//             default:
//                 locale = Locale.SIMPLIFIED_CHINESE;
//                 break;
//         }
//         i18nUtils.setLocale(locale);
//
//         Map<String, Object> result = new HashMap<>();
//         result.put("currentLanguage", i18nUtils.getLanguage());
//         result.put("currentCountry", i18nUtils.getCountry());
//         result.put("currentLocale", i18nUtils.getCurrentLocale().toString());
//
//         // 测试不同错误码的国际化消息
//         result.put("error500", i18nUtils.getMessage("500"));
//         result.put("error20001", i18nUtils.getMessage("20001"));
//         result.put("error30001", i18nUtils.getMessage("30001"));
//         result.put("error40001", i18nUtils.getMessage("40001"));
//
//         return new Result<Map<String, Object>>().ok(result);
//     }
//
//     @GetMapping("/switch")
//     @ApiOperation("切换语言环境")
//     public Result<Map<String, Object>> switchLanguage(@RequestParam String lang) {
//         Locale locale;
//         switch (lang.toLowerCase()) {
//             case "en":
//                 locale = Locale.ENGLISH;
//                 break;
//             case "hi":
//                 locale = new Locale("hi");
//                 break;
//             case "zh":
//                 locale = Locale.SIMPLIFIED_CHINESE;
//                 break;
//             default:
//                 locale = Locale.SIMPLIFIED_CHINESE;
//                 break;
//         }
//
//         i18nUtils.setLocale(locale);
//
//         Map<String, Object> result = new HashMap<>();
//         result.put("message", "Language switched successfully");
//         result.put("newLanguage", i18nUtils.getLanguage());
//         result.put("newLocale", i18nUtils.getCurrentLocale().toString());
//
//         return new Result<Map<String, Object>>().ok(result);
//     }
// }
