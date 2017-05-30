package serialize;

import akkaLabs.ExtremeAircrafts.http.PositionChangedHttpEntity;
import akkaLabs.ExtremeAircrafts.position.Position;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by ron-lenovo on 5/29/2017.
 */
public class PositionChangedHttpEntityDeserializer extends JsonDeserializer<PositionChangedHttpEntity> {

    @Override
    public PositionChangedHttpEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectNode node = p.getCodec().readTree(p);
        UUID id = UUID.fromString(node.get("aircraftId").asText());
        JsonNode positionNode = node.get("position");
        return new PositionChangedHttpEntity(id,new Position(positionNode.get("longitude").asDouble(),
                positionNode.get("latitude").asDouble(),
                positionNode.get("altitude").asDouble()));
    }
}
