package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class Driver {

    private static final InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    public static WebDriver getDriver() {


        if (driverPool.get() == null) {

            String browserType = ConfigReader.getProperty("browser");
            System.setProperty("webdriver.http.factory", "jdk-http-client");
            String chromeDriverPath = "C:\\Tools\\chrome\\chromedriver.exe";
            String firefoxDriverPath = "C:\\Tools\\firefox\\geckodriver.exe";
            String edgeDriverPath = "C:\\Tools\\edge\\msedgedriver.exe";
            ChromeOptions chromeOptions = new ChromeOptions();


            switch (browserType) {
                case "chrome":
                    System.setProperty("webdriver.chrome.driver", chromeDriverPath);
                    driverPool.set(new ChromeDriver(chromeOptions.addArguments("--remote-allow-origins=*")));
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;

                case "chrome-headless":
                    System.setProperty("webdriver.chrome.driver", chromeDriverPath);
                    driverPool.set(new ChromeDriver(new ChromeOptions().addArguments("--headless","--disable-gpu")));
                    break;

                case "firefox":
                    System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
                    driverPool.set(new FirefoxDriver());
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;

                case "edge":
                    System.setProperty("webdriver.edge.driver", edgeDriverPath);
                    driverPool.set(new EdgeDriver());
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;


            }

        }

        return driverPool.get();

    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }

}
