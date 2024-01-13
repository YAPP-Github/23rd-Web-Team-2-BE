package com.baro.common.data;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JpaDataCleaner {

    private static final String TRUNCATE_FORMAT = "TRUNCATE TABLE %s";

    private final List<String> tableNames = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @PostConstruct
    void findDatabaseTableNames() {
        List<Object[]> tableInfos = entityManager.createNativeQuery("SHOW TABLES").getResultList();
        for (Object[] tableInfo : tableInfos) {
            String tableName = (String) tableInfo[0];
            tableNames.add(tableName);
        }
    }

    @Transactional
    public void cleanUp() {
        String truncateQuery = tableNames.stream()
                .map(tableName -> String.format(TRUNCATE_FORMAT, tableName))
                .collect(Collectors.joining(";"));

        entityManager.createNativeQuery(truncateQuery).executeUpdate();
    }
}
