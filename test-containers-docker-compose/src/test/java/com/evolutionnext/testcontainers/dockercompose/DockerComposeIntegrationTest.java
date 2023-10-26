package com.evolutionnext.testcontainers.dockercompose;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
public class DockerComposeIntegrationTest {

    private static final int REDIS_PORT = 6379;
    @Container
    public static DockerComposeContainer environment =
        new DockerComposeContainer(new File("test-containers-docker-compose/src/main/resources/docker-compose.yaml"))
            .withExposedService("redis", REDIS_PORT);

    @Test
    void testDockerCompose() throws InterruptedException {
        Integer redisPort = environment.getServicePort("redis", REDIS_PORT);
        String redisHost = environment.getServiceHost("redis", REDIS_PORT);

        System.out.println(redisPort);
        System.out.println(redisHost);
        Assertions.assertThat(redisPort).isGreaterThan(0);
        Assertions.assertThat(redisHost).isEqualTo("127.0.0.1");
    }
}
