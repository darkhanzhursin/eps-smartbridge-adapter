package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;

@Component
public class DefectElementBuilder implements SoapMessageBuilder {

    @Override
    public void buildBody(SOAPBody soapBody, Object data) throws SOAPException {

    }
}
