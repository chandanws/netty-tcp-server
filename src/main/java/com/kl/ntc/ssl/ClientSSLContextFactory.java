package com.kl.ntc.ssl;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Security;

public class ClientSSLContextFactory {
    private static final String PROTOCOL = "TLS";
    private static final SSLContext CLIENT_CONTEXT;

    static {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX";
        }

        SSLContext clientContext;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("client.jks"), "sc_password".toCharArray());

            // Set up key manager factory to use key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks, "kc_password".toCharArray());

            // Set up trusted manager factory to use key store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            tmf.init(ks);

            TrustManager[] tms = tmf.getTrustManagers();
            X509TrustManager sunJSSEX509TrustManager = null;
            for (TrustManager tm : tms) {
                if (tm instanceof X509TrustManager) {
                    sunJSSEX509TrustManager = (X509TrustManager) tm;
                }
            }
            TrustManager tm = new ClientTrustManager(sunJSSEX509TrustManager);

            // Initialize the SSLContext to work with key managers
            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(kmf.getKeyManagers(), new TrustManager[]{tm}, null);
        } catch (Exception e) {
            throw new Error("Failed to initialize the client-side SSLContext", e);
        }
        CLIENT_CONTEXT = clientContext;
    }

    static SSLContext getClientContext() {
        return CLIENT_CONTEXT;
    }

    private ClientSSLContextFactory() {
        // Unused
    }
}
