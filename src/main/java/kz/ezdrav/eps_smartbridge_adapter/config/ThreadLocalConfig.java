package kz.ezdrav.eps_smartbridge_adapter.config;

import kz.ezdrav.eps_smartbridge_adapter.model.dto.internal.ShepServiceEventParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadLocalConfig {
    @Bean
    public ThreadLocal<ShepServiceEventParams> shepServiceEventParamsHolder() {
        return ThreadLocal.withInitial(ShepServiceEventParams::new);
    }
}
