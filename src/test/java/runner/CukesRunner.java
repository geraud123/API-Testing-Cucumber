package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import myHooks.Hooks;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"step_def", "myHooks"},
        dryRun = true,
        plugin = {"json:target/cucumber-report/cucumber.json",
                "html:target/cucumber-reports.html",
                "rerun:target/rerun.txt"
        },
        tags = ""
)
public class CukesRunner {
}
