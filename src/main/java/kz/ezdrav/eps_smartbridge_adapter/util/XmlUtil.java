package kz.ezdrav.eh.erdb.util;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.StringWriter;

public class XmlUtil {
    public static <T> String serializeNoRoot(T obj, Class<T> clazz,
                                             String namespace,
                                             String localPart,
                                             String prefix) {
        try {
            Marshaller marshaller = JaxbUtil.buildMarshaller();
            StringWriter result = new StringWriter();

            JAXBElement<T> jaxbElement =
                    new JAXBElement<>(new QName(namespace, localPart, prefix), clazz, obj);
            marshaller.marshal(jaxbElement, result);

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
