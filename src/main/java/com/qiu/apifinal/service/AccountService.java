package com.qiu.apifinal.service;


import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.qiu.apifinal.entity.Account;
import com.qiu.apifinal.entity.dto.*;
import com.qiu.apifinal.mapper.AccountMapper;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;



@Service
public class AccountService {

    @Resource
    AccountMapper accountMapper;

    @Resource RedisService redisService;

    @Resource
    RabbitTemplate rabbitTemplate;


    public SaResult sendRegisterCode(SendCodeBody sendCodeBody) {
        //
        if(!sendCodeBody.isComplete()) {
            return SaResult.error("参数不完整");
        }
        if(accountMapper.selectByUsername(sendCodeBody.getUsername())!=null) {
            return SaResult.error("用户名已存在");
        }
        if (sendCodeBody.getEmail() == null || !EmailService.isValidEmail(sendCodeBody.getEmail())) {
            return SaResult.error("邮箱格式不正确");
        }
        //检查邮箱是否已经注册
        if (!accountMapper.selectByEmail(sendCodeBody.getEmail()).isEmpty()) {
            return SaResult.error("邮箱已注册");
        }
        //检查redis中是否已经有验证码
        if (redisService.getValue(sendCodeBody.getEmail()) != null) {
            return SaResult.error("验证码已发送，请查看邮箱，不要重复发送");
        }


        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        EmailMessage emailMessage = new EmailMessage(sendCodeBody.getEmail(), "验证码",
                "您的验证码是：" + verificationCode);
        rabbitTemplate.convertAndSend("emailQueue", emailMessage);
        // 将验证码存入Redis
        redisService.setValue(sendCodeBody.getEmail(), verificationCode, 180);
        return SaResult.ok("发送验证码成功");

    }

    public  SaResult sendResetCode(SendCodeBody sendCodeBody){
        if(!sendCodeBody.isComplete()) {
            return SaResult.error("参数不完整");
        }
        //检查用户名或邮箱是否对应
        Account account = accountMapper.selectByUsernameAndEmail(sendCodeBody.getUsername(),sendCodeBody.getEmail());
        if(account == null) {
            return SaResult.error("用户名或邮箱错误");
        }
        //检查redis中是否已经有验证码
        if (redisService.getValue(sendCodeBody.getEmail()) != null) {
            return SaResult.error("验证码已发送，请查看邮箱，不要重复发送");
        }
        //发送验证码
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        EmailMessage emailMessage = new EmailMessage(sendCodeBody.getEmail(), "重置密码",
                "您的验证码是：" + verificationCode);
        rabbitTemplate.convertAndSend("emailQueue", emailMessage);
        // 将验证码存入Redis
        redisService.setValue(sendCodeBody.getEmail(), verificationCode, 180);
        return SaResult.ok("发送验证码成功");

    }






    public SaResult resetPassword(ResetBody resetBody) {
        if (!resetBody.isComplete()) {
            return SaResult.error("参数不完整");
        }
        Account account = accountMapper.selectByUsername(resetBody.getUsername());

        if (account == null) {
            return SaResult.error("用户名不存在");
        }

        if (!EmailService.isValidEmail(resetBody.getEmail())) {
            return SaResult.error("邮箱格式不正确");
        }
        //检查邮箱是否与用户名匹配
        if (!account.getEmail().equals(resetBody.getEmail())) {
            return SaResult.error("邮箱错误");
        }
        String codeFromRedis = redisService.getValue(resetBody.getEmail());
        if (!codeFromRedis.equals(resetBody.getCode())) {
            return SaResult.error("验证码错误");
        }
        //重置密码
        String encryptedPassword = SaSecureUtil.sha1(resetBody.getPassword());
        account.setPassword(encryptedPassword);
        accountMapper.update(account);
        return SaResult.ok("重置密码成功");

    }

    public SaResult register(RegisterBody registerBody) {
        if(!registerBody.isComplete()){
            return SaResult.error("参数不完整");
        }

        if(accountMapper.selectByUsername(registerBody.getUsername())!=null) {
            return SaResult.error("用户名已存在");
        }
        String codeFromRedis = redisService.getValue(registerBody.getEmail());
        if(!codeFromRedis.equals(registerBody.getCode())){
        return SaResult.error("验证码错误");
        }

        // 进行密码加密等操作
        String encryptedPassword = SaSecureUtil.sha1(registerBody.getPassword());
        Account account = new Account();
        account.setUsername(registerBody.getUsername());
        account.setPassword(encryptedPassword);
        account.setEmail(registerBody.getEmail());
        accountMapper.insert(account);
        return  SaResult.ok("注册成功");
    }

    public SaResult  doLogin(LoginBody loginBody) {
        if(!loginBody.isComplete()) {
            return SaResult.error("参数不完整");
        }
        String encryptedPassword = SaSecureUtil.sha1(loginBody.getPassword());
        var username = loginBody.getUsername();
        Account account = accountMapper.selectByUsernameAndPassword(username,encryptedPassword);
        if(account == null) {
            return SaResult.error("用户名或密码错误");
        }
        StpUtil.login(account.getUid());
        return SaResult.ok("登录成功");

    }














}
