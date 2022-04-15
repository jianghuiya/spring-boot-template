package cn.middleware.whitelist;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "bugstack.whitelist")
public class WhiteListProperties {

    private String users;

}
