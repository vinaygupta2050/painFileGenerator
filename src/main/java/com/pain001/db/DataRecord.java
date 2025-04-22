package com.pain001.db;

import java.util.HashMap;
import java.util.Map;

public class DataRecord {
    private final Map<String, Object> fields = new HashMap<>();

    public void addField(String key, Object value) {
        fields.put(key, value);
    }

    public Object getField(String key) {
        return fields.get(key);
    }

    public Map<String, Object> getAllFields() {
        return fields;
    }

    @Override
    public String toString() {
        return fields.toString();
    }
}