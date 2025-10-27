package kz.ezdrav.eps_smartbridge_adapter.webservice.factory;

import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.xml.soap.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SoapMessageFactory {

    public SOAPMessage createMessage(Object data, SoapMessageBuilder builder) throws Exception {
        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = mf.createMessage();

        SOAPPart part = msg.getSOAPPart();
        SOAPEnvelope envelope = part.getEnvelope();
        envelope.addNamespaceDeclaration(KaySoapConstants.KAY, KaySoapConstants.NAMESPACE);

        builder.buildBody(envelope.getBody(), data);
        msg.saveChanges();

        return msg;
    }
}
