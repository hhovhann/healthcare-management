package am.hhovhann.healthcare_management;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestHealthcareManagementApplication {

    public static void main(String[] args) {
        SpringApplication.from(HealthcareManagementApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}