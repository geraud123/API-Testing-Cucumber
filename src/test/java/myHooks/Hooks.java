package myHooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import utilities.Driver;

public class Hooks {


    @Before
    public void setup(){
        System.out.println("launch Simple Books API");
    }

    @After
    public void tearDown(){
        //Driver.closeDriver();
        System.out.println("close the Driver");
    }
}
