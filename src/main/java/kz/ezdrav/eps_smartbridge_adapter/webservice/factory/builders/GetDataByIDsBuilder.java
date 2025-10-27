package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.GetDataByIDs;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.ParamsIDs;
import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

@Component
public class GetDataByIDsBuilder implements SoapMessageBuilder {

    @Override
    public void buildBody(SOAPBody soapBody, Object data) throws SOAPException {
        GetDataByIDs request = (GetDataByIDs) data;
        SOAPElement element = soapBody.addChildElement(KaySoapConstants.GET_DATA_BY_IDS, KaySoapConstants.KAY);

        if (request.getParams() != null) {
            buildParams(element, request.getParams());
        }

        Utils.addToken(element, request.getToken());
    }

    private void buildParams(SOAPElement element, ParamsIDs params) throws SOAPException {
        SOAPElement paramsIdsElement = element.addChildElement(KaySoapConstants.PARAMS, KaySoapConstants.KAY);

        if (!params.getIdentifiers().isEmpty()) {
            for (var identifier : params.getIdentifiers()) {
                SOAPElement identifiersElement =
                        paramsIdsElement.addChildElement(KaySoapConstants.IDENTIFIERS, KaySoapConstants.KAY);

                SOAPElement misIdElement = identifiersElement.addChildElement(KaySoapConstants.MIS_ID, KaySoapConstants.KAY);
                misIdElement.addTextNode(identifier.getMisId().toString());

                SOAPElement idElement = identifiersElement.addChildElement(KaySoapConstants.ID, KaySoapConstants.KAY);
                idElement.addTextNode(identifier.getId().toString());
            }
        }
    }
}
