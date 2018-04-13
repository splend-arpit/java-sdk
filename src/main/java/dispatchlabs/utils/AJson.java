package dispatchlabs.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
public abstract class AJson implements Serializable {

    /**
     * Class level-declarations.
     */
    @JsonIgnore
    private final List<String> omitted = new ArrayList<>();

    /**
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    public static JsonNode toJsonNode(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(jsonString);
        return mapper.readTree(parser);
    }

    /**
     *
     * @param omit
     */
    public void omit(String omit) {
        omitted.add(omit);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            return objectMapper.writer(new SimpleFilterProvider().addFilter("AJsonFilter", SimpleBeanPropertyFilter.serializeAllExcept(new HashSet<>(omitted)))).writeValueAsString(this);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            return objectMapper.writer(new SimpleFilterProvider()).writeValueAsString(object);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     *
     * @return
     */
    public String toPrettyString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            return objectMapper.writer(new SimpleFilterProvider().addFilter("AJsonFilter", SimpleBeanPropertyFilter.serializeAllExcept(new HashSet<>(omitted)))).withDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     *
     * @param theClass
     * @param json
     * @return
     * @throws java.lang.Exception
     */
    public static AJson deserialize(Class theClass, String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (AJson) objectMapper.readValue(json, theClass);
    }

    /**
     *
     * @param theClass
     * @param json
     * @return
     * @throws Exception
     */
    public static List deserializeList(Class theClass, String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, theClass);
        return objectMapper.readValue(json, javaType);
    }

    /**
     *
     * @param json
     * @return
     * @throws Exception
     */
    public static Map deserializeMap(String json) throws Exception {
        return new ObjectMapper().readValue(json, HashMap.class);
    }
}
