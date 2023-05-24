package com.wjy.learn.kafka.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpHelper {

    public static CloseableHttpResponse doGet(String uri) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse rsp = httpClient.execute(get);
        return rsp;
    }

}
