package com.evolutionnext.testcontainers.generic;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class GenericIntegrationTest {

    @Container
    public static GenericContainer<?> alpine = new GenericContainer<>(DockerImageName.parse("nginx:1.25.3"))
        .withExposedPorts(80);

    @Test
    void testGenericCalls() throws InterruptedException {
        String url = String.format("http://localhost:%s", alpine.getFirstMappedPort());
        System.out.println(url);
        String result = RestAssured.when().get(url).asString();
        Assertions.assertThat(result).contains("Thank you for using nginx.");
    }
}
