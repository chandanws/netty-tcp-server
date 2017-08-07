package com.kl.nts.server.ssl;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Security;

public class ServerSSLContextFactory {
    private static final String PROTOCOL = "TLS";
    private static final SSLContext SERVER_CONTEXT;

    static {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }

        SSLContext serverContext;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("server.jks"), "ss_password".toCharArray());

            // Set up key manager factory to use key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks, "ks_password".toCharArray());

            // Set up trusted manager factory to use key store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            tmf.init(ks);

            X509TrustManager sunJSSEX509TrustManager = null;
            TrustManager[] tms = tmf.getTrustManagers();
            for (TrustManager tm : tms) {
                if (tm instanceof X509TrustManager) {
                    sunJSSEX509TrustManager = (X509TrustManager) tm;
                }
            }
            TrustManager tm = new ServerTrustManager(sunJSSEX509TrustManager);

            // Initialize the SSLContext to work with key managers
            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(kmf.getKeyManagers(), new TrustManager[]{tm}, null);
        } catch (Exception e) {
            throw new Error("Failed to initialize the server-side SSLContext", e);
        }

        SERVER_CONTEXT = serverContext;
    }

    static SSLContext getServerContext() {
        return SERVER_CONTEXT;
    }

    private ServerSSLContextFactory() {
        // Unused
    }
}
