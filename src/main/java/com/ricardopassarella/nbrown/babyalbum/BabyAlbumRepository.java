package com.ricardopassarella.nbrown.babyalbum;

import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumDto;
import com.ricardopassarella.nbrown.babyalbum.dto.PictureMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BabyAlbumRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    String insert(String clientId, PictureMetadata pictureMetadata) {
        String imageId = createId();

        String sql = "INSERT INTO baby_image (id, latitude, longitude, image_datetime, client_id) " +
                " VALUES (:id, :latitude, :longitude, :imageDatetime, :clientId) ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", imageId);
        params.addValue("latitude", pictureMetadata.getLatitude());
        params.addValue("longitude", pictureMetadata.getLongitude());
        params.addValue("imageDatetime", pictureMetadata.getLocalDateTime());
        params.addValue("clientId", clientId);

        jdbcTemplate.update(sql, params);

        return imageId;
    }

    private String createId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    Optional<BabyAlbumDto> findImageById(String imageId, String clientId) {
        String sql = "select b.* " +
                " from baby_image b " +
                " where " +
                " id = :imageId " +
                " and client_id = :clientId ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("imageId", imageId);
        params.addValue("clientId", clientId);

        return jdbcTemplate.query(sql, params, rs -> {
            if (rs.next()) {
                return Optional.of(createDto(rs));
            }
            return Optional.empty();
        });
    }

    List<BabyAlbumDto> findImagesByClientId(String clientId) {
        String sql = "select b.* " +
                " from baby_image b " +
                " where " +
                "  client_id = :clientId ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("clientId", clientId);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> createDto(rs));
    }

    private BabyAlbumDto createDto(ResultSet rs) throws SQLException {
        return BabyAlbumDto.builder()
                .id(rs.getString("id"))
                .latitude(rs.getBigDecimal("latitude"))
                .longitude(rs.getBigDecimal("longitude"))
                .dateTime(rs.getObject("image_datetime", LocalDateTime.class))
                .clientId(rs.getString("client_id"))
                .build();
    }

    public boolean deleteImageEntry(String imageId, String clientId) {
        String sql = "delete " +
                " from baby_image " +
                " where id = :imageId " +
                "  and client_id = :clientId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("imageId", imageId);
        params.addValue("clientId", clientId);

        return jdbcTemplate.update(sql, params) == 1;
    }
}
