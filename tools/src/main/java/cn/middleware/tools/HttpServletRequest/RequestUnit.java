package cn.middleware.tools.HttpServletRequest;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestUnit {

    /**
     * 获取用户真实IP
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request){
        String ip = request.getHeader("x - forwarded - for");
        if (isIp(ip)) {
            ip = request.getHeader("Proxy - Client - IP");
        }
        if (isIp(ip)) {
            ip = request.getHeader("WL - Proxy - Client - IP");
        }
        if (isIp(ip)) {
            ip = request.getHeader("Proxy - Client - IP");
        }
        if (isIp(ip)) {
            ip = request.getHeader("WL - Proxy - Client - IP");
        }
        return ip;
    }

    private boolean isIp(String ip){
        return ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip);
    }

}
