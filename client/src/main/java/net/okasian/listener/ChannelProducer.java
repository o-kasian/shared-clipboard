package net.okasian.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class ChannelProducer {

    @Value("${clipboard.uri}")
    private String uri;

    @Value("${clipboard.client-id}")
    private String clientId;

    private RestTemplate restTemplate = new RestTemplate();

    public ChannelProducer() {
        restTemplate.setInterceptors(Arrays.asList((request, body, execution) -> {
            request.getHeaders().add("Accept", "*/*");
            request.getHeaders().add("Accept-Encoding", "gzip deflate br");
            request.getHeaders().add("Accept-Language", "en");
            request.getHeaders().add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0");
            return execution.execute(request, body);
        }));
    }

    public void produce(final String data) {
        System.out.println(data);
        final String s = restTemplate.postForObject(uri + "/put/" + clientId, data, String.class);
        System.out.println(s);
    }
}
