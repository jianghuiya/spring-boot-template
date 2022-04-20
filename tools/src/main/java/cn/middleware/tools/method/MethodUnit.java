package cn.middleware.tools.method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
public class MethodUnit {

    public Method getMethon(JoinPoint pjp) throws NoSuchMethodException {
        Signature sig = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return pjp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
