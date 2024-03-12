package edu.gsu.eventplanner.event.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class ParticipantsConverter implements AttributeConverter<Set<Long>, String> {
    @Override
    public String convertToDatabaseColumn(Set<Long> attribute) {
        if(attribute == null || attribute.isEmpty()){
            return null;
        }

       return String.join(",",attribute.stream().map(String::valueOf).toList());
    }

    @Override
    public Set<Long> convertToEntityAttribute(String dbData) {
        if(dbData == null){
            return Set.of();
        }
        return  Arrays.stream(dbData.split(",")).map(Long::parseLong).collect(Collectors.toSet());
    }
}
