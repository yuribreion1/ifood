package org.github;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

public class CadastroTestLifecycleManager implements QuarkusTestResourceLifecycleManager {

    public static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres");
    @Override
    public Map<String, String> start() {
        POSTGRE_SQL_CONTAINER.start();
        Map<String, String> propriedades = new HashMap<>();

        propriedades.put("quarkus.datasource.db-kind","postgresql");
        propriedades.put("quarkus.datasource.username", POSTGRE_SQL_CONTAINER.getUsername());
        propriedades.put("quarkus.datasource.password", POSTGRE_SQL_CONTAINER.getPassword());
        propriedades.put("quarkus.datasource.jdbc.url", POSTGRE_SQL_CONTAINER.getJdbcUrl());

        return propriedades;
    }

    @Override
    public void stop() {
        if (POSTGRE_SQL_CONTAINER.isRunning()) POSTGRE_SQL_CONTAINER.stop();
    }
}
