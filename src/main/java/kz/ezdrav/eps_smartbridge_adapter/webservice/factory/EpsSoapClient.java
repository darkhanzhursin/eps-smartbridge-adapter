package kz.ezdrav.eps_smartbridge_adapter.webservice.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@Component
@Slf4j
public class EpsSoapClient {
    private static final Logger log = LoggerFactory.getLogger(EpsSoapClient.class);
    private final SoapMessageFactory soapMessageFactory;

    public EpsSoapClient(SoapMessageFactory soapMessageFactory) {
        this.soapMessageFactory = soapMessageFactory;
    }

    public String sendRequest(SOAPMessage message, String endpoint) throws Exception {
        log.debug("Sending SOAP request to: {}", endpoint);
        logMessage("Request", message);

        SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
        SOAPConnection conn = factory.createConnection();

        try {
            SOAPMessage response = conn.call(message, endpoint);
            logMessage("Response", response);
            return messageToString(response);
        } finally {
            conn.close();
        }
    }

    private void logMessage(String title, SOAPMessage msg) {
        if (log.isDebugEnabled()) {
            try {
                log.debug("{}: {}", title, messageToString(msg));
            } catch (Exception e) {
                log.warn("Failed to log message", e);
            }
        }
    }

    private String messageToString(SOAPMessage msg) throws Exception {
        StringWriter writer = new StringWriter();
        TransformerFactory.newInstance().newTransformer()
                .transform(new DOMSource(msg.getSOAPPart()), new StreamResult(writer));
        return writer.toString();
    }
}
