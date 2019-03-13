package com.ricardopassarella.nbrown.infrastructure.db;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .locations("filesystem:src/main/resources/db/migration/common/").load();

        // TODO change location for classpath if env != local

        log.info("Running flyway migration");
        flyway.migrate();

        return flyway;
    }
}
