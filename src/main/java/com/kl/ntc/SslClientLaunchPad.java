package com.kl.ntc;

import com.kl.ntc.ssl.NettySSLClient;

public class SslClientLaunchPad {

    public static void main(String[] args) throws Exception {
        final String host = "localhost";
        final int sslPort = 8443;

        NettySSLClient sslClient = new NettySSLClient(host, sslPort);

        sslClient.start();
        sslClient.join();
    }
}
