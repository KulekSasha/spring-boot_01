package com.nix.api.rest.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.nix.model.Role;
import com.nix.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class JsonRole {

    public static class Serializer extends JsonSerializer<Role> {
        @Override
        public void serialize(Role value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException, JsonProcessingException {
            gen.writeString(value.getName());
        }
    }

    public static class Deserializer extends JsonDeserializer<Role> {

        @Autowired
        private RoleService roleService;

        @Override
        public Role deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return roleService.findByName(p.getValueAsString("role"));
        }
    }
}
