package bo.gob.bdp.sam.bdp_credit_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "bo.gob.bdp.sam")
public class BdpCreditBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdpCreditBackendApplication.class, args);
	}

}
