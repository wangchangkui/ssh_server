package com.coderwang;

import com.coderwang.config.ConnectConfig;
import com.coderwang.config.CustomerReadConfig;
import com.coderwang.config.ReadConfig;
import com.coderwang.config.YamlReadConfig;
import com.coderwang.connect.ConnectSsh;

public class ApplicationMain
{
    public static void main(String[] args) {
        ConnectSsh connectSsh = new ConnectSsh(new YamlReadConfig());
        connectSsh.connectSsh();
    }
}
