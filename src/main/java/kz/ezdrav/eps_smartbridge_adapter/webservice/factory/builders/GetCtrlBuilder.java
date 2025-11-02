package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.exception.FieldMandatoryException;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.GetCtrl;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.ParamsDate;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.ParamsDateFull;
import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.util.Optional;

@Component
public class GetCtrlBuilder implements SoapMessageBuilder {

    private final String template = " is mandatory field";

    @Override
    public void buildBody(SOAPBody soapBody, Object data) throws SOAPException {
        GetCtrl request = (GetCtrl) data;
        SOAPElement element = soapBody.addChildElement(KaySoapConstants.GET_CTRL, KaySoapConstants.KAY);

        if (request.getParams() != null) {
            buildParams(element, request.getParams());
        }

        Utils.addToken(element, request.getToken());
    }

    private void buildParams(SOAPElement element, ParamsDate params) throws SOAPException {
        SOAPElement paramsElement = element.addChildElement(KaySoapConstants.PARAMS, KaySoapConstants.KAY);

        // Check if this is a ParamsDateFull for extended fields
        ParamsDateFull paramsFull = params instanceof ParamsDateFull ? (ParamsDateFull) params : null;

        // RequestID (extended field, required in SOAP)
        if (paramsFull != null && paramsFull.getRequestID() != null) {
            addElement(paramsElement, KaySoapConstants.REQUEST_ID, paramsFull.getRequestID());
        }

        // DateBegin (required)
        var dateBegin = Optional.of(params.getDateBegin())
                .orElseThrow(() -> new FieldMandatoryException(KaySoapConstants.DATE_BEGIN + template));
        addElement(paramsElement, KaySoapConstants.DATE_BEGIN, dateBegin);

        // DateEnd (required)
        var dateEnd = Optional.of(params.getDateEnd())
                .orElseThrow(() -> new FieldMandatoryException(KaySoapConstants.DATE_END + template));
        addElement(paramsElement, KaySoapConstants.DATE_END, dateEnd);

        // TypeRegistr (required)
        var typeRegister = Optional.of(params.getTypeRegistr())
                .orElseThrow(() -> new FieldMandatoryException(KaySoapConstants.TYPE_REGISTR + template));
        addElement(paramsElement, KaySoapConstants.TYPE_REGISTR, typeRegister);

        // Extended optional fields
        if (paramsFull != null) {
            addElement(paramsElement, KaySoapConstants.FINANCE_SOURCE, paramsFull.getFinanceSource());
            addElement(paramsElement, KaySoapConstants.CUSTOMER_ID, paramsFull.getCustomerID());
            addElement(paramsElement, KaySoapConstants.PERFORMER_ID, paramsFull.getPerformerID());
            addElement(paramsElement, KaySoapConstants.TREATMENT_REASON_ID, paramsFull.getTreatmentReasonID());
            addElement(paramsElement, KaySoapConstants.SERVICE_CDS_KIND, paramsFull.getServiceCDSKind());
            addElement(paramsElement, KaySoapConstants.PATIENT_IIN, paramsFull.getPatientIIN());
        }
    }

    private void addElement(SOAPElement parent, String elementName, Object value) throws SOAPException {
        SOAPElement child = parent.addChildElement(elementName, KaySoapConstants.KAY);
        if (value != null) {
            child.addTextNode(value.toString());
        }
    }
}
