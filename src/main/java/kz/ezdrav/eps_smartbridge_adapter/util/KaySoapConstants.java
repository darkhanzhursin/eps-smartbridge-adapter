package kz.ezdrav.eps_smartbridge_adapter.util;

/**
 * Constants for SOAP element names used in KaySat PS integration.
 */
public final class KaySoapConstants {

    private KaySoapConstants() {
        // Prevent instantiation
    }

    // Common
    public static final String NAMESPACE = "http://www.kaysat-ps.org";
    public static final String TOKEN = "Token";
    public static final String PARAMS = "Params";
    public static final String SDATA = "sData";
    public static final String SERVICES = "Services";
    public static final String KAY = "kay";

    // Common request fields
    public static final String REQUEST_ID = "RequestID";
    public static final String DATE_BEGIN = "DateBegin";
    public static final String DATE_END = "DateEnd";
    public static final String TYPE_REGISTR = "TypeRegistr";
    public static final String FINANCE_SOURCE = "FinanceSource";
    public static final String CUSTOMER_ID = "СustomerID";
    public static final String PERFORMER_ID = "PerformerID";
    public static final String TREATMENT_REASON_ID = "TreatmentReasonID";
    public static final String SERVICE_CDS_KIND = "ServiceCDSKind";
    public static final String PATIENT_IIN = "PatientIIN";
    public static final String AFTER_UPD = "AfterUpd";
    public static final String AFTER_UPD_END = "AfterUpdEnd";
    public static final String REGION = "Region";
    public static final String PAGE_NO = "PageNo";
    public static final String PAGE_SIZE = "PageSize";
    public static final String SIZE = "size";
    public static final String IIN = "IIN";
    public static final String CONSENT_TOKEN = "ConsentToken";

    // Identifiers
    public static final String IDENTIFIERS = "Identifiers";
    public static final String MIS_ID = "mis_id";
    public static final String ID = "id";

    // Referral
    public static final String DIRECTED_MO_ID = "directed_mo_id";
    public static final String SENDING_MO_ID = "sending_mo_id";
    public static final String PERSON_ID = "person_id";
    public static final String DIRECTION_DATE = "direction_date";
    public static final String SERVICE_CODE = "service_code";
    public static final String ICD_TYPE = "icd_type";
    public static final String OCD_CODE = "ocd_code";
    public static final String DIRECTION_REASON = "direction_reason";
    public static final String PATIENT_STATUS = "PatientStatus";
    public static final String RPN_ID_MOTHER = "RpnIDMother";
    public static final String RPN_ID = "RpnID";
    public static final String GROUP_ID = "group_id";
    public static final String PLACE = "Place";
    public static final String PARENT_ID = "Parent_id";
    public static final String SERVICE_KIND2 = "ServiceKind2";
    public static final String REFFERAL_ID = "Refferal_id";
    public static final String VISIT_TYPE = "Visit_type";
    public static final String CUST_DEPARTMENT = "CustomerDepartament";
    public static final String PERF_DEPARTMENT = "PerformerDepartament";
    public static final String CUSTOMER_EMPLOYEE = "CustomerEmployee";
    public static final String PERFORMER_EMPLOYEE = "PerformerEmployee";

    // Patient
    public static final String PATIENT_ID = "PatientID";
    public static final String PATIENT_FIRST_NAME = "PatientFirstName";
    public static final String PATIENT_LAST_NAME = "PatientLastName";
    public static final String PATIENT_MIDDLE_NAME = "PatientMiddleName";
    public static final String PATIENT_SEX_ID = "PatientSexID";
    public static final String PATIENT_IDN = "PatientIDN";
    public static final String PATIENT_BIRTH_DATE = "PatientBirthDate";
    public static final String PATIENT_PHONE_NUMBER = "PatientPhoneNumber";
    public static final String PATIENT_ADDRESS = "PatientAddress";
    public static final String PATIENT_FIO = "PatientFIO";

    // Doctor
    public static final String DOCTOR_FIRST_NAME = "DoctorFirstName";
    public static final String DOCTOR_LAST_NAME = "DoctorLastName";
    public static final String DOCTOR_MIDDLE_NAME = "DoctorMiddleName";
    public static final String DOCTOR_FIO = "DoctorFIO";
    public static final String CUSTOMER_EMPLOYEE_POST_ID = "CustomerEmployeePostID";

    // Financial
    public static final String FINANCE_SOURCE_ID = "FinanceSourceID";
    public static final String PAYMENT_TYPE = "PaymentType";
    public static final String FINANCE_SOURCE_OSMS = "FinanceSourceOSMS";

    // Services
    public static final String SERVICE_ID = "ServiceID";
    public static final String SERVICE_KIND = "ServiceKind";
    public static final String LEASING_ID = "LeasingID";
    public static final String COST = "Cost";
    public static final String COUNT = "Count";
    public static final String RESULT = "Result";
    public static final String DATE = "Date";
    public static final String DATE_VERIFIED = "DateVerified";
    public static final String DELETE_DATE = "DeleteDate";
    public static final String SERVICE = "Service";
    public static final String VISITING_ID = "Visiting_id";
    public static final String SCRINING_ID = "scrining_id";
    public static final String CHECK_LIST = "chek_list";
    public static final String CHECK_ID = "chek_id";
    public static final String CHECK_VALUE = "chek_value";

    // Diagnosis
    public static final String DIAG_TYPE = "Diag_type";
    public static final String DIAG_CHAR = "Diag_char";
    public static final String DIAG_VID = "Diag_vid";
    public static final String DIAG_LIST = "Diag_list";
    public static final String MKB10 = "MKB10";

    // Referral specific
    public static final String CUST_MO = "sending_mo";
    public static final String SENDED_MO = "sended_mo";
    public static final String CANCEL_DATE = "cancel_date";
    public static final String PAYMENTTYPE = "paymentType";
    public static final String SCRINING_ID_REF = "scrining_id";
    public static final String REFFERAL_STATUS = "RefferalStatus";
    public static final String CHEK_ID = "chek_id";

    // Ambulatory
    public static final String TYPE_CALL = "TypeCall";
    public static final String CALL_REASON = "СallReason";
    public static final String NOTE = "Note";

    // Defects
    public static final String DEFECTS = "Defects";
    public static final String DEFECT_ID = "defect_id";
    public static final String DATE_DEFECT = "date_defect";
    public static final String ACT = "act";
    public static final String MONITORING = "monitoring";
    public static final String EXPERT_ID = "expert_id";
    public static final String VID_DEFECT = "vid_defect";
    public static final String MESSAGE = "Message";

    // Screening
    public static final String REFFERAL = "Refferal";
    public static final String GET_CTRL = "GetCtrl";
    public static final String GET_DATA2 = "GetData2";
    public static final String GET_DATA_BY_IDS = "GetDataByIDs";
    public static final String GET_DATA_BY_PERIOD = "GetDataByPeriod";
    public static final String GET_ID_BY_DATE = "GetIdByDate";
    public static final String GET_IDS = "GetIDs";
    public static final String GET_REFERRAL = "GetReferral";
    public static final String GET_REFERRAL_BY_ID = "GetRefferalByID";
    public static final String GET_REFERRAL_BY_PERIOD = "GetRefferalByPeriod";
    public static final String GET_REFERRAL_BY_PERSON = "GetRefferalByPerson";
    public static final String GET_SERVICES_BY_DATE = "GetServicesByDate";
    public static final String SET_DATA = "SetData";
    public static final String SET_DATA_AMB = "SetDataAmb";
    public static final String SET_DEFECTS = "SetDefects";
    public static final String SET_REFERRAL = "SetReferral";
}

