package steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;


import java.net.MalformedURLException;

public class Hooks {
    public static Scenario scenario;

    public Hooks() throws MalformedURLException{

    }

    @Before
    public void beforeScenario(Scenario sc){
        Hooks.scenario = sc;
    }

    @After
    public void afterScenario(Scenario sc){

    }

}
