package cn.middleware.test.whitelist.controller;

import cn.middleware.methodext.annotation.DoMethodExt;
import cn.middleware.ratelimiter.annotation.DoRateLimiter;
import cn.middleware.hystrix.annotation.DoHystrix;
import cn.middleware.test.whitelist.entity.UserInfo;
import cn.middleware.whitelist.annotation.DoWhiteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 测试白名单
     * 通过：http://localhost:8081/api/queryUserInfo?userId=aaa
     * 拦截：http://localhost:8081/api/queryUserInfo?userId=123
     */
    @DoWhiteList(key = "userId", returnJson = "{\"code\":\"1111\",\"info\":\"非白名单可访问用户拦截！\"}")
    @GetMapping(value = "/queryUserInfo")
    public UserInfo queryUserInfo(@RequestParam String userId) {
        logger.info("查询用户信息，userId：{}", userId);
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }

    /**
     * 测试超时熔断
     * @param userId
     * @return
     */
    @DoHystrix(timeoutValue = 350,returnJson = "{\"code\":\"1111\",\"info\":\"调用方法超过350毫秒，熔断返回！\"}")
    @GetMapping(value = "/testHystrix")
    public UserInfo testHystrix(@RequestParam String userId) throws InterruptedException {
        logger.info("查询用户信息，userId：{}", userId);
        //Thread.sleep(1000);
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }

    /**
     * 测试调用限流
     * @param userId
     * @return
     */
    @DoRateLimiter(permitsPerSecond = 1,returnJson = "{\"code\":\"1111\",\"info\":\"调用方法超过最大次数，限流返回！\",\"name\":null,\"age\":null,\"address\":null}")
    @GetMapping(value = "/testHRateLimiter")
    public UserInfo testRateLimiter(@RequestParam String userId) throws InterruptedException {
        logger.info("查询用户信息，userId：{}", userId);
        //Thread.sleep(1000);
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }

    /**
     * 测试调用自定义拦截方法
     * @param userId
     * @return
     */
    @DoMethodExt(method = "blacklist", returnJson = "{\"code\":\"1111\",\"info\":\"自定义校验方法拦截，不允许访问！\"}")
    @GetMapping(value = "/testHRateLimiter")
    public UserInfo testMiddleware(@RequestParam String userId) throws InterruptedException {
        logger.info("查询用户信息，userId：{}", userId);
        //Thread.sleep(1000);
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }


    /**
     * 自定义黑名单，拦截方法
     */
    public boolean blacklist(@RequestParam String userId) {
        if ("bbb".equals(userId) || "222".equals(userId)) {
            logger.info("拦截自定义黑名单用户 userId：{}", userId);
            return false;
        }
        return true;
    }

}
