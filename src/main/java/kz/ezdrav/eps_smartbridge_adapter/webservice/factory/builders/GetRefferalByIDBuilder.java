package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.GetRefferalByID;
import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

@Component
public class GetRefferalByIDBuilder implements SoapMessageBuilder {

    @Override
    public void buildBody(SOAPBody soapBody, Object data) throws SOAPException {
        GetRefferalByID request = (GetRefferalByID) data;
        SOAPElement element = soapBody.addChildElement(KaySoapConstants.GET_REFERRAL_BY_ID, KaySoapConstants.KAY);

        SOAPElement id = element.addChildElement(KaySoapConstants.ID, KaySoapConstants.KAY);
        id.addTextNode(request.getID().toString());

        Utils.addToken(element, request.getToken());
    }
}
