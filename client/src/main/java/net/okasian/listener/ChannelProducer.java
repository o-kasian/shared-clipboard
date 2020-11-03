package net.okasian.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChannelProducer {

    @Value("${clipboard.uri}")
    private String uri;

    @Value("${clipboard.client-id}")
    private String clientId;

    private RestTemplate restTemplate = new RestTemplate();

    public void produce(final String data) {
        System.out.println(data);
        final String s = restTemplate.postForObject(uri + "/put/" + clientId, data, String.class);
        System.out.println(s);
    }
}
