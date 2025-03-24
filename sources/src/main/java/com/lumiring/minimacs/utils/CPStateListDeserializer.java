package com.lumiring.minimacs.utils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.lumiring.minimacs.domain.dto.raidAlertRemote.CPStateList;

import java.io.IOException;

public class CPStateListDeserializer extends StdDeserializer<CPStateList> {

    public CPStateListDeserializer() {
        this(null);
    }

    public CPStateListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CPStateList deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode root = mapper.readTree(jp);

        JsonNode infoNode = root.get("info");

        return mapper.treeToValue(infoNode, CPStateList.class); // Десериализуем в CPStateList
    }
}

