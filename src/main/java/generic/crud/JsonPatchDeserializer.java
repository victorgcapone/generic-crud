package generic.crud;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import javax.inject.Singleton;
import javax.json.*;
import java.io.IOException;

@Singleton
public class JsonPatchDeserializer extends JsonDeserializer<JsonPatch> {

    @Override
    public JsonPatch deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ArrayNode node = p.readValueAsTree();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(JsonNode child : node) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("op", child.get("op").asText())
                    .add("path", child.get("path").asText())
                    .add("value", child.get("value").asText());
            arrayBuilder.add(objectBuilder.build());
        }
        JsonArray array = arrayBuilder.build();
        JsonPatch patch = Json.createPatch(array);
        return patch;
    }

}