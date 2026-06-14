package bo.gob.bdp.sam.bdp_credit_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "bo.gob.bdp.sam")
@EntityScan(basePackages = "bo.gob.bdp.sam.adapters.out.persistence")
@EnableJpaRepositories(basePackages = "bo.gob.bdp.sam.adapters.out.persistence")
public class BdpCreditBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdpCreditBackendApplication.class, args);
	}

}
