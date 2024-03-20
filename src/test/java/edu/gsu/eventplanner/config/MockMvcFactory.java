package edu.gsu.eventplanner.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MockMvcFactory {

    public static final String APP_DEV_DNS = "13.208.116.174";
    private static final ObjectMapper MOCK_MVC_MAPPER;

    static {
        MOCK_MVC_MAPPER = new ObjectMapper();

        var javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        javaTimeModule.addSerializer(ZonedDateTime.class,
                new ZonedDateTimeSerializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        javaTimeModule.addDeserializer(ZonedDateTime.class, InstantDeserializer.ZONED_DATE_TIME);


        MOCK_MVC_MAPPER.registerModules(javaTimeModule);

        MOCK_MVC_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MOCK_MVC_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static MockMvc getMockMvc(Object... controllers) {
        return getMockMvcBuilder(controllers).build();
    }

    public static MockMvc getRestDocsMockMvc(
            RestDocumentationContextProvider restDocumentationContextProvider,
            Object... controllers) {
        return getRestDocsMockMvc(restDocumentationContextProvider, APP_DEV_DNS, controllers);
    }

    public static MockMvc getRestDocsMockMvc(
            RestDocumentationContextProvider restDocumentationContextProvider,
            String host,
            Object... controllers) {
        var documentationConfigurer =
                MockMvcRestDocumentation.documentationConfiguration(
                        restDocumentationContextProvider);
        documentationConfigurer.uris().withScheme("http").withHost(host).withPort(8080);
        return getMockMvcBuilder(controllers).apply(documentationConfigurer).build();
    }

    private static StandaloneMockMvcBuilder getMockMvcBuilder(Object... controllers) {
        var conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new LocalDateTimeConverter());
        conversionService.addConverter(new LocalDateConverter());
        conversionService.addConverter(new LocalTimeConverter());

        return MockMvcBuilders.standaloneSetup(controllers)
                .setConversionService(conversionService)
                .setMessageConverters(
                        new MappingJackson2HttpMessageConverter(MOCK_MVC_MAPPER));
    }



    public static class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String source) {
            return LocalDateTime.parse(
                    source, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        }
    }

    public static class LocalDateConverter implements Converter<String, LocalDate> {

        @Override
        public LocalDate convert(String source) {
            return LocalDate.parse(
                    source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    public static class LocalTimeConverter implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String source) {
            return LocalTime.parse(
                    source, DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
    }

    public static <T> T readValue(String json, TypeReference<T> typeReference) {
        if (json == null || json.isBlank()) {
            return null;
        }

        try {
            return MOCK_MVC_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) {
            return null;
        }

        try {
            return MOCK_MVC_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T fromJsonFile(String resourceClasspath, Class<T> clazz) {
        if (resourceClasspath == null) {
            return null;
        }

        var classpathResource = new ClassPathResource(resourceClasspath);
        try {
            return MOCK_MVC_MAPPER.readValue(classpathResource.getInputStream(), clazz);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T fromJsonFile(String resourceClasspath, TypeReference<T> typeReference) {
        if (resourceClasspath == null) {
            return null;
        }

        var classpathResource = new ClassPathResource(resourceClasspath);
        try {
            return MOCK_MVC_MAPPER.readValue(classpathResource.getInputStream(), typeReference);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static String toPrettyJson(final Object object) {
        try {
            return MOCK_MVC_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            throw new JsonEncodeException(e);
        }
    }

    public static String writeAsStringFromJsonFile(String resourceClasspath) {
        if (resourceClasspath == null) {
            return null;
        }

        var classpathResource = new ClassPathResource(resourceClasspath);
        try {
            return toPrettyJson(MOCK_MVC_MAPPER.readTree(classpathResource.getFile()));
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static class JsonEncodeException extends RuntimeException {

        private static final long serialVersionUID = 4975703115049362769L;

        public JsonEncodeException(Throwable cause) {
            super(cause);
        }
    }

    public static class JsonDecodeException extends RuntimeException {

        private static final long serialVersionUID = -2651564042039413190L;

        public JsonDecodeException(Throwable cause) {
            super(cause);
        }
    }
}