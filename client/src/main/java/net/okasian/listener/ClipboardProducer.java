package net.okasian.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

@Service
public class ClipboardProducer {

    @Autowired
    private Encryptor encryptor;

    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public void produce(final String string) {
        final StringSelection stringSelection = new StringSelection(encryptor.decrypt(string));
        clipboard.setContents(stringSelection, stringSelection);
    }
}
