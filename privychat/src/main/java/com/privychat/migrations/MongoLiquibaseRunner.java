package com.privychat.migrations;

import com.privychat.config.PrivyMongoProperties;
import liquibase.command.CommandScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClients;

/**
 * Runs Liquibase update against MongoDB on application startup using the liquibase-mongodb extension.
 * This bypasses SpringLiquibase (JDBC oriented) and directly issues the 'update' command.
 */
@Component
public class MongoLiquibaseRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(MongoLiquibaseRunner.class);

    private final PrivyMongoProperties mongoProps;

    public MongoLiquibaseRunner(PrivyMongoProperties mongoProps) {
        this.mongoProps = mongoProps;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!isEnabled()) {
            log.info("Liquibase MongoDB runner disabled by configuration.");
            return;
        }
        String changelogPath = System.getProperty("privychat.liquibase.changelog", "db/changelog/changelog.xml");
        try {
            if (!new ClassPathResource(changelogPath).exists()) {
                log.error("Liquibase changelog NOT found (path={}). Ensure file exists under src/main/resources/db/changelog/", changelogPath);
                return;
            }
            String uri = buildMongoUri();
            waitForMongo(uri, 5, 2000);
            log.info("Executing Liquibase MongoDB update (changelog={}, uri={})", changelogPath, uri);
            new CommandScope("update")
                    .addArgumentValue("changelogFile", changelogPath) // no classpath: prefix per liquibase-mongodb expectation
                    .addArgumentValue("searchPath", "classpath:,.")
                    .addArgumentValue("url", uri)
                    .execute();
            log.info("Liquibase MongoDB update completed.");
        } catch (Exception e) {
            log.error("Liquibase MongoDB update failed (changelog={})", changelogPath, e);
        }
    }

    private boolean isEnabled() {
        String env = System.getenv("PRIVYCHAT_LIQUIBASE_ENABLED");
        String prop = System.getProperty("privychat.liquibase.enabled");
        String val = prop != null ? prop : env;
        return val == null || !val.equalsIgnoreCase("false"); // default enabled
    }

    private void waitForMongo(String uri, int attempts, long sleepMs) {
        for (int i = 1; i <= attempts; i++) {
            try (var client = MongoClients.create(uri)) {
                client.getDatabase("admin").runCommand(new org.bson.Document("ping", 1));
                log.info("MongoDB reachable (attempt {}/{})", i, attempts);
                return;
            } catch (Exception e) {
                log.info("MongoDB not yet reachable (attempt {}/{}): {}", i, attempts, e.getMessage());
                try { Thread.sleep(sleepMs); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
            }
        }
        log.warn("Proceeding with Liquibase update even though MongoDB ping failed after {} attempts.", attempts);
    }

    private String buildMongoUri() {
        String uri = mongoProps.getUri();
        if (uri != null && !uri.isBlank()) {
            return uri;
        }
        String username = mongoProps.getUsername();
        String password = mongoProps.getPassword();
        String host = mongoProps.getHost();
        int port = mongoProps.getPort();
        String database = mongoProps.getDatabase() != null ? mongoProps.getDatabase() : "privychat";
        String authSource = mongoProps.getAuthSource() != null ? mongoProps.getAuthSource() : "admin";
        if (password != null) password = password.replace("@", "%40");
        return String.format("mongodb://%s:%s@%s:%d/%s?authSource=%s", username, password, host, port, database, authSource);
    }
}
