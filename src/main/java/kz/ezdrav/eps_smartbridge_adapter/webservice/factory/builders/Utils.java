package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Utils {

    protected static void addToken(SOAPElement parent, Object value) throws SOAPException {
        if (value != null) {
            SOAPElement child = parent.addChildElement(KaySoapConstants.TOKEN, KaySoapConstants.KAY);
            child.addTextNode(value.toString());
        }
    }
}
