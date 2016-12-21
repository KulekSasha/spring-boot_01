package com.nix.api.rest.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonComponent
public class JsonDate {

    private static final Logger log = LoggerFactory.getLogger(JsonDate.class);

    public static class Deserializer extends JsonDeserializer<Date> {
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        static {
            dateFormat.setLenient(false);
        }

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {

            Date birthday;
            try {
                synchronized (dateFormat) {
                    birthday = dateFormat.parse(p.getValueAsString("birthday"));
                }
            } catch (ParseException e) {
                log.error("cannot parse date: {}", p.getValueAsString("birthday"));
                birthday = null;
            }
            return birthday;
        }
    }

    public static class Serializer extends JsonSerializer<Date> {

        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException, JsonProcessingException {

            synchronized (dateFormat) {
                gen.writeString(dateFormat.format(value));
            }

        }
    }

}
