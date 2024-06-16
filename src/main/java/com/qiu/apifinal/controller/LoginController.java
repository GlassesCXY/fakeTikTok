package com.qiu.apifinal.controller;


import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.qiu.apifinal.entity.dto.LoginBody;
import com.qiu.apifinal.entity.dto.RegisterBody;
import com.qiu.apifinal.entity.dto.ResetBody;
import com.qiu.apifinal.entity.dto.SendCodeBody;
import com.qiu.apifinal.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录测试
 */
@RestController
@RequestMapping("/auth/")
public class LoginController {
    @Resource
    AccountService accountService;



    @RequestMapping("register")
    public SaResult doRegister(@RequestBody RegisterBody registerBody) {
        return accountService.register(registerBody);

    }

    @RequestMapping("sendRegisterCode")
    public SaResult sendCode(@RequestBody SendCodeBody sendCodeBody) {
        return accountService.sendRegisterCode(sendCodeBody);
    }

    @RequestMapping("sendResetCode")
    public SaResult sendResetCode(@RequestBody SendCodeBody sendCodeBody) {
        return accountService.sendResetCode(sendCodeBody);
    }

    @RequestMapping("resetPassword")
    public SaResult resetPassword(@RequestBody ResetBody resetBody) {
        return accountService.resetPassword(resetBody);
    }


    @RequestMapping("login")
    public SaResult doLogin(@RequestBody LoginBody loginBody) {
        return accountService.doLogin(loginBody);
    }


    @RequestMapping("isLogin")
    public SaResult isLogin() {
        // StpUtil.isLogin() 查询当前客户端是否登录，返回 true 或 false
        boolean isLogin = StpUtil.isLogin();
        return SaResult.ok("当前客户端是否登录：" + isLogin);
    }


    @RequestMapping("checkLogin")
    public SaResult checkLogin() {
        // 检验当前会话是否已经登录, 如果未登录，则抛出异常：`NotLoginException`
        StpUtil.checkLogin();
        // 抛出异常后，代码将走入全局异常处理（GlobalException.java），
        // 如果没有抛出异常，则代表通过了登录校验，返回下面信息
        return SaResult.ok("校验登录成功，这行字符串是只有登录后才会返回的信息");
    }


    @RequestMapping("logout")
    public SaResult logout() {
        // 退出登录会清除三个地方的数据：
        // 		1、Redis中保存的 Token 信息
        // 		2、当前请求上下文中保存的 Token 信息
        // 		3、Cookie 中保存的 Token 信息（如果未使用Cookie模式则不会清除）
        StpUtil.logout();

        // StpUtil.logout() 在未登录时也是可以调用成功的，
        // 也就是说，无论客户端有没有登录，执行完 StpUtil.logout() 后，都会处于未登录状态
        System.out.println("当前是否处于登录状态：" + StpUtil.isLogin());

        // 返回给前端
        return SaResult.ok("退出登录成功");
    }


    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        // TokenName 是 Token 名称的意思，此值也决定了前端提交 Token 时应该使用的参数名称
        String tokenName = StpUtil.getTokenName();
        System.out.println("前端提交 Token 时应该使用的参数名称：" + tokenName);

        // 使用 StpUtil.getTokenValue() 获取前端提交的 Token 值
        // 框架默认前端可以从以下三个途径中提交 Token：
        // 		Cookie 		（浏览器自动提交）
        // 		Header头	（代码手动提交）
        // 		Query 参数	（代码手动提交） 例如： /user/getInfo?satoken=xxxx-xxxx-xxxx-xxxx
        // 读取顺序为： Query 参数 --> Header头 -- > Cookie
        // 以上三个地方都读取不到 Token 信息的话，则视为前端没有提交 Token
        String tokenValue = StpUtil.getTokenValue();
        System.out.println("前端提交的Token值为：" + tokenValue);

        // TokenInfo 包含了此 Token 的大多数信息
        SaTokenInfo info = StpUtil.getTokenInfo();
        System.out.println("Token 名称：" + info.getTokenName());
        System.out.println("Token 值：" + info.getTokenValue());
        System.out.println("当前是否登录：" + info.getIsLogin());
        System.out.println("当前登录的账号id：" + info.getLoginId());
        System.out.println("当前登录账号的类型：" + info.getLoginType());
        System.out.println("当前登录客户端的设备类型：" + info.getLoginDevice());
        System.out.println("当前 Token 的剩余有效期：" + info.getTokenTimeout()); // 单位：秒，-1代表永久有效，-2代表值不存在
        System.out.println("当前 Token 距离被冻结还剩：" + info.getTokenActiveTimeout()); // 单位：秒，-1代表永久有效，-2代表值不存在
        System.out.println("当前 Account-Session 的剩余有效期" + info.getSessionTimeout()); // 单位：秒，-1代表永久有效，-2代表值不存在
        System.out.println("当前 Token-Session 的剩余有效期" + info.getTokenSessionTimeout()); // 单位：秒，-1代表永久有效，-2代表值不存在

        // 返回给前端
        return SaResult.data(StpUtil.getTokenInfo());
    }


}