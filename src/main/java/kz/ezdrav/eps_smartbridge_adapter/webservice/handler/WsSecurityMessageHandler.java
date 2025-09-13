package kz.ezdrav.eps_smartbridge_adapter.webservice.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import kz.ezdrav.eps_smartbridge_adapter.util.FileSystemFunctions;
import kz.gov.pki.kalkan.asn1.knca.KNCAObjectIdentifiers;
import kz.gov.pki.kalkan.asn1.pkcs.PKCSObjectIdentifiers;
import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import kz.gov.pki.kalkan.xmldsig.KncaXS;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.token.SecurityTokenReference;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.XMLUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class WsSecurityMessageHandler implements SOAPHandler<SOAPMessageContext> {
    private static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
    private static final String WSU_PREFIX = "wsu";
    private static final String WS_HEADER_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static final String WS_HEADER_TAG = "Security";
    private static final String KALKAN_ALGORITHMS_SPEC_NS = "urn:ietf:params:xml:ns:pkigovkz:xmlsec:algorithms:";
    private static final String KEY_STORAGE_TYPE = "PKCS12";

    @Value("${sign.wss-key}")
    private String wssKey;
    @Value("${sign.wss-cert}")
    private String wssCert;
    @Value("${sign.wss-password}")
    private String wssPassword;

    @Override
    public Set<QName> getHeaders() {
        QName qName = new QName(WS_HEADER_NS, WS_HEADER_TAG);
        HashSet<QName> hashSet = new HashSet<>();
        hashSet.add(qName);
        return hashSet;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext soapMessageContext) {
        Boolean outboundProperty = (Boolean) soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        SOAPMessage soapMessage = soapMessageContext.getMessage();
        ByteArrayOutputStream messageOs = new ByteArrayOutputStream();

        try {
            soapMessage.writeTo(messageOs);
            String xml = messageOs.toString(StandardCharsets.UTF_8);

            if (outboundProperty) {
                SOAPMessage signedSoap = sign(soapMessage);
                ByteArrayOutputStream signedOs = new ByteArrayOutputStream();
                signedSoap.writeTo(signedOs);
                soapMessageContext.getMessage().getSOAPPart().setContent(signedSoap.getSOAPPart().getContent());
                return true;
            }

            if (!verifyXml(xml)) {
                throw new RuntimeException("WS-SECURITY is not valid");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext soapMessageContext) {
        return true;
    }

    @Override
    public void close(MessageContext messageContext) {
    }

    private SOAPMessage sign(SOAPMessage msg) {
        KalkanProvider kalkanProvider = new KalkanProvider();
        Security.addProvider(kalkanProvider);
        KncaXS.loadXMLSecurity();

        final String signMethod;
        final String digestMethod;
        try {
            SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
            SOAPBody body = env.getBody();

            String bodyId = "id-" + UUID.randomUUID();
            body.addAttribute(new QName(WSU_NS, "Id", WSU_PREFIX), bodyId);

            SOAPHeader header = env.getHeader();
            if (header == null) {
                header = env.addHeader();
            }

            final PrivateKey privateKey = FileSystemFunctions.loadKeyFromBase64(wssKey, KEY_STORAGE_TYPE, wssPassword);
            final X509Certificate x509Certificate = FileSystemFunctions.loadCertFromBase64(wssKey, KEY_STORAGE_TYPE, wssPassword);

            String sigAlgOid = x509Certificate.getSigAlgOID();
            if (sigAlgOid.equals(PKCSObjectIdentifiers.sha1WithRSAEncryption.getId())) {
                signMethod = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha1";
                digestMethod = "http://www.w3.org/2001/04/xmldsig-more#sha1";
            } else if (sigAlgOid.equals(PKCSObjectIdentifiers.sha256WithRSAEncryption.getId())) {
                signMethod = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
                digestMethod = "http://www.w3.org/2001/04/xmlenc#sha256";
            } else if (sigAlgOid.equals(KNCAObjectIdentifiers.gost3411_2015_with_gost3410_2015_512.getId())) {
                signMethod = KALKAN_ALGORITHMS_SPEC_NS + "gostr34102015-gostr34112015-512";
                digestMethod = KALKAN_ALGORITHMS_SPEC_NS + "gostr34112015-512";
            } else {
                signMethod = "http://www.w3.org/2001/04/xmldsig-more#gost34310-gost34311";
                digestMethod = "http://www.w3.org/2001/04/xmldsig-more#gost34311";
            }

            Document doc = env.getOwnerDocument();
            Transforms transforms = new Transforms(doc);
            transforms.addTransform("http://www.w3.org/2001/10/xml-exc-c14n#");

            Element c14nMethod = XMLUtils.createElementInSignatureSpace(doc, "CanonicalizationMethod");
            c14nMethod.setAttributeNS(null, "Algorithm", "http://www.w3.org/2001/10/xml-exc-c14n#");

            Element signatureMethod = XMLUtils.createElementInSignatureSpace(doc, "SignatureMethod");
            signatureMethod.setAttributeNS(null, "Algorithm", signMethod);

            WSSecHeader secHeader = new WSSecHeader();
            secHeader.setMustUnderstand(true);
            secHeader.insertSecurityHeader(doc);

            XMLSignature sig = new XMLSignature(doc, "", signatureMethod, c14nMethod);
            sig.addDocument("#" + bodyId, transforms, digestMethod);
            sig.getSignedInfo().getSignatureMethodElement().setNodeValue("http://www.w3.org/2001/10/xml-exc-c14n#");

            secHeader.getSecurityHeader().appendChild(sig.getElement());
            header.appendChild(secHeader.getSecurityHeader());

            SecurityTokenReference reference = new SecurityTokenReference(doc);
            reference.setKeyIdentifier(x509Certificate);
            sig.getKeyInfo().addUnknownElement(reference.getElement());

            sig.sign(privateKey);
            String signedSoap = org.apache.ws.security.util.XMLUtils.PrettyDocumentToString(doc);

            return createSOAPFromString(signedSoap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean verifyXml(String xmlString) {
        KalkanProvider kalkanProvider = new KalkanProvider();
        Security.addProvider(kalkanProvider);
        KncaXS.loadXMLSecurity();

        boolean result = false;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509", kalkanProvider.getName());
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(wssCert)));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document doc = documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8)));

            Element sigElement;
            Element rootEl = (Element) doc.getFirstChild();
            NodeList list = rootEl.getElementsByTagName("ds:Signature");
            int length = list.getLength();
            for (int i = 0; i < length; i++) {
                Node sigNode = list.item(i);
                if (sigNode.getParentNode().getLocalName().contains("Security")) {
                    sigElement = (Element) sigNode;
                    XMLSignature signature = new XMLSignature(sigElement, "");
                    result = signature.checkSignatureValue(cert);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private SOAPMessage createSOAPFromString(String xmlString) {
        try {
            SOAPMessage message = MessageFactory.newInstance().createMessage();
            SOAPPart soapPart = message.getSOAPPart();

            ByteArrayInputStream stream = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
            StreamSource source = new StreamSource(stream);

            soapPart.setContent(source);
            stream.close();

            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
