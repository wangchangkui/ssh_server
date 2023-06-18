package com.coderwang;

import com.coderwang.config.ConnectConfig;
import com.coderwang.config.CustomerReadConfig;
import com.coderwang.config.ReadConfig;
import com.coderwang.config.YamlReadConfig;
import com.coderwang.connect.ClientEntity;
import com.coderwang.connect.ConnectSsh;
import com.coderwang.connect.CostumerClientManager;

public class ApplicationMain
{
    public static void main(String[] args) {
        ConnectSsh connectSsh = new ConnectSsh(new YamlReadConfig());
        connectSsh.connectSsh();
        ClientEntity client = CostumerClientManager.getInstance().getClient("192.168.100.111");
        client.writeCmd("ls -l \n",100);
        client.writeCmd("pwd \n",100);
    }
}
