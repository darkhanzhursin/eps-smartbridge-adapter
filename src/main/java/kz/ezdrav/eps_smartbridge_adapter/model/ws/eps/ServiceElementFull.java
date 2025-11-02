package kz.ezdrav.eps_smartbridge_adapter.model.ws.eps;

import java.math.BigDecimal;

/**
 * Extended version of ServiceElement with all fields required for SetData SOAP request.
 * This class extends the JAXB-generated ServiceElement to include additional fields
 * that are not present in the base schema.
 */
public class ServiceElementFull extends ServiceElement {

    // Department and employee fields
    protected Long customerDepartament;
    protected Long performerDepartament;
    protected Long customerEmployee;
    protected Long performerEmployee;

    // Additional patient fields
    protected String patientMiddleName;
    protected Integer patientSexID;
    protected String patientIDN;

    // Service details
    protected Integer visitKindID;
    protected Integer treatmentReasonID;
    protected BigDecimal cost;
    protected Integer count;
    protected Integer serviceKind2;
    protected String leasingID;

    // Additional doctor field
    protected String doctorMiddleName;

    // Service types
    protected Integer serviceKind;
    protected Integer serviceCDSKind;
    protected Integer paymentType;

    // Result and service info
    protected String result;
    protected String service;
    protected String place;
    protected String visitType;

    // References
    protected String groupId;
    protected String refferalId;
    protected String parentId;

    // Diagnosis fields
    protected Integer diagType;
    protected String mis;
    protected String financeSourceOSMS;
    protected String visitingId;
    protected String scriningId;
    protected String diagChar;
    protected String diagVid;

    // Patient status
    protected Integer patientStatus;
    protected String rpnIDMother;

    // Getters and Setters

    public Long getCustomerDepartament() {
        return customerDepartament;
    }

    public void setCustomerDepartament(Long customerDepartament) {
        this.customerDepartament = customerDepartament;
    }

    public Long getPerformerDepartament() {
        return performerDepartament;
    }

    public void setPerformerDepartament(Long performerDepartament) {
        this.performerDepartament = performerDepartament;
    }

    public Long getCustomerEmployee() {
        return customerEmployee;
    }

    public void setCustomerEmployee(Long customerEmployee) {
        this.customerEmployee = customerEmployee;
    }

    public Long getPerformerEmployee() {
        return performerEmployee;
    }

    public void setPerformerEmployee(Long performerEmployee) {
        this.performerEmployee = performerEmployee;
    }

    public String getPatientMiddleName() {
        return patientMiddleName;
    }

    public void setPatientMiddleName(String patientMiddleName) {
        this.patientMiddleName = patientMiddleName;
    }

    public Integer getPatientSexID() {
        return patientSexID;
    }

    public void setPatientSexID(Integer patientSexID) {
        this.patientSexID = patientSexID;
    }

    public String getPatientIDN() {
        return patientIDN;
    }

    public void setPatientIDN(String patientIDN) {
        this.patientIDN = patientIDN;
    }

    public Integer getVisitKindID() {
        return visitKindID;
    }

    public void setVisitKindID(Integer visitKindID) {
        this.visitKindID = visitKindID;
    }

    public Integer getTreatmentReasonID() {
        return treatmentReasonID;
    }

    public void setTreatmentReasonID(Integer treatmentReasonID) {
        this.treatmentReasonID = treatmentReasonID;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getServiceKind2() {
        return serviceKind2;
    }

    public void setServiceKind2(Integer serviceKind2) {
        this.serviceKind2 = serviceKind2;
    }

    public String getLeasingID() {
        return leasingID;
    }

    public void setLeasingID(String leasingID) {
        this.leasingID = leasingID;
    }

    public String getDoctorMiddleName() {
        return doctorMiddleName;
    }

    public void setDoctorMiddleName(String doctorMiddleName) {
        this.doctorMiddleName = doctorMiddleName;
    }

    public Integer getServiceKind() {
        return serviceKind;
    }

    public void setServiceKind(Integer serviceKind) {
        this.serviceKind = serviceKind;
    }

    public Integer getServiceCDSKind() {
        return serviceCDSKind;
    }

    public void setServiceCDSKind(Integer serviceCDSKind) {
        this.serviceCDSKind = serviceCDSKind;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRefferalId() {
        return refferalId;
    }

    public void setRefferalId(String refferalId) {
        this.refferalId = refferalId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getDiagType() {
        return diagType;
    }

    public void setDiagType(Integer diagType) {
        this.diagType = diagType;
    }

    public String getMis() {
        return mis;
    }

    public void setMis(String mis) {
        this.mis = mis;
    }

    public String getFinanceSourceOSMS() {
        return financeSourceOSMS;
    }

    public void setFinanceSourceOSMS(String financeSourceOSMS) {
        this.financeSourceOSMS = financeSourceOSMS;
    }

    public String getVisitingId() {
        return visitingId;
    }

    public void setVisitingId(String visitingId) {
        this.visitingId = visitingId;
    }

    public String getScriningId() {
        return scriningId;
    }

    public void setScriningId(String scriningId) {
        this.scriningId = scriningId;
    }

    public String getDiagChar() {
        return diagChar;
    }

    public void setDiagChar(String diagChar) {
        this.diagChar = diagChar;
    }

    public String getDiagVid() {
        return diagVid;
    }

    public void setDiagVid(String diagVid) {
        this.diagVid = diagVid;
    }

    public Integer getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(Integer patientStatus) {
        this.patientStatus = patientStatus;
    }

    public String getRpnIDMother() {
        return rpnIDMother;
    }

    public void setRpnIDMother(String rpnIDMother) {
        this.rpnIDMother = rpnIDMother;
    }
}