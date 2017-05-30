package serialize;

import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpEntity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Created by ron-lenovo on 5/29/2017.
 */
public class HttpEntitySerializer extends JsonDeserializer<HttpEntity.Strict> {

    @Override
    public HttpEntity.Strict deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//TODO        HttpEntity.Strict ret = HttpEntities.create()
        return null;
    }
}
