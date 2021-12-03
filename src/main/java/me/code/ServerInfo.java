package me.code;

import lombok.Getter;

@Getter
public class ServerInfo {

    private String name;
    private String address;
    private int port;

    public ServerInfo(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }

}
