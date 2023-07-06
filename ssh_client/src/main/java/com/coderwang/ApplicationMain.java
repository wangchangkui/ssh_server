package com.coderwang;

import com.coderwang.config.ConfigType;
import com.coderwang.config.ConnectConfigFactory;
import com.coderwang.config.YamlReadConfig;
import com.coderwang.connect.ClientEntity;
import com.coderwang.connect.ConnectSsh;
import com.coderwang.connect.CostumerClientManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author 29059
 */
public class ApplicationMain
{
    public static void main(String[] args) {
        ConnectSsh connectSsh = new ConnectSsh(ConnectConfigFactory.getReadConfig(ConfigType.PROPERTIES));
        ClientEntity client = connectSsh.connectSsh();
        while (true){
            System.out.println("请输入命令:");
            // 从控制台输入获取输入的字符串
            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
            String cmd;
            try {
                cmd = buffer.readLine();
                if("exit".equals(cmd)){
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String response = client.writeCmd(cmd + "\n");
            System.out.println(response);
        }
        client.close();
    }
}
