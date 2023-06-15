package com.coderwang;

import com.coderwang.config.ConnectConfig;
import com.coderwang.config.CustomerReadConfig;
import com.coderwang.config.ReadConfig;
import com.coderwang.config.YamlReadConfig;

public class ApplicationMain
{
    public static void main(String[] args) {
        ReadConfig customerReadConfig = new YamlReadConfig();
        ConnectConfig connectConfig = customerReadConfig.readConfig();
        System.out.println(connectConfig);
    }
}
