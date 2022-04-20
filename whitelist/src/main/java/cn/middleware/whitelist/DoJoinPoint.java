package cn.middleware.whitelist;

import cn.middleware.tools.method.MethodUnit;
import cn.middleware.whitelist.annotation.DoWhiteList;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.lang.reflect.Method;


@Aspect
@Component
public class DoJoinPoint {

    private Logger logger = LoggerFactory.getLogger(DoJoinPoint.class);

    @Autowired
    MethodUnit methodUnit;

    @Resource
    String whiteListConfig;

    @Pointcut("@annotation(cn.middleware.whitelist.annotation.DoWhiteList)")
    public void aopPoint() {
    }

    /***
     * aop 切面 模拟用户白名单，可扩展功能根据接口获取判断是否有XXX接口调用权限
     * @param jp
     * @return
     * @throws Throwable
     */
    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint jp) throws Throwable {

        // 获取内容
        Method method = methodUnit.getMethon(jp);
        DoWhiteList whiteList = method.getAnnotation(DoWhiteList.class);

        // 获取字段值
        String keyValue = getFiledValue(whiteList.key(), jp.getArgs());
        logger.info("middleware whitelist handler method：{} value：{}", method.getName(), keyValue);
        if (null == keyValue || "".equals(keyValue)) return jp.proceed();

        String[] split = whiteListConfig.split(",");

        // 白名单过滤
        for (String str : split) {
            if (keyValue.equals(str)) {
                return jp.proceed();
            }
        }

        // 拦截
        return returnObject(whiteList, method);
    }



    // 返回对象
    private Object returnObject(DoWhiteList whiteList, Method method) throws IllegalAccessException, InstantiationException {
        Class<?> returnType = method.getReturnType();
        String returnJson = whiteList.returnJson();
        if ("".equals(returnJson)) {
            return returnType.newInstance();
        }
        return JSON.parseObject(returnJson, returnType);
    }

    // 获取属性值
    private String getFiledValue(String filed, Object[] args) {
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (null == filedValue || "".equals(filedValue)) {
                    filedValue = BeanUtils.getProperty(arg, filed);
                } else {
                    break;
                }
            } catch (Exception e) {
                if (args.length == 1) {
                    return args[0].toString();
                }
            }
        }
        return filedValue;
    }
}
