package kz.ezdrav.eps_smartbridge_adapter.webservice.factory;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;

public interface SoapMessageBuilder {
    void buildBody(SOAPBody soapBody, Object data) throws SOAPException;
}
