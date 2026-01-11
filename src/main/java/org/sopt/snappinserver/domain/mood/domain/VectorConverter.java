package org.sopt.snappinserver.domain.mood.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.List;
import org.springframework.util.StringUtils;

@Converter
public class VectorConverter implements AttributeConverter<List<Float>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Float> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Vector conversion failed", e);
        }
    }

    @Override
    public List<Float> convertToEntityAttribute(String dbData) {
        if (!StringUtils.hasText(dbData)) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Float.class));
        } catch (IOException e) {
            throw new IllegalStateException("Vector conversion failed", e);
        }
    }
}
