package owner.kitchencompliance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KitchenComplianceHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(KitchenComplianceHubApplication.class, args);
	}

}
