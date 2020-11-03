package net.okasian.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

@Service
public class ChannelConsumer {

    private static final String NONE = "<NONE>";

    @Value("${clipboard.uri}")
    private String uri;

    @Value("${clipboard.client-id}")
    private String clientId;

    private volatile long lastTime = System.currentTimeMillis();
    private RestTemplate restTemplate = new RestTemplate();

    public ChannelConsumer() {
        restTemplate.setInterceptors(Arrays.asList((request, body, execution) -> {
            request.getHeaders().add("Accept", "*/*");
            request.getHeaders().add("Accept-Encoding", "gzip deflate br");
            request.getHeaders().add("Accept-Language", "en");
            request.getHeaders().add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0");
            return execution.execute(request, body);
        }));
    }

    @Autowired
    private ClipboardProducer clipboardProducer;

    @Scheduled(fixedRate = 1000)
    public void poll() {
        final String data = restTemplate.getForObject(uri + "/poll/" + clientId + "?ts={ts}", String.class, lastTime);
        if (!NONE.equals(data)) {
            clipboardProducer.produce(data);
        }
        lastTime = System.currentTimeMillis();
    }
}
