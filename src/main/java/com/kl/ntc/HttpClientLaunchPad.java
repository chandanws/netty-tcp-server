package com.kl.ntc;

import com.kl.ntc.http.NettyHTTPClient;

public class HttpClientLaunchPad {

    public static void main(String[] args) throws Exception {
        final String host = "localhost";
        final String path = "/";
        final int httpPort = 8080;

        NettyHTTPClient httpClient = new NettyHTTPClient(host, httpPort, path);

        httpClient.start();
        httpClient.join();
    }
}
