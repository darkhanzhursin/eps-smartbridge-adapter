package kz.ezdrav.eps_smartbridge_adapter.webservice.endpoint.impl;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.xml.soap.SOAPMessage;
import kz.ezdrav.eh.shep.syncchannel.v10.interfaces.SendMessageSendMessageFaultMsg;
import kz.ezdrav.eh.shep.syncchannel.v10.types.request.SyncSendMessageRequest;
import kz.ezdrav.eh.shep.syncchannel.v10.types.response.SyncSendMessageResponse;
import kz.ezdrav.eh.shep.util.ShepUtil;
import kz.ezdrav.eps_smartbridge_adapter.annotation.SaveShepServiceEvent;
import kz.ezdrav.eps_smartbridge_adapter.exception.GeneralException;
import kz.ezdrav.eps_smartbridge_adapter.exception.UnsupportedRequestTypeException;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.EpsRequest;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.common.GeneralInfoResponse;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.*;
import kz.ezdrav.eps_smartbridge_adapter.webservice.endpoint.EpsEndpoint;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.EpsSoapClient;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilderFactory;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@WebService(serviceName = "EpsService",
    targetNamespace = "http://bip.bee.kz/SyncChannel/v10/Interfaces",
    endpointInterface = "kz.ezdrav.eps_smartbridge_adapter.webservice.endpoint.EpsEndpoint")
@Component
@RequiredArgsConstructor
@Slf4j
public class EpsEndpointImpl implements EpsEndpoint {

    private final SoapMessageBuilderFactory builderFactory;
    private final SoapMessageFactory messageFactory;
    private final EpsSoapClient soapClient;

    private static final String EPS_SERVICE_URL = "https://app01.ezdrav.kz/appwais/ws/ws1.1cws";
    private static final Logger log = LoggerFactory.getLogger(EpsEndpointImpl.class);

    @Value("${eps.ssl.trust-all:false}")
    private boolean trustAllCertificates;

    @Value("${eps.ssl.truststore:#{null}}")
    private Resource trustStore;

    @Value("${eps.ssl.truststore-password:changeit}")
    private String trustStorePassword;

    @Value("${eps.ssl.truststore-type:JKS}")
    private String trustStoreType;

    @Value("${eps.ssl.verify-hostname:true}")
    private boolean verifyHostname;

    @PostConstruct
    public void configureSsl() {
        try {
            if (trustAllCertificates) {
                log.warn("SSL verification is disabled. This should only be used in development!");
                configureTrustAllCertificates();
            } else if (trustStore != null && trustStore.exists()) {
                log.info("Configuring SSL with custom truststore: {}", trustStore.getFilename());
                configureCustomTrustStore();
            } else {
                log.info("Using default JVM SSL configuration");
                configureSystemTrustStore();
            }

            if (!verifyHostname) {
                log.warn("Hostname verification is disabled. This reduces security!");
                HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            }
        } catch (Exception e) {
            log.error("Failed to configure SSL", e);
            throw new RuntimeException("SSL configuration failed", e);
        }
    }

    private void configureSystemTrustStore() {
        String trustStorePath = System.getProperty("javax.net.ssl.trustStore");
        if (trustStorePath != null) {
            log.info("Using system truststore: {}", trustStorePath);
        }
    }

    private void configureCustomTrustStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(trustStoreType);
        keyStore.load(trustStore.getInputStream(), trustStorePassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        log.info("SSL configured with custom truststore containing {} certificates",
            keyStore.size());
    }

    private void configureTrustAllCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    if (log.isDebugEnabled() && certs != null && certs.length > 0) {
                        log.debug("Trusting server certificate: CN={}",
                            certs[0].getSubjectDN().getName());
                    }
                }
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

    @Override
    @SaveShepServiceEvent
    public SyncSendMessageResponse sendMessage(SyncSendMessageRequest request) throws SendMessageSendMessageFaultMsg {
        log.info("Received SOAP request");
        return handleRequest(request);
    }

    private SyncSendMessageResponse handleRequest(SyncSendMessageRequest request) {
        log.debug("Handling request");
        Object data = request.getRequestData().getData();

        try {
            if (data instanceof EpsRequest) {
                return processEpsRequest(data);
            }
        } catch (GeneralException e) {
            return ShepUtil.buildErrorSyncSendMessageResponse(GeneralInfoResponse.buildError(e));
        } catch (Exception e) {
            log.error("Error occurred while processing request: ", e);
            return ShepUtil.buildErrorSyncSendMessageResponse(GeneralInfoResponse.buildError(e));
        }

        return ShepUtil.buildErrorSyncSendMessageResponse(
            GeneralInfoResponse.buildError("Данный тип запроса не поддерживается")
        );
    }

    private SyncSendMessageResponse processEpsRequest(Object data) throws Exception {
        Class<?> dataType = data.getClass();

        if (!builderFactory.supports(dataType)) {
            throw new UnsupportedRequestTypeException(
                    "Unsupported request type: " + dataType.getSimpleName()
            );
        }

        SoapMessageBuilder builder = builderFactory.getBuilder(dataType);
        SOAPMessage msg = messageFactory.createMessage(data, builder);
        String response = soapClient.sendRequest(msg, EPS_SERVICE_URL);

        return ShepUtil.buildSuccessSyncSendMessageResponse(response);
    }
}