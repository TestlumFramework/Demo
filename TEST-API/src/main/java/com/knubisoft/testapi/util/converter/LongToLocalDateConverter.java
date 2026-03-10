package com.knubisoft.testapi.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@ReadingConverter
public class LongToLocalDateConverter implements Converter<Long, LocalDateTime> {

    @Override
    public LocalDateTime convert(Long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.UTC);
    }
}
