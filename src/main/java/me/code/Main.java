package me.code;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import me.code.proxy.Connection;
import me.code.proxy.Proxy;
import me.code.proxy.ProxyServerHandler;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        new Thread(() -> {

            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();

            while (!line.equalsIgnoreCase("exit")) {

                String[] split = line.split(" ");
                if (split[0].equalsIgnoreCase("switch")) {
                    String serverName = split[2];
                    String username = split[1];

                    ServerInfo info = Proxy.INSTANCE.getConfig().findServer(serverName);
                    Connection connection = Proxy.INSTANCE.getConnection(username);
                    if (connection != null && info != null) {

                        // disconnect

                        ChannelPipeline serverPipeline = connection.getServerChannel().pipeline();
                        ChannelPipeline clientPipeline = connection.getClientChannel().pipeline();

                        while (serverPipeline.first() != null)
                            serverPipeline.removeFirst();

                        while (clientPipeline.first() != null)
                            clientPipeline.removeFirst();


                        connection.getServerChannel().close();
                        // reconnect

                        Proxy.connect(info, connection);

                    } else {
                        System.out.println("No such player or no such server.");
                    }

                }


                line = scanner.nextLine();
            }

            System.out.println("Stopped command line.");
        }).start();

        Proxy.INSTANCE.start();




    }

}
