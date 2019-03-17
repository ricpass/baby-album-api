package com.ricardopassarella.nbrown.baby;

import com.ricardopassarella.nbrown.baby.dto.BabyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class BabyRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    Optional<BabyResponse> getBabyDetails(String clientId) {

        String sql = "SELECT * from baby where client_id = :clientId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("clientId", clientId);

        return jdbcTemplate.query(sql, params, findByIdExtractor());
    }

    private ResultSetExtractor<Optional<BabyResponse>> findByIdExtractor() {
        return rs -> {
            if (rs.next()) {
                return Optional.of(BabyResponse.builder()
                        .name(rs.getString("name"))
                        .gender(rs.getString("gender"))
                        .dateOfBirth(
                                Optional.ofNullable(rs.getDate("date_of_birth"))
                                        .map(Date::toLocalDate).orElse(null))
                        .build());
            }
            return Optional.empty();
        };
    }

    void insert(String clientId, BabyRequest babyRequest) {
        String sql = "INSERT INTO baby (id, name, gender, date_of_birth, client_id) " +
                " VALUES (:id, :name, :gender, :dateOfBirth, :clientId) ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", UUID.randomUUID().toString().replace("-", ""));
        params.addValue("clientId", clientId);
        params.addValue("name", babyRequest.getName());
        params.addValue("gender", babyRequest.getGender());
        params.addValue("dateOfBirth", babyRequest.getDateOfBirth());

        jdbcTemplate.update(sql, params);
    }

    boolean update(String clientId, BabyRequest babyRequest) {
        String sql = "UPDATE baby " +
                " SET name         = :name, " +
                "    gender        = :gender, " +
                "    date_of_birth = :dateOfBirth " +
                " WHERE client_id  = :clientId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", babyRequest.getName());
        params.addValue("gender", babyRequest.getGender());
        params.addValue("dateOfBirth", babyRequest.getDateOfBirth());
        params.addValue("clientId", clientId);

        return jdbcTemplate.update(sql, params) == 1;
    }
}
