package net.okasian.listener;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Encryptor implements InitializingBean {

    @Value("${clipboard.secret}")
    private String secret;
    private StandardPBEStringEncryptor encryptor;

    public String encrypt(final String data) {
        return encryptor.encrypt(data);
    }

    public String decrypt(final String data) {
        return encryptor.decrypt(data);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(secret);
    }
}
