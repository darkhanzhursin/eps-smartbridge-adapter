package kz.ezdrav.eps_smartbridge_adapter.webservice.endpoint;

import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import kz.ezdrav.eh.shep.syncchannel.v10.interfaces.ISyncChannel;


@WebService(targetNamespace = "http://bip.bee.kz/SyncChannel/v10/Interfaces")
@XmlSeeAlso({
    kz.ezdrav.eps_smartbridge_adapter.model.ws.common.ObjectFactory.class
})
public interface EpsEndpoint extends ISyncChannel {
}
