package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.GetRefferalByPeriod;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.ParamsDate;
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

        addOptionalElement(element, "Token", request.getToken());
    }

    private void buildParams(SOAPElement parent, Object params) throws SOAPException {
        SOAPElement paramsElement = parent.addChildElement("Params", "kay");
        // Reflection or direct casting based on your params structure
        ParamsDate paramsDate = (ParamsDate) params;
        if (paramsDate.getDateBegin() != null) {
            SOAPElement dateBegin = paramsElement.addChildElement(KaySoapConstants.DATE_BEGIN, "kay");
            dateBegin.addTextNode(paramsDate.getDateBegin().toString());
        }
        if (paramsDate.getDateEnd() != null) {
            SOAPElement dateEnd = paramsElement.addChildElement("DateEnd", "kay");
            dateEnd.addTextNode(paramsDate.getDateEnd().toString());
        }

        if (paramsDate.getPageNo() > 0) {
            SOAPElement pageNo = paramsElement.addChildElement("PageNo", "kay");
            pageNo.addTextNode(String.valueOf(paramsDate.getPageNo()));
        }

        if (paramsDate.getPageSize() > 0) {
            SOAPElement pageSize = paramsElement.addChildElement("PageSize", "kay");
            pageSize.addTextNode(String.valueOf(paramsDate.getPageSize()));
        }
    }

    private void addOptionalElement(SOAPElement parent, String name, Object value) throws SOAPException {
        if (value != null) {
            SOAPElement child = parent.addChildElement(name, "kay");
            child.addTextNode(value.toString());
        }
    }
}
