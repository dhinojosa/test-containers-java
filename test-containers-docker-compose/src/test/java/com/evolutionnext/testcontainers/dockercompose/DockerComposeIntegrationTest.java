package com.evolutionnext.testcontainers.dockercompose;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

@Testcontainers
public class DockerComposeIntegrationTest {

    private static final int REDIS_PORT = 6379;

    @Container
        public static DockerComposeContainer environment =
        new DockerComposeContainer(new File(DockerComposeIntegrationTest.class.getClassLoader().getResource("docker-compose.yaml").getFile()))
            .withExposedService("redis", REDIS_PORT);

    @Test
    void testDockerCompose() throws InterruptedException, IOException {
        Integer redisPort = environment.getServicePort("redis", REDIS_PORT);
        String redisHost = environment.getServiceHost("redis", REDIS_PORT);

        System.out.println(redisPort);
        System.out.println(redisHost);
        Assertions.assertThat(redisPort).isGreaterThan(0);
        Assertions.assertThat(redisHost).isEqualTo("127.0.0.1");
    }
}
