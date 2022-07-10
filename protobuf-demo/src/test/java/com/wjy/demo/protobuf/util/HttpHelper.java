package com.wjy.demo.protobuf.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpHelper {

    public static CloseableHttpResponse doPost(String uri, byte[] content, String contentType) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(uri);
        post.setEntity(new ByteArrayEntity(content));
        post.setHeader("Content-Type", contentType);
        CloseableHttpResponse rsp = httpClient.execute(post);
        return rsp;
    }

}
