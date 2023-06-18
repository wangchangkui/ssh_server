package com.coderwang;

import com.coderwang.config.YamlReadConfig;
import com.coderwang.connect.ClientEntity;
import com.coderwang.connect.ConnectSsh;
import com.coderwang.connect.CostumerClientManager;
import org.junit.jupiter.api.Test;

public class ApplicationMainTest {


    @Test
    void test01(){
        ConnectSsh connectSsh = new ConnectSsh(new YamlReadConfig());
        connectSsh.connectSsh();
        ClientEntity client = CostumerClientManager.getInstance().getClient("101.37.253.142");
        client.writeCmd("ls -l \n",100);
        client.writeCmd("aaa \n",100);
        client.close();
    }
}
