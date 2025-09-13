package kz.ezdrav.eps_smartbridge_adapter.config;

import java.util.List;
import javax.xml.ws.Endpoint;
import kz.ezdrav.eps_smartbridge_adapter.webservice.endpoint.EpsEndpoint;
import kz.ezdrav.eps_smartbridge_adapter.webservice.handler.WsSecurityMessageHandler;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebServiceConfig {

    private final Bus bus;
    private final EpsEndpoint epsEndpoint;
    private final WsSecurityMessageHandler wsSecurityMessageHandler;

    /**
     * Общая конфигурация логирования CXF.
     */
    @Bean
    public LoggingFeature cxfLoggingFeature() {
        LoggingFeature logFeat = new LoggingFeature();
        logFeat.setPrettyLogging(true);
        logFeat.setLimit(16 * 1024);        // 16 KB
        logFeat.setInMemThreshold(64 * 1024);
        return logFeat;
    }

    /**
     * Внешняя SOAP-endpoint с WS-Security.
     */
    @Bean
    public Endpoint externalOncoFormEndpoint(LoggingFeature loggingFeature) {
        return buildEndpoint("/eps-service", true, loggingFeature);
    }

    /**
     * Внутренняя SOAP-endpoint без WS-Security.
     */
    @Bean
    public Endpoint internalOncoFormEndpoint(LoggingFeature loggingFeature) {
        return buildEndpoint("/internal/eps-service", false, loggingFeature);
    }

    /**
     * Процесс обработки запросов SOAP Endpoint.
     */
    private Endpoint buildEndpoint(String address, boolean withWsSecurity, LoggingFeature loggingFeature) {
        EndpointImpl endpoint = new EndpointImpl(bus, epsEndpoint);
        endpoint.getFeatures().add(loggingFeature);

        if (withWsSecurity) {
            endpoint.setHandlers(List.of(wsSecurityMessageHandler));
        }
        endpoint.publish(address);
        return endpoint;
    }

}