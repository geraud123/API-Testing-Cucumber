package myHooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ConfigReader;

public class Hooks {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);
    private static final String BASE_URI = ConfigReader.getProperty("baseURI");

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @After
    public void tearDown() {
        //Driver.closeDriver();
        LOG.info("close the Driver");
    }
}
