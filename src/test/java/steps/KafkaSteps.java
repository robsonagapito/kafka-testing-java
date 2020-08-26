package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.SchemaLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import definition.Message;
import definition.User;
import definition.UserError;
import gherkin.deps.com.google.gson.Gson;
import org.junit.Assert;
import support.JsonUtil;
import support.Kafka;

import java.io.IOException;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class KafkaSteps {

    private Kafka kafka = new Kafka();
    private Message message = new Message();
    private UserError userError = new UserError();
    private User user = new User();

    @Given("^kafka server uses the topic \"([^\"]*)\"$")
    public void kafkaServerUsesTheTopic(String topic) throws Throwable {
        message.setTopic(topic);
    }

    @And("^the message is \"([^\"]*)\"$")
    public void theMessageIs(String pMessage) throws Throwable {
        message.setMessage(pMessage);
    }

    @And("^the message is produced$")
    public void theMessageIsProduced() {
        kafka.sendMessage(message.getMessage(),message.getTopic());
    }

    @And("^the application works for \"([^\"]*)\" seconds$")
    public void theApplicationWorksForSeconds(int sec) throws Throwable {
        Thread.sleep(sec * 1000);
    }

    @And("^the message is consumed$")
    public void theMessageIsConsumed() {
        Gson g = new Gson();
        message.setResponse(kafka.getLastMessage(message.getTopic()));
        if (message.isOk()){
            user = g.fromJson(message.getResponse(), User.class);
        } else {
            userError = g.fromJson(message.getResponse(), UserError.class);
        }
    }

    @Then("^the message has a field \"([^\"]*)\" with value \"([^\"]*)\"$")
    public void theMessageHasAFieldWithValue(String field, String value) throws Throwable {
        Assert.assertEquals(value, user.getField(field));
    }

    @Then("^the message error has a field \"([^\"]*)\" with value \"([^\"]*)\"$")
    public void theMessageErrorHasAFieldWithValue(String field, String value) throws Throwable {
        Assert.assertEquals(value, userError.getMessage());
    }

    @Then("^the contract is ok$")
    public void theContractIsOk() throws IOException, ProcessingException {
        Boolean result = JsonUtil.checkSchema(message.getResponse(),"schemas/users-schema.json");
        Assert.assertTrue("Returned Health JSON does not validate against the specification schema", result);
    }
}
