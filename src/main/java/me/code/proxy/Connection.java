package me.code.proxy;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Connection {


    private String username;
    private Channel clientChannel;
    private Channel serverChannel;

    public Connection(String username) {
        this.username = username;
    }

}
