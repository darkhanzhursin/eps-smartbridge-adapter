package kz.ezdrav.eps_smartbridge_adapter.webservice.endpoint.impl;

import java.io.StringWriter;
import java.net.URL;
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
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import kz.ezdrav.eh.shep.syncchannel.v10.interfaces.SendMessageSendMessageFaultMsg;
import kz.ezdrav.eh.shep.syncchannel.v10.types.request.SyncSendMessageRequest;
import kz.ezdrav.eh.shep.syncchannel.v10.types.response.SyncSendMessageResponse;
import kz.ezdrav.eh.shep.util.ShepUtil;
import kz.ezdrav.eps_smartbridge_adapter.annotation.SaveShepServiceEvent;
import kz.ezdrav.eps_smartbridge_adapter.exception.GeneralException;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.EpsRequest;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.common.GeneralInfoResponse;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.*;
import kz.ezdrav.eps_smartbridge_adapter.webservice.endpoint.EpsEndpoint;
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

    private static final String EPS_SERVICE_URL = "https://app01.ezdrav.kz/appwais/ws/ws1.1cws";
    private static final String KAYSAT_NAMESPACE = "http://www.kaysat-ps.org";
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
                return handleEpsRequest(data);
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

    private SyncSendMessageResponse handleEpsRequest(Object data) {
        log.debug("Processing EPS request of type: {}", data.getClass().getSimpleName());

        try {
            SOAPMessage soapMessage = createSoapMessage(data);
            String response = sendSoapRequest(soapMessage);

            log.debug("Received response from EPS service: {}", response);
            return ShepUtil.buildSuccessSyncSendMessageResponse(response);
        } catch (Exception e) {
            log.error("Error processing EPS request", e);
            return ShepUtil.buildErrorSyncSendMessageResponse(
                GeneralInfoResponse.buildError("Ошибка при обработке запроса: " + e.getMessage())
            );
        }
    }

    private SOAPMessage createSoapMessage(Object data) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage soapMessage = messageFactory.createMessage();

        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();

        envelope.addNamespaceDeclaration("kay", KAYSAT_NAMESPACE);

        SOAPBody soapBody = envelope.getBody();

        if (data instanceof GetReferral) {
            createGetReferralElement(soapBody, (GetReferral) data);
        } else if (data instanceof DefectElement) {
            createDefectElement(soapBody, (DefectElement) data);
        } else if (data instanceof ChekList) {
            createChekListElement(soapBody, (ChekList) data);
        } else if (data instanceof GetRefferalByPeriod) {
            createGetRefferalByPeriodElement(soapBody, (GetRefferalByPeriod) data);
        } else if (data instanceof SetData) {
            System.out.println("it is SetData");
        } else if (data instanceof SetReferral) {
            System.out.println("it is SetReferral");
        } else if (data instanceof GetRefferalByPerson) {
            System.out.println("it is GetRefferalByPerson");
        } else if (data instanceof GetRefferalByID) {
            System.out.println("it is GetRefferalByID");
        } else if (data instanceof FinanceSource) {
            System.out.println("it is FinanceSource");
        } else if (data instanceof GetIdByDate) {
            System.out.println("it is GetIdByDate");
        } else if (data instanceof GetDataByIDs) {
            System.out.println("it is GetDataByIDs");
        } else {
            throw new Exception("Unsupported request type: " + data.getClass().getSimpleName());
        }

        soapMessage.saveChanges();
        return soapMessage;
    }

    private void createGetRefferalByPeriodElement(SOAPBody soapBody, GetRefferalByPeriod getRefferalByPeriod) throws SOAPException {
        SOAPElement getReferralByPeriodElement = soapBody.addChildElement("GetRefferalByPeriod", "kay");

        if (getRefferalByPeriod.getParams() != null) {
            // Create Params wrapper element
            SOAPElement paramsElement = getReferralByPeriodElement.addChildElement("Params", "kay");

            if (getRefferalByPeriod.getParams().getDateBegin() != null) {
                SOAPElement dateBegin = paramsElement.addChildElement("DateBegin", "kay");
                dateBegin.addTextNode(getRefferalByPeriod.getParams().getDateBegin().toString());
            }

            if (getRefferalByPeriod.getParams().getDateEnd() != null) {
                SOAPElement dateEnd = paramsElement.addChildElement("DateEnd", "kay");
                dateEnd.addTextNode(getRefferalByPeriod.getParams().getDateEnd().toString());
            }

            if (getRefferalByPeriod.getParams().getPageNo() > 0) {
                SOAPElement pageNo = paramsElement.addChildElement("PageNo", "kay");
                pageNo.addTextNode(String.valueOf(getRefferalByPeriod.getParams().getPageNo()));
            }

            if (getRefferalByPeriod.getParams().getPageSize() > 0) {
                SOAPElement pageSize = paramsElement.addChildElement("PageSize", "kay");
                pageSize.addTextNode(String.valueOf(getRefferalByPeriod.getParams().getPageSize()));
            }
        }

        if (getRefferalByPeriod.getToken() != null) {
            SOAPElement tokenElement = getReferralByPeriodElement.addChildElement("Token", "kay");
            tokenElement.addTextNode(getRefferalByPeriod.getToken());
        }
    }

    private void createGetReferralElement(SOAPBody soapBody, GetReferral getReferral) throws SOAPException {
        SOAPElement getReferralElement = soapBody.addChildElement("GetReferral", "kay");

        if (getReferral.getDirectedMoId() != null) {
            SOAPElement directedMoIdElement = getReferralElement.addChildElement("directed_mo_id", "kay");
            directedMoIdElement.addTextNode(getReferral.getDirectedMoId().toString());
        }

        if (getReferral.getDate() != null) {
            SOAPElement dateElement = getReferralElement.addChildElement("date", "kay");
            dateElement.addTextNode(getReferral.getDate().toString());
        }

        if (getReferral.getToken() != null) {
            SOAPElement tokenElement = getReferralElement.addChildElement("Token", "kay");
            tokenElement.addTextNode(getReferral.getToken());
        }
    }

    private void createDefectElement(SOAPBody soapBody, DefectElement defectElement) throws SOAPException {
        SOAPElement defectElementSoap = soapBody.addChildElement("DefectElement", "kay");

        if (defectElement.getMisId() != null) {
            SOAPElement misIdElement = defectElementSoap.addChildElement("mis_id", "kay");
            misIdElement.addTextNode(defectElement.getMisId().toString());
        }

        if (defectElement.getId() != null) {
            SOAPElement idElement = defectElementSoap.addChildElement("id", "kay");
            idElement.addTextNode(defectElement.getId().toString());
        }

        if (defectElement.getDefectId() != null) {
            SOAPElement defectIdElement = defectElementSoap.addChildElement("defect_id", "kay");
            defectIdElement.addTextNode(defectElement.getDefectId());
        }

        if (defectElement.getDateDefect() != null) {
            SOAPElement dateDefectElement = defectElementSoap.addChildElement("date_defect", "kay");
            dateDefectElement.addTextNode(defectElement.getDateDefect().toString());
        }

        if (defectElement.getAct() != null) {
            SOAPElement actElement = defectElementSoap.addChildElement("act", "kay");
            actElement.addTextNode(defectElement.getAct());
        }

        SOAPElement monitoringElement = defectElementSoap.addChildElement("monitoring", "kay");
        monitoringElement.addTextNode(String.valueOf(defectElement.getMonitoring()));

        SOAPElement countElement = defectElementSoap.addChildElement("count", "kay");
        countElement.addTextNode(String.valueOf(defectElement.getCount()));

        if (defectElement.getExpertId() != null) {
            SOAPElement expertIdElement = defectElementSoap.addChildElement("expert_id", "kay");
            expertIdElement.addTextNode(defectElement.getExpertId().toString());
        }

        SOAPElement vidDefectElement = defectElementSoap.addChildElement("vid_defect", "kay");
        vidDefectElement.addTextNode(String.valueOf(defectElement.getVidDefect()));
    }

    private void createChekListElement(SOAPBody soapBody, ChekList chekList) throws SOAPException {
        SOAPElement chekListElement = soapBody.addChildElement("ChekList", "kay");

        if (chekList.getScriningId() != null) {
            SOAPElement scriningIdElement = chekListElement.addChildElement("scrining_id", "kay");
            scriningIdElement.addTextNode(chekList.getScriningId());
        }

        if (chekList.getChekId() != null) {
            SOAPElement chekIdElement = chekListElement.addChildElement("chek_id", "kay");
            chekIdElement.addTextNode(chekList.getChekId());
        }

        SOAPElement chekValueElement = chekListElement.addChildElement("chek_value", "kay");
        chekValueElement.addTextNode(String.valueOf(chekList.getChekValue()));
    }

    private String sendSoapRequest(SOAPMessage soapMessage) throws Exception {
        log.debug("Sending SOAP request to: {}", EPS_SERVICE_URL);

        logSoapMessage("Outgoing SOAP Request", soapMessage);

        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        try {
            URL endpoint = new URL(EPS_SERVICE_URL);
            SOAPMessage response = soapConnection.call(soapMessage, EPS_SERVICE_URL);

            logSoapMessage("Incoming SOAP Response", response);

            return soapMessageToString(response);

        } finally {
            soapConnection.close();
        }
    }

    private void logSoapMessage(String title, SOAPMessage message) {
        if (log.isDebugEnabled()) {
            try {
                String messageStr = soapMessageToString(message);
                log.debug("{}: {}", title, messageStr);
            } catch (Exception e) {
                log.warn("Failed to log SOAP message: {}", e.getMessage());
            }
        }
    }

    private String soapMessageToString(SOAPMessage message) throws Exception {
        StringWriter writer = new StringWriter();
        TransformerFactory.newInstance().newTransformer()
            .transform(new DOMSource(message.getSOAPPart()), new StreamResult(writer));
        return writer.toString();
    }
}