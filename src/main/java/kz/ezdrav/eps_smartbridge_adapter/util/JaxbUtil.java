package kz.ezdrav.eps_smartbridge_adapter.util;

import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

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
                "kz.ezdrav.eps_smartbridge_adapter.model.ws"
        };

        Jaxb2Marshaller marshallerFactory = new Jaxb2Marshaller();
        marshallerFactory.setPackagesToScan(packagesToScan);

        return marshallerFactory;
    }
}
