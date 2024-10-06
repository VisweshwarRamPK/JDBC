package com.example.jdbc.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.util.Map;

public class JsonToXmlConverter {

    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    public JsonToXmlConverter() {
        this.objectMapper = new ObjectMapper();
        this.xmlMapper = new XmlMapper();
    }

    public String convertJsonToXml(String json) throws IOException {

        var map = objectMapper.readValue(json, Map.class);

        return xmlMapper.writeValueAsString(map);
    }

    public String convertXmlToJson(String xml) throws IOException {
        JsonNode jsonNode = xmlMapper.readTree(xml.getBytes());
        return new ObjectMapper().writeValueAsString(jsonNode);
    }
}
