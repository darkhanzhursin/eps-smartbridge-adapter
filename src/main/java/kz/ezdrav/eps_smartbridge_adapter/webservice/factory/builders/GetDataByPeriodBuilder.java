package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.exception.FieldMandatoryException;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.GetDataByPeriod;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.ParamsDate;
import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.util.Optional;

@Component
public class GetDataByPeriodBuilder implements SoapMessageBuilder {

    private final String template = " is mandatory field";

    @Override
    public void buildBody(SOAPBody soapBody, Object data) throws SOAPException {
        GetDataByPeriod request = (GetDataByPeriod) data;
        SOAPElement element = soapBody.addChildElement(KaySoapConstants.GET_DATA_BY_PERIOD, KaySoapConstants.KAY);

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

        if (params.getPageNo() != 0) {
            SOAPElement pageNo = paramsElement.addChildElement(KaySoapConstants.PAGE_NO, KaySoapConstants.KAY);
            pageNo.addTextNode(String.valueOf(params.getPageNo()));
        }

        if (params.getPageSize() != 0) {
            SOAPElement pageSize = paramsElement.addChildElement(KaySoapConstants.PAGE_SIZE, KaySoapConstants.KAY);
            pageSize.addTextNode(String.valueOf(params.getPageSize()));
        }

    }
}
