package definition;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Message {

    private String message = "";
    private String topic;
    private String response;

    public boolean isOk() {
        return getResponse().contains("login");
    }
}
