package kz.ezdrav.eps_smartbridge_adapter.model.ws.eps;

import java.math.BigInteger;

/**
 * Extended version of ParamsDate with all fields required for GetCtrl SOAP request.
 * This class extends the JAXB-generated ParamsDate to include additional fields
 * that are not present in the base schema.
 */
public class ParamsDateFull extends ParamsDate {

    // Additional required field
    protected String requestID;

    // Additional optional fields
    protected String financeSource;
    protected BigInteger customerID;
    protected BigInteger performerID;
    protected Integer treatmentReasonID;
    protected Integer serviceCDSKind;
    protected String patientIIN;

    // Getters and Setters

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getFinanceSource() {
        return financeSource;
    }

    public void setFinanceSource(String financeSource) {
        this.financeSource = financeSource;
    }

    public BigInteger getCustomerID() {
        return customerID;
    }

    public void setCustomerID(BigInteger customerID) {
        this.customerID = customerID;
    }

    public BigInteger getPerformerID() {
        return performerID;
    }

    public void setPerformerID(BigInteger performerID) {
        this.performerID = performerID;
    }

    public Integer getTreatmentReasonID() {
        return treatmentReasonID;
    }

    public void setTreatmentReasonID(Integer treatmentReasonID) {
        this.treatmentReasonID = treatmentReasonID;
    }

    public Integer getServiceCDSKind() {
        return serviceCDSKind;
    }

    public void setServiceCDSKind(Integer serviceCDSKind) {
        this.serviceCDSKind = serviceCDSKind;
    }

    public String getPatientIIN() {
        return patientIIN;
    }

    public void setPatientIIN(String patientIIN) {
        this.patientIIN = patientIIN;
    }
}