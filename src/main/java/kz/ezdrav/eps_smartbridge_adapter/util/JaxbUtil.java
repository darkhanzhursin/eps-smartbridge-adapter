package kz.ezdrav.eh.erdb.util;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

public class JaxbUtil {
    public static Marshaller buildMarshaller() throws PropertyException {
        Jaxb2Marshaller marshallerFactory = buildMarshallerFactory();
        Marshaller marshaller = marshallerFactory.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        return marshaller;
    }

    private static Jaxb2Marshaller buildMarshallerFactory() {
        String[] packagesToScan = {
                "kz.ezdrav.eh.shep.syncchannel.v10.types",
                "kz.ezdrav.eh.shep.common.v10.types",
                "kz.ezdrav.eh.erdb.model.ws"
        };

        Jaxb2Marshaller marshallerFactory = new Jaxb2Marshaller();
        marshallerFactory.setPackagesToScan(packagesToScan);

        return marshallerFactory;
    }
}
