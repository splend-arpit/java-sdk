package dispatchlabs.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import dispatchlabs.states.Transaction;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 */
public class Http implements AutoCloseable {

    /**
     * Class level-declarations.
     */
    private CloseableHttpClient closeableHttpClient;

    /**
     *
     */
    public Http() throws Exception {
        closeableHttpClient = HttpClients.createDefault();
    }

    /**
     * @param trustAll
     */
    public Http(boolean trustAll) throws Exception {
        if (trustAll) {
            closeableHttpClient = createTrustedCloseableHttpClient();
        } else {
            closeableHttpClient = HttpClients.createDefault();
        }
    }

    /**
     * @param url
     * @param headers
     * @param entity
     * @return
     * @throws Exception
     */
    public String post(String url, Map<String, String> headers, String entity) throws Exception {
        StringEntity stringEntity = new StringEntity(entity, "UTF-8");
        HttpPost httpPost = new HttpPost(url);

        // Set headers.
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // Set entity.
        stringEntity.setContentType("application/json; charset=UTF-8");
        httpPost.setEntity(stringEntity);

        // Execute.
        HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

        // Get httpResponse.
        StringBuilder responseStringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            responseStringBuilder.append(line);
        }
        return responseStringBuilder.toString();
    }

    /**
     * @param url
     * @param headers
     * @param nameValuePairs
     * @return
     * @throws Exception
     */
    public String post(String url, Map<String, String> headers, ArrayList<NameValuePair> nameValuePairs) throws Exception {
        HttpPost httpPost = new HttpPost(url);

        // Set headers.
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // Set entity.
        if (nameValuePairs != null) {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }

        // Execute.
        HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

        // Get httpResponse.
        StringBuilder responseStringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            responseStringBuilder.append(line);
        }

        return responseStringBuilder.toString();
    }

    /**
     * @param url
     * @param headers
     * @return
     * @throws Exception
     */
    public String get(String url, Map<String, String> headers) throws Exception {
        HttpGet httpGet = new HttpGet(url);

        // Set headers.
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // Execute.
        HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

        StringBuilder responseStringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            responseStringBuilder.append(line);
        }

        return responseStringBuilder.toString();
    }

    /**
     * @throws Exception
     */
    public void close() throws Exception {
        closeableHttpClient.close();
    }

    /**
     * @return
     * @throws Exception
     */
    private CloseableHttpClient createTrustedCloseableHttpClient() throws Exception {
        TrustManager[] trustManager = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManager, new SecureRandom());
        return HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext)).build();
    }
}
