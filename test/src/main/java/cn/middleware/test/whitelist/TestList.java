package cn.middleware.test.whitelist;

import cn.middleware.test.whitelist.entity.UserInfo;
import org.apache.catalina.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestList {

    public static void main(String[] ares){
        UserInfo u1 = new UserInfo();
        u1.setName("a");
        u1.setAge(23);
        UserInfo u2 = new UserInfo();
        u2.setName("b");
        u2.setAge(26);
        UserInfo u3 = new UserInfo();
        u3.setName("c");
        u3.setAge(23);
        UserInfo u4 = new UserInfo();
        u4.setName("d");
        u4.setAge(26);
        UserInfo u5 = new UserInfo();
        u5.setName("e");
        u5.setAge(26);
        List<UserInfo> list = new ArrayList<>();
        list.add(u1);
        list.add(u2);
        list.add(u3);
        list.add(u4);
        list.add(u5);
        System.out.println("过滤前");
        list.forEach(userInfo -> {
            System.out.println(userInfo);
        });
        list = list.stream().filter(userInfo -> userInfo.getAge()==26).collect(Collectors.toList());
        System.out.println("过滤后");
        list.forEach(userInfo -> {
            System.out.println(userInfo);
        });

    }

}
