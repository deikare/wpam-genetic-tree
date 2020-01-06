package com.example.gentree;

import java.util.HashMap;
import java.util.Map;

public class NodeData {
    private Map<String, Object> attributes;
    private Map<String, String> lifeEvents;

    public NodeData(Map<String, Object> attributes, Map<String, String> lifeEvents) {
        this.attributes = attributes;
        this.lifeEvents = lifeEvents;
    }

    public Map<String, Object> getWorkingNode() {
        Map<String, Object> result = new HashMap<>();

        result.put(attributes.getClass().getName(), attributes);
        result.put(lifeEvents.getClass().getName(), lifeEvents);

        return result;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getLifeEvents() {
        return lifeEvents;
    }

    public void setLifeEvents(Map<String, String> lifeEvents) {
        this.lifeEvents = lifeEvents;
    }
}
