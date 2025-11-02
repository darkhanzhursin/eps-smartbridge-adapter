# FinanceSourceBuilder Usage Example

The `FinanceSourceBuilder` has been implemented to build SOAP requests for the `FinanceSource` operation. It uses the JAXB-generated `FinanceElement` class which already contains all required fields.

## Basic Usage

```java
// Create a FinanceSource request
FinanceSource financeSourceRequest = new FinanceSource();
financeSourceRequest.setToken("your-auth-token-here");

// Create FinanceBatch
FinanceBatch financeBatch = new FinanceBatch();
financeSourceRequest.setSData(financeBatch);

// Create a FinanceElement
FinanceElement service = new FinanceElement();

// Required fields
service.setID("1");
service.setPersonId(new BigInteger("15300000037110860"));
service.setDirectionDate(directionDateCalendar); // XMLGregorianCalendar
service.setServiceCode("C03.055.004");
service.setIcdType(2);
service.setOcdCode("D50.1");
service.setDirectionReason(36);
service.setSendedMo(new BigInteger("488"));
service.setSendingMo(new BigInteger("488"));

// Optional fields
service.setPatientStatus(1);
service.setRpnIDMother(new BigInteger("")); // or null for empty
service.setGroupId(null); // null for empty
service.setPlace("П");
service.setParentId(null); // null for empty
service.setServiceKind2(null); // Boolean - can be null for xsi:nil behavior
service.setRefferalId("");
service.setVisitType(null); // Integer - null for empty

// Add service to the batch
financeBatch.getServices().add(service);

// Use the builder (injected via Spring)
@Autowired
private FinanceSourceBuilder financeSourceBuilder;

// Build SOAP message
SOAPMessage soapMessage = ...; // Create SOAP message
SOAPBody soapBody = soapMessage.getSOAPBody();
financeSourceBuilder.buildBody(soapBody, financeSourceRequest);
```

## Field Mapping

The builder maps the following fields from `FinanceElement` to SOAP elements:

| SOAP Element | Java Field | Type | Required |
|--------------|------------|------|----------|
| ID | id | String | Yes |
| person_id | personId | BigInteger | Yes |
| direction_date | directionDate | XMLGregorianCalendar | Yes |
| service_code | serviceCode | String | Yes |
| icd_type | icdType | int | Yes |
| ocd_code | ocdCode | String | Yes |
| direction_reason | directionReason | int | Yes |
| sended_mo | sendedMo | BigInteger | Yes |
| sending_mo | sendingMo | BigInteger | Yes |
| PatientStatus | patientStatus | Integer | No |
| RpnIDMother | rpnIDMother | BigInteger | No |
| group_id | groupId | Integer | No |
| Place | place | String | No |
| Parent_id | parentId | BigInteger | No |
| ServiceKind2 | serviceKind2 | Boolean | No |
| Refferal_id | refferalId | String | No |
| Visit_type | visitType | Integer | No |

## Key Features

1. **All Fields Available**: The JAXB-generated `FinanceElement` class already contains all fields from the WSDL
2. **Null-Safe**: Empty/null fields are rendered as empty XML elements in the SOAP message
3. **Multiple Services**: Supports 0-20000 service elements in a single request
4. **Type Safety**: Uses proper types (BigInteger, XMLGregorianCalendar, etc.) as defined in the WSDL

## Notes

- All required fields must be set before sending the request
- Optional fields can be null or empty strings
- `ServiceKind2` is a Boolean field - when null, it will render as `xsi:nil="true"`
- Make sure to set the authentication token before sending the request
- The structure follows: FinanceSource → sData (FinanceBatch) → Services (List<FinanceElement>) → Token
