package com.coderwang;

import com.coderwang.config.ConfigType;
import com.coderwang.config.ConnectConfigFactory;
import com.coderwang.connect.ClientEntity;
import com.coderwang.connect.ConnectSshHandler;
import com.coderwang.connect.CostumerClientManager;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author wck
 * @version 1.0.0
 * @Description
 * @createTime 2023年07月06日 11:37:00
 */
public class ApplicationTest {


    @Test
    void test1(){
        // 创建连接参数
        ConnectSshHandler connectSshHandler = new ConnectSshHandler(ConnectConfigFactory.getReadConfig(ConfigType.PROPERTIES));
        // 注入一个连接器管理池对象(如果你有多个对象 请管理好自己的连接器对象池) 并获得连接器
        ClientEntity client = connectSshHandler.connectSsh(CostumerClientManager.getInstance());
        // 执行一些命令 但是不包括 ps,top,vim 等命令
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
