package com.knubisoft.testapi.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@WritingConverter
public class LocalDateToLongConverter implements Converter<LocalDateTime, Long> {

    @Override
    public Long convert(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
