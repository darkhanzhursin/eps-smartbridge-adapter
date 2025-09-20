package kz.ezdrav.eps_smartbridge_adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("kz.ezdrav.eps_smartbridge_adapter.entity")
@EnableJpaRepositories(basePackages = "kz.ezdrav.eps_smartbridge_adapter.repository")
public class EpsSmartbridgeAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpsSmartbridgeAdapterApplication.class, args);
	}

}
