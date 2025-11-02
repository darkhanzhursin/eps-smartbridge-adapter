package kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders;

import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.ChekList;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.ServiceElement;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.ServiceElementFull;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.SetData;
import kz.ezdrav.eps_smartbridge_adapter.util.KaySoapConstants;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.SoapMessageBuilder;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

@Component
public class SetDataBuilder implements SoapMessageBuilder {

    @Override
    public void buildBody(SOAPBody soapBody, Object data) throws SOAPException {
        SetData request = (SetData) data;
        SOAPElement setDataElement = soapBody.addChildElement(KaySoapConstants.SET_DATA, KaySoapConstants.KAY);

        if (request.getSData() != null && request.getSData().getServices() != null) {
            buildSData(setDataElement, request);
        }

        Utils.addToken(setDataElement, request.getToken());
    }

    private void buildSData(SOAPElement setDataElement, SetData request) throws SOAPException {
        SOAPElement sDataElement = setDataElement.addChildElement(KaySoapConstants.SDATA, KaySoapConstants.KAY);

        for (ServiceElement service : request.getSData().getServices()) {
            buildService(sDataElement, service);
        }
    }

    private void buildService(SOAPElement sDataElement, ServiceElement service) throws SOAPException {
        SOAPElement servicesElement = sDataElement.addChildElement(KaySoapConstants.SERVICES, KaySoapConstants.KAY);

        // Check if this is a ServiceElementFull for extended fields
        ServiceElementFull serviceFull = service instanceof ServiceElementFull ? (ServiceElementFull) service : null;

        // Core fields
        addElement(servicesElement, KaySoapConstants.ID, service.getID());
        addElement(servicesElement, KaySoapConstants.DATE, service.getDate());
        addElement(servicesElement, KaySoapConstants.CUSTOMER_ID, service.getCustomer());
        addElement(servicesElement, "Performer", service.getPerformer());
        addElement(servicesElement, KaySoapConstants.CUST_DEPARTMENT,
                serviceFull != null ? serviceFull.getCustomerDepartament() : null);
        addElement(servicesElement, KaySoapConstants.PERF_DEPARTMENT,
                serviceFull != null ? serviceFull.getPerformerDepartament() : null);
        addElement(servicesElement, KaySoapConstants.CUSTOMER_EMPLOYEE,
                serviceFull != null ? serviceFull.getCustomerEmployee() : null);
        addElement(servicesElement, KaySoapConstants.PERFORMER_EMPLOYEE,
                serviceFull != null ? serviceFull.getPerformerEmployee() : null);
        addElement(servicesElement, KaySoapConstants.SERVICE_ID, service.getServiceID());

        // Patient information
        addElement(servicesElement, KaySoapConstants.PATIENT_ID, service.getPatientID());
        addElement(servicesElement, KaySoapConstants.PATIENT_FIRST_NAME, service.getPatientFirstName());
        addElement(servicesElement, KaySoapConstants.PATIENT_LAST_NAME, service.getPatientLastName());
        addElement(servicesElement, KaySoapConstants.PATIENT_MIDDLE_NAME,
                serviceFull != null ? serviceFull.getPatientMiddleName() : null);
        addElement(servicesElement, KaySoapConstants.PATIENT_SEX_ID,
                serviceFull != null ? serviceFull.getPatientSexID() : null);
        addElement(servicesElement, KaySoapConstants.PATIENT_IDN,
                serviceFull != null ? serviceFull.getPatientIDN() : null);
        addElement(servicesElement, KaySoapConstants.PATIENT_BIRTH_DATE, service.getPatientBirthDate());

        // Financial and service details
        addElement(servicesElement, KaySoapConstants.FINANCE_SOURCE_ID, service.getFinanceSourceID());
        addElement(servicesElement, "VisitKindID",
                serviceFull != null ? serviceFull.getVisitKindID() : null);
        addElement(servicesElement, KaySoapConstants.TREATMENT_REASON_ID,
                serviceFull != null ? serviceFull.getTreatmentReasonID() : null);
        addElement(servicesElement, KaySoapConstants.COST,
                serviceFull != null ? serviceFull.getCost() : null);
        addElement(servicesElement, KaySoapConstants.COUNT,
                serviceFull != null ? serviceFull.getCount() : null);
        addElement(servicesElement, KaySoapConstants.SERVICE_KIND2,
                serviceFull != null ? serviceFull.getServiceKind2() : null);
        addElement(servicesElement, KaySoapConstants.LEASING_ID,
                serviceFull != null ? serviceFull.getLeasingID() : null);

        // Diagnosis and doctor
        addElement(servicesElement, KaySoapConstants.MKB10, service.getMKB10());
        addElement(servicesElement, KaySoapConstants.DOCTOR_FIRST_NAME, service.getDoctorFirstName());
        addElement(servicesElement, KaySoapConstants.DOCTOR_LAST_NAME, service.getDoctorLastName());
        addElement(servicesElement, KaySoapConstants.DOCTOR_MIDDLE_NAME,
                serviceFull != null ? serviceFull.getDoctorMiddleName() : null);

        // Service kind and type
        addElement(servicesElement, KaySoapConstants.SERVICE_KIND,
                serviceFull != null ? serviceFull.getServiceKind() : null);
        addElement(servicesElement, KaySoapConstants.SERVICE_CDS_KIND,
                serviceFull != null ? serviceFull.getServiceCDSKind() : null);
        addElement(servicesElement, KaySoapConstants.PAYMENT_TYPE,
                serviceFull != null ? serviceFull.getPaymentType() : null);
        addElement(servicesElement, KaySoapConstants.RESULT,
                serviceFull != null ? serviceFull.getResult() : null);
        addElement(servicesElement, KaySoapConstants.SERVICE,
                serviceFull != null ? serviceFull.getService() : null);
        addElement(servicesElement, KaySoapConstants.PLACE,
                serviceFull != null ? serviceFull.getPlace() : null);
        addElement(servicesElement, KaySoapConstants.VISIT_TYPE,
                serviceFull != null ? serviceFull.getVisitType() : null);

        // References
        addElement(servicesElement, KaySoapConstants.GROUP_ID,
                serviceFull != null ? serviceFull.getGroupId() : null);
        addElement(servicesElement, KaySoapConstants.REFFERAL_ID,
                serviceFull != null ? serviceFull.getRefferalId() : null);
        addElement(servicesElement, KaySoapConstants.PARENT_ID,
                serviceFull != null ? serviceFull.getParentId() : null);

        // Diagnosis type
        addElement(servicesElement, KaySoapConstants.DIAG_TYPE,
                serviceFull != null ? serviceFull.getDiagType() : null);
        addElement(servicesElement, "MIS",
                serviceFull != null ? serviceFull.getMis() : null);
        addElement(servicesElement, KaySoapConstants.FINANCE_SOURCE_OSMS,
                serviceFull != null ? serviceFull.getFinanceSourceOSMS() : null);
        addElement(servicesElement, KaySoapConstants.VISITING_ID,
                serviceFull != null ? serviceFull.getVisitingId() : null);
        addElement(servicesElement, KaySoapConstants.SCRINING_ID,
                serviceFull != null ? serviceFull.getScriningId() : null);

        // Check list (nested elements)
        if (service.getDiagList() != null && !service.getDiagList().isEmpty()) {
            for (ChekList checkItem : service.getDiagList()) {
                buildCheckList(servicesElement, checkItem);
            }
        }

        addElement(servicesElement, KaySoapConstants.DIAG_CHAR,
                serviceFull != null ? serviceFull.getDiagChar() : null);
        addElement(servicesElement, KaySoapConstants.DIAG_VID,
                serviceFull != null ? serviceFull.getDiagVid() : null);
        addElement(servicesElement, KaySoapConstants.PATIENT_STATUS,
                serviceFull != null ? serviceFull.getPatientStatus() : null);
        addElement(servicesElement, KaySoapConstants.RPN_ID_MOTHER,
                serviceFull != null ? serviceFull.getRpnIDMother() : null);
    }

    private void buildCheckList(SOAPElement servicesElement, ChekList checkItem) throws SOAPException {
        SOAPElement checkListElement = servicesElement.addChildElement(KaySoapConstants.CHECK_LIST, KaySoapConstants.KAY);

        addElement(checkListElement, KaySoapConstants.SCRINING_ID, checkItem.getScriningId());
        addElement(checkListElement, KaySoapConstants.CHECK_ID, checkItem.getChekId());
        addElement(checkListElement, KaySoapConstants.CHECK_VALUE, checkItem.getChekValue());
    }

    private void addElement(SOAPElement parent, String elementName, Object value) throws SOAPException {
        SOAPElement child = parent.addChildElement(elementName, KaySoapConstants.KAY);
        if (value != null) {
            child.addTextNode(value.toString());
        }
    }
}

