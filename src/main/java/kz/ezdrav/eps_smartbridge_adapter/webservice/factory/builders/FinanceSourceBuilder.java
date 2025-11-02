package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.FinanceElement;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.FinanceSource;
import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

@Component
public class FinanceSourceBuilder implements SoapMessageBuilder {

    @Override
    public void buildBody(SOAPBody soapBody, Object data) throws SOAPException {
        FinanceSource request = (FinanceSource) data;
        SOAPElement financeSourceElement = soapBody.addChildElement(KaySoapConstants.FINANCE_SOURCE, KaySoapConstants.KAY);

        if (request.getSData() != null && request.getSData().getServices() != null) {
            buildSData(financeSourceElement, request);
        }

        Utils.addToken(financeSourceElement, request.getToken());
    }

    private void buildSData(SOAPElement financeSourceElement, FinanceSource request) throws SOAPException {
        SOAPElement sDataElement = financeSourceElement.addChildElement(KaySoapConstants.SDATA, KaySoapConstants.KAY);

        for (FinanceElement service : request.getSData().getServices()) {
            buildService(sDataElement, service);
        }
    }

    private void buildService(SOAPElement sDataElement, FinanceElement service) throws SOAPException {
        SOAPElement servicesElement = sDataElement.addChildElement(KaySoapConstants.SERVICES, KaySoapConstants.KAY);

        // Required fields
        addElement(servicesElement, KaySoapConstants.ID, service.getID());
        addElement(servicesElement, KaySoapConstants.PERSON_ID, service.getPersonId());
        addElement(servicesElement, KaySoapConstants.DIRECTION_DATE, service.getDirectionDate());
        addElement(servicesElement, KaySoapConstants.SERVICE_CODE, service.getServiceCode());
        addElement(servicesElement, KaySoapConstants.ICD_TYPE, service.getIcdType());
        addElement(servicesElement, KaySoapConstants.OCD_CODE, service.getOcdCode());
        addElement(servicesElement, KaySoapConstants.DIRECTION_REASON, service.getDirectionReason());
        addElement(servicesElement, KaySoapConstants.SENDED_MO, service.getSendedMo());
        addElement(servicesElement, KaySoapConstants.CUST_MO, service.getSendingMo());

        // Optional fields
        addElement(servicesElement, KaySoapConstants.PATIENT_STATUS, service.getPatientStatus());
        addElement(servicesElement, KaySoapConstants.RPN_ID_MOTHER, service.getRpnIDMother());
        addElement(servicesElement, KaySoapConstants.GROUP_ID, service.getGroupId());
        addElement(servicesElement, KaySoapConstants.PLACE, service.getPlace());
        addElement(servicesElement, KaySoapConstants.PARENT_ID, service.getParentId());
        addElement(servicesElement, KaySoapConstants.SERVICE_KIND2, service.isServiceKind2());
        addElement(servicesElement, KaySoapConstants.REFFERAL_ID, service.getRefferalId());
        addElement(servicesElement, KaySoapConstants.VISIT_TYPE, service.getVisitType());
    }

    private void addElement(SOAPElement parent, String elementName, Object value) throws SOAPException {
        SOAPElement child = parent.addChildElement(elementName, KaySoapConstants.KAY);
        if (value != null) {
            child.addTextNode(value.toString());
        }
    }
}
