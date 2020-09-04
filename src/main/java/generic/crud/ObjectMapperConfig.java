package generic.crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;

import javax.json.JsonPatch;

@Factory
public class ObjectMapperConfig {

    @Context
    public ObjectMapper objectMapper(JsonPatchDeserializer deserializer){
        SimpleModule customDeserializerModule = new SimpleModule();
        customDeserializerModule.addDeserializer(JsonPatch.class, deserializer);
        return new ObjectMapper()
                .registerModule(customDeserializerModule)
                .registerModule(new JSR353Module())
                .findAndRegisterModules();
    }

}
