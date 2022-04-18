package cn.middleware.hystrix.value;

import cn.middleware.hystrix.annotation.DoHystrix;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public interface IValueService {


    public Object access(ProceedingJoinPoint jp, Method method, DoHystrix doHystrix, Object[] args);

}
