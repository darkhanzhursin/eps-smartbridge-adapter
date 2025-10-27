package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.GetRefferalByPeriod;
import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

@Component
public class GetRefferalByPeriodBuilder implements SoapMessageBuilder {

    @Override
    public void buildBody(SOAPBody soapBody, Object data) throws SOAPException {
        GetRefferalByPeriod request = (GetRefferalByPeriod) data;
        SOAPElement element = soapBody.addChildElement(KaySoapConstants.GET_REFERRAL_BY_PERIOD, KaySoapConstants.KAY);

        if (request.getParams() != null) {
            buildParams(element, request.getParams());
        }
    }

    private void buildParams(SOAPElement parent, Object params) throws SOAPException {
        SOAPElement paramsElement = parent.addChildElement("Params", "kay");
        // Reflection or direct casting based on your params structure
    }
}
