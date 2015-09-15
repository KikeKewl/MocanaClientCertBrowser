//
//  MAPCertificateProvider.java
//
//  Copyright (c) 2014 Mocana Corp. All rights reserved.
//  Build: 3.3.3.8787
//  Generated on Fri, Feb 27 12:16:49 PST 2015
//


package com.mocana.map.android.sdk;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;

public class MAPCertificateProvider {

    private static Context sContext                 = null;
    private static String  sCertificateFileName     = null;
    private static String  sCertificatePassword     = null;
    private static String  sUsername                = null;

    public static void initCertificateForDebug(
            final Context context,
            final String certificatePath,
            final String password) {

        sContext = context;
        sCertificateFileName = certificatePath;
        sCertificatePassword = password;
    }

    public static void initUserForDebug(final String username) {
        sUsername = username;
    }



    public static KeyStore getKeystoreForUserCertificate() {
        if (sContext != null && sCertificateFileName != null && sCertificatePassword != null) {
            AssetManager am = sContext.getAssets();
            try {
                InputStream is = am.open(sCertificateFileName);
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                copy(is, dataStream, 512);
                byte[] p12 = dataStream.toByteArray();

                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                InputStream sslInputStream = new ByteArrayInputStream(p12);
                keyStore.load( sslInputStream, sCertificatePassword.toCharArray() );
                return keyStore;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static X509Certificate getUserIdentityCertificate() {
        if (sContext != null && sCertificateFileName != null && sCertificatePassword != null) {
            AssetManager am = sContext.getAssets();
            try {
                KeyStore keystore = getKeystoreForUserCertificate();
                for (String alias : Collections.list(keystore.aliases())) {
                    X509Certificate certificate = (X509Certificate)keystore.getCertificate(alias);
                    return certificate;
                }
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean hasCertificate() {
        return getUserIdentityCertificate() != null;
    }

    public static String getUsername() {
        return sUsername;
    }

    private static void copy(
            final InputStream in,
            final OutputStream out,
            final int bufferSize) throws IOException {

        byte[] buffer = new byte[bufferSize];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        in.close();
    }


}