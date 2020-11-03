package net.okasian.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

@Service
public class ClipboardConsumer {

    @Autowired
    private Encryptor encryptor;

    private static final DataFlavor df = DataFlavor.stringFlavor;
    private volatile String lastData;
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    @Autowired
    private ChannelProducer channelProducer;

    @Scheduled(fixedRate = 20)
    public void poll() throws IOException, UnsupportedFlavorException {
        if (clipboard.isDataFlavorAvailable(df)) {
            final String data = clipboard.getData(df).toString();
            if (!data.equals(lastData)) {
                lastData = data;
                onDataChanged();
            }
        }
    }

    private void onDataChanged() {
        channelProducer.produce(encryptor.encrypt(lastData));
    }
}
