package definition;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {

    private String login;
    private String name;
    private String phone;

    public String getField(String field) {
        String res = "";
        switch (field){
            case "login": res = getLogin(); break;
            case "name": res = getName();break;
            case "phone": res = getPhone();break;
        }
        return res;
    }
}
