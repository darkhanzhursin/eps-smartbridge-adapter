# SetDataBuilder Usage Example

The `SetDataBuilder` has been implemented to build SOAP requests for the `SetData` operation. It supports both the base `ServiceElement` class and the extended `ServiceElementFull` class.

## Basic Usage

```java
// Create a SetData request
SetData setDataRequest = new SetData();
setDataRequest.setToken("your-auth-token-here");

// Create ServicesBatch
ServicesBatch servicesBatch = new ServicesBatch();
setDataRequest.setSData(servicesBatch);

// Create a ServiceElementFull with all fields
ServiceElementFull service = new ServiceElementFull();

// Required core fields
service.setID(new BigInteger("168176414221"));
service.setDate(dateTimeCalendar); // XMLGregorianCalendar
service.setCustomer(new BigInteger("40000000000000531"));
service.setPerformer(new BigInteger("40000000000000531"));
service.setServiceID(0L);

// Department and employee fields (extended fields)
service.setCustomerDepartament(0L);
service.setPerformerDepartament(0L);
service.setCustomerEmployee(0L);
service.setPerformerEmployee(0L);

// Patient information
service.setPatientID(new BigInteger("15300000049662380"));
service.setPatientFirstName("ТЕСТ");
service.setPatientLastName("ТЕСТ");
service.setPatientMiddleName("ТЕСТ"); // Extended field
service.setPatientSexID(2); // Extended field
service.setPatientIDN(""); // Extended field
service.setPatientBirthDate(birthDateCalendar); // XMLGregorianCalendar

// Financial and service details
service.setFinanceSourceID(69L);
service.setVisitKindID(0); // Extended field
service.setTreatmentReasonID(7); // Extended field
service.setCost(new BigDecimal("0")); // Extended field
service.setCount(1); // Extended field
service.setServiceKind2(0); // Extended field
service.setLeasingID(""); // Extended field

// Diagnosis and doctor
service.setMKB10("C54.8");
service.setDoctorFirstName("ТЕСТ");
service.setDoctorLastName("ТЕСТ");
service.setDoctorMiddleName("ТЕСТ"); // Extended field

// Service type fields
service.setServiceKind(0); // Extended field
service.setServiceCDSKind(0); // Extended field
service.setPaymentType(0); // Extended field
service.setResult(""); // Extended field
service.setService("D92.330.001"); // Extended field
service.setPlace("П"); // Extended field
service.setVisitType("1"); // Extended field

// References
service.setGroupId(""); // Extended field
service.setRefferalId(""); // Extended field
service.setParentId(""); // Extended field

// Diagnosis details
service.setDiagType(2); // Extended field
service.setMis(""); // Extended field
service.setFinanceSourceOSMS(""); // Extended field
service.setVisitingId(""); // Extended field
service.setScriningId(""); // Extended field
service.setDiagChar("4"); // Extended field
service.setDiagVid("1"); // Extended field

// Patient status
service.setPatientStatus(1); // Extended field
service.setRpnIDMother(""); // Extended field

// Add check list items if needed
ChekList checkItem = new ChekList();
checkItem.setScriningId("");
checkItem.setChekId("");
checkItem.setChekValue(0);
service.getDiagList().add(checkItem);

// Add service to the batch
servicesBatch.getServices().add(service);

// Use the builder (injected via Spring)
@Autowired
private SetDataBuilder setDataBuilder;

// Build SOAP message
SOAPMessage soapMessage = ...; // Create SOAP message
SOAPBody soapBody = soapMessage.getSOAPBody();
setDataBuilder.buildBody(soapBody, setDataRequest);
```

## Key Features

1. **Backward Compatible**: Works with both `ServiceElement` and `ServiceElementFull`
   - If you use `ServiceElement`, only the base fields will be included
   - If you use `ServiceElementFull`, all extended fields will be included

2. **Null-Safe**: Empty/null fields are rendered as empty XML elements in the SOAP message

3. **Check List Support**: Supports multiple `chek_list` items via the `diagList` field

4. **Multiple Services**: Supports 0-20000 service elements in a single request (as per WSDL spec)

## Notes

- The `ServiceElementFull` class extends the JAXB-generated `ServiceElement` class
- All extended fields are optional and will be rendered as empty elements if null
- Make sure to set the authentication token before sending the request