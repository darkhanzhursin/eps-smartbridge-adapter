package kz.ezdrav.eps_smartbridge_adapter.webservice.endpoint.impl;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import kz.ezdrav.eh.shep.syncchannel.v10.interfaces.SendMessageSendMessageFaultMsg;
import kz.ezdrav.eh.shep.syncchannel.v10.types.request.SyncSendMessageRequest;
import kz.ezdrav.eh.shep.syncchannel.v10.types.response.SyncSendMessageResponse;
import kz.ezdrav.eh.shep.util.ShepUtil;
import kz.ezdrav.eps_smartbridge_adapter.annotation.SaveShepServiceEvent;
import kz.ezdrav.eps_smartbridge_adapter.exception.GeneralException;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.EpsRequest;
import kz.ezdrav.eps_smartbridge_adapter.model.ws.common.GeneralInfoResponse;
import kz.ezdrav.eps_smartbridge_adapter.service.SmartBridgeLoggingService;
import kz.ezdrav.eps_smartbridge_adapter.webservice.endpoint.EpsEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@WebService(serviceName = "EpsService",
    targetNamespace = "http://bip.bee.kz/SyncChannel/v10/Interfaces",
    endpointInterface = "kz.ezdrav.eps_smartbridge_adapter.webservice.endpoint.EpsEndpoint")
@Component
@RequiredArgsConstructor
@Slf4j
public class EpsEndpointImpl implements EpsEndpoint {

    private final SmartBridgeLoggingService loggingService;
    private final HttpServletRequest httpServletRequest;

    @Override
    @SaveShepServiceEvent
    public SyncSendMessageResponse sendMessage(SyncSendMessageRequest request) throws SendMessageSendMessageFaultMsg {
        log.info("Received SOAP request: {}", request);
        return handleRequest(request);
    }

    private SyncSendMessageResponse handleRequest(SyncSendMessageRequest request) {
        log.debug("Handling request");
        Object data = request.getRequestData().getData();

        try {
            if (data instanceof EpsRequest) {
                return handleEpsRequest(request);
            }
        }
        catch (GeneralException e) {
            return ShepUtil.buildErrorSyncSendMessageResponse(GeneralInfoResponse.buildError(e));
        }
        catch (Exception e) {
            log.error("Error occurred while processing request: ", e);
            return ShepUtil.buildErrorSyncSendMessageResponse(GeneralInfoResponse.buildError(e));
        }

        return ShepUtil.buildErrorSyncSendMessageResponse(
            GeneralInfoResponse.buildError("Данный тип запроса не поддерживается")
        );
    }

    private SyncSendMessageResponse handleEpsRequest(SyncSendMessageRequest request) {

        return ShepUtil.buildSuccessSyncSendMessageResponse("Success");
    }
}
