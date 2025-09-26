package com.collector.etc;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.config.keys.FilePasswordProvider;
import org.apache.sshd.common.config.keys.loader.KeyPairResourceLoader;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import jakarta.annotation.PreDestroy;

// ssh 터널링 설정.
@Configuration
public class SshTunnelConfig {

//	@Value("${ssh.host}") private String sshHost;
//    @Value("${ssh.port}") private int sshPort;
//    @Value("${ssh.user}") private String sshUser;
//    @Value("${ssh.privateKey}") private String sshPrivateKey;
//    @Value("${ssh.keyPassphrase:}") private String sshKeyPassphrase;
//    @Value("${ssh.localPort}") private int sshLocalPort;
//    @Value("${ssh.remoteHost}") private String sshRemoteHost;
//    @Value("${ssh.remotePort}") private int sshRemotePort;

    @Value("${spring.datasource.driver-class-name}") private String driverClassName;
    @Value("${spring.datasource.url}") private String jdbcUrl;
    @Value("${spring.datasource.username}") private String dbUserName;
    @Value("${spring.datasource.password}") private String dbUserPw;

    private SshClient client;
    private ClientSession session;

    @Bean
    public DataSource dataSource() throws Exception {
//        // 1. SSH 클라이언트 설정
//        client = SshClient.setUpDefaultClient();
//        client.getProperties().put("idle-timeout", 1800000);
//        client.start();
//
//        // 2. 키 로딩 (패스프레이즈 지원)
//        KeyPairResourceLoader loader = SecurityUtils.getKeyPairResourceParser();
//        Path path = Paths.get(sshPrivateKey);
//        Collection<KeyPair> keys = loader.loadKeyPairs(null, path,
//                FilePasswordProvider.of((sshKeyPassphrase == null || sshKeyPassphrase.isBlank()) ? null : sshKeyPassphrase));
//
//        // 3. 세션 생성 및 인증
//        session = client.connect(sshUser, sshHost, sshPort)
//                .verify(10, TimeUnit.SECONDS)
//                .getSession();
//        session.addPublicKeyIdentity(keys.iterator().next());
//        session.auth().verify(10, TimeUnit.SECONDS);
//
//        // 4. 포트 포워딩 설정
//        SshdSocketAddress localAddress = new SshdSocketAddress("localhost", sshLocalPort);
//        SshdSocketAddress remoteAddress = new SshdSocketAddress(sshRemoteHost, sshRemotePort);
//        session.startLocalPortForwarding(localAddress, remoteAddress);

        // 5. DataSource 생성
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(dbUserName);
        dataSource.setPassword(dbUserPw);

        return dataSource;
    }

    @PreDestroy
    public void closeSsh() {
        try {
            if (session != null && session.isOpen()) session.close();
            if (client != null && client.isOpen()) client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
