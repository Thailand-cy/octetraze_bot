package io.tonme.minebot.component;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpClient {
    public ResponseEntity<String> client(String url, HttpMethod method, Object params){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, headers);
        //  执行HTTP请求
        return client.exchange(url, method, requestEntity, String.class);
    }

    public <T> ResponseEntity<T> client(String url, HttpMethod method, Object params, Class<T> parse){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, headers);
        //  执行HTTP请求
        return client.exchange(url, method, requestEntity, parse);
    }

    public <T> ResponseEntity<T> client(String url, HttpMethod method, Object params, Map<String,Object> headerMap, Class<T> parse){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(headerMap != null && !headerMap.isEmpty()){
            for(Map.Entry<String,Object> entry : headerMap.entrySet()){
                headers.add(entry.getKey(),entry.getValue().toString());
            }
        }
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, headers);
        //  执行HTTP请求
        return client.exchange(url, method, requestEntity, parse);
    }

    public ResponseEntity<String> get(String url, Object params){
        return client(url, HttpMethod.GET, params);
    }

    public ResponseEntity<String> get(String url, Object params, Map<String, Object> headers){
        return client(url, HttpMethod.GET, params, headers, String.class);
    }

    public <T> ResponseEntity<T> get(String url, Object params, Class<T> parse){
        return client(url,HttpMethod.GET,params, parse);
    }

    public <T> ResponseEntity<T> get(String url, Object params, Map<String, Object> headers,Class<T> parse){
        return client(url,HttpMethod.GET,params, headers, parse);
    }

    public ResponseEntity<String> post(String url, Object params){
        return client(url, HttpMethod.POST, params);
    }

    public ResponseEntity<String> post(String url, Object params, Map<String, Object> headers){
        return client(url, HttpMethod.POST, params, headers, String.class);
    }

    public <T> ResponseEntity<T> post(String url, Object params, Class<T> parse){
        return client(url,HttpMethod.POST,params, parse);
    }

    public <T> ResponseEntity<T> post(String url, Object params, Map<String, Object> headers,Class<T> parse){
        return client(url,HttpMethod.POST,params, headers, parse);
    }
}