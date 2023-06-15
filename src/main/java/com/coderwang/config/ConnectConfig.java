package com.coderwang.config;

import com.coderwang.antation.PropertyReadAnnotation;




public class ConnectConfig {

    @PropertyReadAnnotation("ssh.host")
    private String host;

    @PropertyReadAnnotation("ssh.port")
    private String port;

    @PropertyReadAnnotation("ssh.user")
    private String userName;

    @PropertyReadAnnotation("ssh.password")
    private String passWord;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
