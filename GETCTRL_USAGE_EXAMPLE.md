# GetCtrlBuilder Usage Example

The `GetCtrlBuilder` has been implemented to build SOAP requests for the `GetCtrl` operation. It supports both the base `ParamsDate` class and the extended `ParamsDateFull` class.

## Basic Usage

```java
// Create a GetCtrl request
GetCtrl getCtrlRequest = new GetCtrl();
getCtrlRequest.setToken("your-auth-token-here");

// Create ParamsDateFull with all fields
ParamsDateFull params = new ParamsDateFull();

// Required field (extended)
params.setRequestID("1");

// Required fields (base class)
params.setDateBegin(dateBeginCalendar); // XMLGregorianCalendar - 2025-07-02T00:00:00.000
params.setDateEnd(dateEndCalendar);     // XMLGregorianCalendar - 2025-07-02T00:00:00.000
params.setTypeRegistr(2);

// Optional extended fields
params.setFinanceSource("");              // Optional
params.setCustomerID(null);               // Optional - BigInteger
params.setPerformerID(null);              // Optional - BigInteger
params.setTreatmentReasonID(null);        // Optional - Integer
params.setServiceCDSKind(null);           // Optional - Integer
params.setPatientIIN("");                 // Optional - String

// Set params to request
getCtrlRequest.setParams(params);

// Use the builder (injected via Spring)
@Autowired
private GetCtrlBuilder getCtrlBuilder;

// Build SOAP message
SOAPMessage soapMessage = ...; // Create SOAP message
SOAPBody soapBody = soapMessage.getSOAPBody();
getCtrlBuilder.buildBody(soapBody, getCtrlRequest);
```

## Field Mapping

The builder maps the following fields from `ParamsDateFull` to SOAP elements:

| SOAP Element | Java Field | Type | Source | Required |
|--------------|------------|------|--------|----------|
| RequestID | requestID | String | Extended | Yes* |
| DateBegin | dateBegin | XMLGregorianCalendar | Base | Yes |
| DateEnd | dateEnd | XMLGregorianCalendar | Base | Yes |
| TypeRegistr | typeRegistr | int | Base | Yes |
| FinanceSource | financeSource | String | Extended | No |
| СustomerID | customerID | BigInteger | Extended | No |
| PerformerID | performerID | BigInteger | Extended | No |
| TreatmentReasonID | treatmentReasonID | Integer | Extended | No |
| ServiceCDSKind | serviceCDSKind | Integer | Extended | No |
| PatientIIN | patientIIN | String | Extended | No |

\* RequestID is required in the SOAP request but is an extended field in `ParamsDateFull`

## Key Features

1. **Backward Compatible**: Works with both `ParamsDate` and `ParamsDateFull`
   - If you use `ParamsDate`, only the base fields will be included
   - If you use `ParamsDateFull`, all extended fields will be included

2. **Validation**: Required fields are validated and will throw `FieldMandatoryException` if missing
   - DateBegin (required)
   - DateEnd (required)
   - TypeRegistr (required)

3. **Null-Safe**: Empty/null optional fields are rendered as empty XML elements

## Notes

- The `ParamsDateFull` class extends the JAXB-generated `ParamsDate` class
- All extended fields are optional in the SOAP schema
- Required field validation is performed before building the SOAP message
- Make sure to set the authentication token before sending the request

## Additional ParamsDate Fields (Not in SOAP Example)

The base `ParamsDate` class also contains these fields that may be used in other operations:
- PageNo (int)
- PageSize (int)
- AfterUpd (XMLGregorianCalendar)
- AfterUpdEnd (XMLGregorianCalendar)
- Region (String)

These fields are available but were not present in the GetCtrl SOAP example provided.