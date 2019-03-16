package com.ricardopassarella.nbrown.babyalbum;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.ricardopassarella.nbrown.babyalbum.dto.PictureMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class MetadataExtractor {

    PictureMetadata read(byte[] imageBytes) {
        try {
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(imageBytes);
            Metadata metadata = ImageMetadataReader.readMetadata(arrayInputStream);
            GpsDirectory gpsDir = metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if (gpsDir != null) {
                GeoLocation geoLocation = gpsDir.getGeoLocation();

                Date gpsDate = gpsDir.getGpsDate();
                LocalDateTime dateTime = LocalDateTime.ofInstant(gpsDate.toInstant(), ZoneId.systemDefault());

                return PictureMetadata.builder()
                        .longitude(BigDecimal.valueOf(geoLocation.getLongitude()))
                        .latitude(BigDecimal.valueOf(geoLocation.getLatitude()))
                        .localDateTime(dateTime)
                        .build();
            }
            return PictureMetadata.builder().build();
        } catch (ImageProcessingException | IOException e) {
            throw new RuntimeException("error reading file metadata", e);
        }

    }

}
