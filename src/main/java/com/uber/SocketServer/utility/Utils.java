package com.uber.SocketServer.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    @Autowired
    private Environment environment;

    public String getConfigValue(String configPath) {
        return environment.getProperty(configPath, "");
    }
}
