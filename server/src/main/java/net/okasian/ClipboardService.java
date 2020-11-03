package net.okasian;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class ClipboardService {

    public static final String NONE = "<NONE>";
    private final ConcurrentHashMap<String, String> clientData = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> clientDataTimestamps = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> objectMonitors = new ConcurrentHashMap<>();
    private final Long timeout = TimeUnit.SECONDS.toMillis(10);

    public DeferredResult<String> pollData(final String uid, final Long lastUpdate) {
        final DeferredResult<String> deferredResult = new DeferredResult<>(timeout, null);
        final Object monitor = getMonitor(uid);
        CompletableFuture.runAsync(() -> {
            synchronized (monitor) {
                Long ts = getTs(uid);
                if (lastUpdate < ts) {
                    deferredResult.setResult(clientData.get(uid));
                } else {
                    try {
                        monitor.wait(timeout);
                        ts = getTs(uid);
                        if (lastUpdate < ts) {
                            deferredResult.setResult(getData(uid));
                        } else {
                            deferredResult.setResult(NONE);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        return deferredResult;
    }

    private synchronized String getData(final String uid) {
        if (!clientData.containsKey(uid)) {
            clientData.put(uid, NONE);
        }
        return clientData.get(uid);
    }

    private synchronized Object getMonitor(final String uid) {
        if (!objectMonitors.containsKey(uid)) {
            objectMonitors.put(uid, new Object());
        }
        return objectMonitors.get(uid);
    }

    private synchronized Long getTs(final String uid) {
        if (!clientDataTimestamps.containsKey(uid)) {
            clientDataTimestamps.put(uid, 0L);
        }
        return clientDataTimestamps.get(uid);
    }

    public synchronized void putData(final String uid, final String data) {
        clientData.put(uid, data);
        clientDataTimestamps.put(uid, System.currentTimeMillis());
        final Object monitor = getMonitor(uid);
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }
}
