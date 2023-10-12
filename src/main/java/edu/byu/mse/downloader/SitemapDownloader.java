package edu.byu.mse.downloader;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class SitemapDownloader {

    private final String sitemapLocation = "https://education.byu.edu/sitemap.xml";

    private URL getSitemapUrl() throws MalformedURLException {
        return new URL(sitemapLocation);
    }

    private TrustManager[] getTrustedCerts() {
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
        };
    }

    public void downloadSitemap() throws IOException, KeyManagementException, NoSuchAlgorithmException {

        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, getTrustedCerts(), new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

        HttpURLConnection conn = (HttpURLConnection) getSitemapUrl().openConnection();

        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();

        if (status >= 200 && status < 400) {

            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream("sitemap.xml");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = in.read(buffer)) != -1)
                out.write(buffer, 0, bytesRead);

        } else {
            throw new IOException("File at " + sitemapLocation + " cannot be found or is not accessible.");
        }
    }
}
