package com.kl.ntc;

import com.kl.ntc.tcp.NettyTCPClient;

public class TcpClientLaunchPad {

    public static void main(String[] args) throws Exception {
        final String host = "localhost";
        final int tcpPort = 8000;

        NettyTCPClient tcpClient = new NettyTCPClient(host, tcpPort);

        tcpClient.start();
        tcpClient.join();
    }
}
