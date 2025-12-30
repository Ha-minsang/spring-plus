package org.example.expert;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class UserDataLoadTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Rollback(false)
    public void testBulkInsertUsers() {
        int totalCount = 5000000;
        int batchSize = 10000;
        String sql = "INSERT INTO users (email, password, nickname, user_role) VALUES (?, ?, ?, ?)";
        for (int i = 0; i < totalCount; i += batchSize) {
            final int start = i;
            final int end = Math.min(start + batchSize, totalCount);

            List<Object[]> batchArgs = new ArrayList<>();
            for (int j = start; j < end; j++) {
                String email = "user" + j + "@test.com";
                String password = "password123";
                String nickname = UUID.randomUUID().toString();

                batchArgs.add(new Object[]{email, password, nickname, "USER"});
            }

            jdbcTemplate.batchUpdate(sql, batchArgs);
        }
    }
}

