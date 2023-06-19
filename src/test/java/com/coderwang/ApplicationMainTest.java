package com.coderwang;

import com.coderwang.config.YamlReadConfig;
import com.coderwang.connect.ClientEntity;
import com.coderwang.connect.ConnectSsh;
import com.coderwang.connect.CostumerClientManager;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ApplicationMainTest {


    @Test
    void test01(){
        ConnectSsh connectSsh = new ConnectSsh(new YamlReadConfig());
        connectSsh.connectSsh();

        ClientEntity client= CostumerClientManager.getInstance().getClient("101.37.253.142");
        while (true){
            // 从控制台输入获取输入的字符串
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in))) {
                String cmd = buffer.readLine();
                client.writeCmd(cmd+"\n");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
