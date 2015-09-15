package com.example.MocanaClientCertBrowser;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import com.mocana.map.android.sdk.MAPCertificateProvider;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class BrowserActivity extends Activity {

    private WebView webViewMain;
    private EditText urlText;

    final static boolean DEBUG_SDK = false;
    final static String DEFAULT_URL = "http://mxoccmga01:14101/certinfo/certinfo";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.webViewMain = (WebView) findViewById(R.id.webViewMain);
        this.urlText = (EditText) findViewById(R.id.editTextUrl);
        this.urlText.setText(DEFAULT_URL);

        this.webViewMain.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }

    public void buttonOpenOnClick(View view) {

        //--- Call this only in debug mode
        if (DEBUG_SDK) {
            MAPCertificateProvider.initCertificateForDebug(getApplicationContext(), "sample.p12", "secret");
            MAPCertificateProvider.initUserForDebug("jdoe@qwe.com");
        }

        KeyStore keyStore = MAPCertificateProvider.getKeystoreForUserCertificate();

        FileOutputStream file = null;
        try {
            file = new FileOutputStream("/storage/emulated/0/DCIM/sample_exp.p12");

            try {
                keyStore.store(file, "secret".toCharArray());
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //this.webViewMain.loadUrl(this.urlText.getText().toString());
    }
}
