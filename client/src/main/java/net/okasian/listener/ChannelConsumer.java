package net.okasian.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChannelConsumer {

    private static final String NONE = "<NONE>";

    @Value("${clipboard.uri}")
    private String uri;

    @Value("${clipboard.client-id}")
    private String clientId;

    private volatile long lastTime = System.currentTimeMillis();
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ClipboardProducer clipboardProducer;

    @Scheduled(fixedRate = 100)
    public void poll() {
        final String data = restTemplate.getForObject(uri + "/poll/" + clientId + "?ts={ts}", String.class, lastTime);
        if (!NONE.equals(data)) {
            clipboardProducer.produce(data);
        }
        lastTime = System.currentTimeMillis();
    }
}
