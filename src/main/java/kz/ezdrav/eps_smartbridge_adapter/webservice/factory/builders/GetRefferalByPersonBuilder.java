package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.GetRefferalByPerson;
import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

@Component
public class GetRefferalByPersonBuilder implements SoapMessageBuilder {

    @Override
    public void buildBody(SOAPBody soapBody, Object data) throws SOAPException {
        GetRefferalByPerson request = (GetRefferalByPerson) data;
        SOAPElement element = soapBody.addChildElement(KaySoapConstants.GET_REFERRAL_BY_PERSON, KaySoapConstants.KAY);

        SOAPElement iin = element.addChildElement(KaySoapConstants.IIN, KaySoapConstants.KAY);
        iin.addTextNode(request.getIIN());

        Utils.addToken(element, request.getToken());

        SOAPElement rpnId = element.addChildElement(KaySoapConstants.RPN_ID, KaySoapConstants.KAY);
        rpnId.addTextNode(request.getRpnID().toString());

        SOAPElement consentToken = element.addChildElement(KaySoapConstants.CONSENT_TOKEN, KaySoapConstants.KAY);
        consentToken.addTextNode(request.getConsentToken());
    }
}
