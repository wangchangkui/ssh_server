# ssh_server

    使用Java 执行远程服务器的一些命令，在某些时候需要调用服务器的资源 例如 python 脚本之类的，
    并不在一台服务器的时候，就可以使用该脚本执行一些命令

# 简单使用：
    配置文件：config.properties 或者yaml，简单存放在项目的resource目录下即可
    这里以properties为例：
    ssh.host= 192.168.100.101
    ssh.port=22
    ssh.user=root
    ssh.password=root

# 代码示例：

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
