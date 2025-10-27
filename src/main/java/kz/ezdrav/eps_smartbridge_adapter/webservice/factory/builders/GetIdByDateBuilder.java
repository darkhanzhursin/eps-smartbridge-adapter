package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.exception.FieldMandatoryException;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.GetIdByDate;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.ParamsDate;
import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.util.Optional;

@Component
public class GetIdByDateBuilder implements SoapMessageBuilder {

    private final String template = " is mandatory field";

    @Override
    public void buildBody(SOAPBody soapBody, Object data) throws SOAPException {
        GetIdByDate request = (GetIdByDate) data;
        SOAPElement element = soapBody.addChildElement(KaySoapConstants.GET_ID_BY_DATE, KaySoapConstants.KAY);

        if (request.getParams() != null) {
            buildParams(element, request.getParams());
        }

        Utils.addToken(element, request.getToken());
    }

    private void buildParams(SOAPElement element, ParamsDate params) throws SOAPException {
        SOAPElement paramsElement = element.addChildElement(KaySoapConstants.PARAMS, KaySoapConstants.KAY);

        var dateBegin = Optional.of(params.getDateBegin())
                .orElseThrow(() -> new FieldMandatoryException(KaySoapConstants.DATE_BEGIN + template));
        SOAPElement dateBeginElement = paramsElement.addChildElement(KaySoapConstants.DATE_BEGIN, KaySoapConstants.KAY);
        dateBeginElement.addTextNode(dateBegin.toString());

        var dateEnd = Optional.of(params.getDateEnd())
                .orElseThrow(() -> new FieldMandatoryException(KaySoapConstants.DATE_BEGIN + template));
        SOAPElement dateEndElement = paramsElement.addChildElement(KaySoapConstants.DATE_END, KaySoapConstants.KAY);
        dateEndElement.addTextNode(dateEnd.toString());

        var typeRegister = Optional.of(params.getTypeRegistr())
                .orElseThrow(() -> new FieldMandatoryException(KaySoapConstants.TYPE_REGISTR + template));
        SOAPElement typeRegisterElement = paramsElement.addChildElement(KaySoapConstants.TYPE_REGISTR, KaySoapConstants.KAY);
        typeRegisterElement.addTextNode(typeRegister.toString());

        if (params.getAfterUpd() != null) {
            SOAPElement afterUpd = paramsElement.addChildElement(KaySoapConstants.AFTER_UPD, KaySoapConstants.KAY);
            afterUpd.addTextNode(params.getAfterUpd().toString());
        }

        if (params.getRegion() != null) {
            SOAPElement region = paramsElement.addChildElement(KaySoapConstants.REGION, KaySoapConstants.KAY);
            region.addTextNode(region.toString());
        }

        if (params.getAfterUpdEnd() != null) {
            SOAPElement afterUpdEnd = paramsElement.addChildElement(KaySoapConstants.AFTER_UPD_END, KaySoapConstants.KAY);
            afterUpdEnd.addTextNode(afterUpdEnd.toString());
        }
    }
}
