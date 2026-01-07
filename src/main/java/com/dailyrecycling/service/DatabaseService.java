package com.dailyrecycling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Get actor ID using policy number
     */
    public Long getActorIdByPolicyNumber(String policyNumber) {
        String sql = "SELECT id_actor FROM pcb.pcbqtaso WHERE policy_number = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, policyNumber);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get address details using actor ID
     */
    public Map<String, Object> getAddressByActorId(Long actorId) {
        String sql = "SELECT * FROM pcb.pcbqtadr WHERE id_actor = ?";
        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, actorId);
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get BCU number using actor ID
     */
    public String getBcuNumberByActorId(Long actorId) {
        String sql = "SELECT bcu_number FROM pcb.pcbqtaco WHERE id_actor = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, actorId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get gross amount sum for policy numbers
     */
    public Double getGrossAmountByPolicyNumber(String policyNumber) {
        String sql = "SELECT SUM(amount_gross) FROM pcb.pcbqtiny WHERE policy_number = ? GROUP BY policy_number";
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, policyNumber);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
