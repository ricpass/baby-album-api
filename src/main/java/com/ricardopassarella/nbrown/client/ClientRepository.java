package com.ricardopassarella.nbrown.client;

import com.ricardopassarella.nbrown.client.dto.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;

@Repository
@RequiredArgsConstructor(access = PACKAGE)
class ClientRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    Optional<ClientResponse> findById(String id) {
        String sql = "SELECT * from client where id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbcTemplate.query(sql, params, findByIdExtractor());
    }

    private ResultSetExtractor<Optional<ClientResponse>> findByIdExtractor() {
        return rs -> {
            if (rs.next()) {
                return Optional.ofNullable(rs.getString("id"))
                        .map(ClientResponse::new);
            }
            return Optional.empty();
        };
    }

}
