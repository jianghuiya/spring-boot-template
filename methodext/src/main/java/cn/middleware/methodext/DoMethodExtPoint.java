package cn.middleware.methodext;

import cn.middleware.methodext.annotation.DoMethodExt;
import cn.middleware.tools.method.MethodUnit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import java.lang.reflect.Method;

@Aspect
@Component
public class DoMethodExtPoint {

    private Logger logger = LoggerFactory.getLogger(DoMethodExtPoint.class);

    @Autowired
    MethodUnit methodUnit;

    @Pointcut("@annotation(cn.middleware.methodext.annotation.DoMethodExt)")
    public void aopPoint(){
    }

    @Around("aopPoint")
    public Object doRouter(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取内容
        Method method = methodUnit.getMethon(joinPoint);
        DoMethodExt doMethodExt = method.getAnnotation(DoMethodExt.class);

        //获取拦截方法
        String methodName = doMethodExt.method();
        //功能处理
        Method methodExt = getClass(joinPoint).getMethod(methodName,method.getParameterTypes());
        Class<?> returnType = methodExt.getReturnType();
        if(!"boolean".equals(returnType.getName())){
            throw new RuntimeException("annotation @DoMethodExt set method：" + methodName + " returnType is not boolean");
        }

        //拦截器判断正常，继续
        boolean invoke = (boolean) methodExt.invoke(joinPoint.getTarget(),joinPoint.getArgs());

        return invoke ? joinPoint.proceed() : JSON.parseObject(doMethodExt.returnJson(), method.getReturnType());

    }

    private Class<? extends Object> getClass(JoinPoint joinPoint){
        return joinPoint.getTarget().getClass();
    }



}
