package support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class JsonUtil {

    public static boolean checkSchema(String textJson, String schemaFile){
        try {
            ObjectMapper mapperAux = new ObjectMapper();
            JsonNode json = mapperAux.readTree(textJson);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode schemaJson = mapper.readTree(Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream(schemaFile));

            final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            final JsonSchema schema = factory.getJsonSchema(schemaJson);
            ProcessingReport report = schema.validate(json);
            return report.isSuccess();
        } catch (Exception e) {
            return false;
        }
    }
}
