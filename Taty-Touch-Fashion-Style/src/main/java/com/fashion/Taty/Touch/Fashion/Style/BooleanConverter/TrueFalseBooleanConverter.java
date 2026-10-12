package com.fashion.Taty.Touch.Fashion.Style.BooleanConverter;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;



@Converter
public class TrueFalseBooleanConverter implements AttributeConverter<Boolean, String> {


    @Override
    public String convertToDatabaseColumn(Boolean value) {
        if (value == null) return null;
        return value ? "true" : "false";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbValue) {

        if (dbValue == null) return null;
        return Boolean.parseBoolean(dbValue);
    }
}
