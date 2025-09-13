package kz.ezdrav.eps_smartbridge_adapter.aspect;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import kz.ezdrav.eh.erdb_common.model.domain.ShepServiceEvent;
import kz.ezdrav.eh.shep.enums.ShepStatusCode;
import kz.ezdrav.eh.shep.syncchannel.v10.types.request.SyncSendMessageRequest;
import kz.ezdrav.eh.shep.syncchannel.v10.types.response.SyncSendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class SaveShepServiceEventAspect {
    private final ShepServiceEventService shepServiceEventService;
    private final ThreadLocal<ShepServiceEventParams> shepServiceEventParamsHolder;
    private static final String DEFAULT_SUCCESS_RESPONSE = "SUCCESS";
    private static final String DEFAULT_ERROR_PREFIX     = "ERROR: ";

    @Around("@annotation(kz.ezdrav.eh.erdb_onko.annotation.SaveShepServiceEvent)")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        SyncSendMessageRequest request = extractRequest(joinPoint);

        ShepServiceEvent event = buildEvent(request);

        try {
            result = joinPoint.proceed();
            SyncSendMessageResponse response = (SyncSendMessageResponse) result;
            setResponseFields(event, response);
            setThreadLocalFields(event);
            return result;
        } catch (Throwable ex) {
            event.setIsSuccess(false);
            event.setResponse(buildErrorResponse(ex));

            setThreadLocalFieldsSafe(event);
            throw ex;
        } finally {
            shepServiceEventParamsHolder.remove();
            shepServiceEventService.save(event);
        }
    }

    private ShepServiceEvent buildEvent(SyncSendMessageRequest request) {
        ShepServiceEvent event = new ShepServiceEvent();
        event.setServiceId(request.getRequestInfo().getServiceId());
        event.setSenderId(request.getRequestInfo().getSender().getSenderId());
        event.setRequest(serializeRequest(request));
        event.setClientIp(getRequestClientIp());
        event.setIsSuccess(true);
        return event;
    }

    private void setResponseFields(ShepServiceEvent event, SyncSendMessageResponse response) {
        setResponseSuccess(event, response);
        String serialized = serializeResponse(response);
        event.setResponse(StringUtils.hasText(serialized) ? serialized : DEFAULT_SUCCESS_RESPONSE);
    }

    private void setThreadLocalFields(ShepServiceEvent event) {
        ShepServiceEventParams p = shepServiceEventParamsHolder.get();
        if (p != null) {
            event.setEvent(p.getEvent());
            event.setIin(p.getIin());
        }
    }

    private void setThreadLocalFieldsSafe(ShepServiceEvent event) {
        setThreadLocalFields(event);
    }

    private void setResponseSuccess(ShepServiceEvent event, SyncSendMessageResponse response) {
        if (response.getResponseInfo() != null &&
                response.getResponseInfo().getStatus() != null &&
                ShepStatusCode.ERROR.name().equals(response.getResponseInfo().getStatus().getCode())) {
            event.setIsSuccess(false);
            return;
        }

        event.setIsSuccess(true);
    }

    private SyncSendMessageRequest extractRequest(JoinPoint joinPoint) {
        Object arg0 = joinPoint.getArgs()[0];
        if (!(arg0 instanceof SyncSendMessageRequest)) {
            throw new IllegalOnkoArgumentException("Ожидается первый аргумент типа SyncSendMessageRequest");
        }
        return (SyncSendMessageRequest) arg0;
    }

    private String getRequestClientIp() {
        return getCurrentHttpRequest()
                .map(this::extractIp)
                .orElse("");
    }

    private static Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional
                .ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }

    private String extractIp(HttpServletRequest request) {
        var clientIp = request.getHeader("X-FORWARDED-FOR");
        if (StringUtils.hasText(clientIp)) {
            return clientIp.split(",")[0];
        }
        return request.getRemoteAddr().split(",")[0];
    }

    private String serializeRequest(SyncSendMessageRequest request) {
        try {
            return XmlUtil.serializeNoRoot(
                    request,
                    SyncSendMessageRequest.class,
                    "",
                    "request",
                    ""
            );
        } catch (RuntimeException re) {
            String cause = re.getCause() != null
                    ? re.getCause().getClass().getSimpleName()
                    : re.getClass().getSimpleName();
            return DEFAULT_ERROR_PREFIX + "serializeRequest " + cause;
        }
    }

    private String serializeResponse(SyncSendMessageResponse response) {
        try {
            return XmlUtil.serializeNoRoot(
                    response,
                    SyncSendMessageResponse.class,
                    "",
                    "response",
                    ""
            );
        } catch (RuntimeException re) {
            String cause = re.getCause() != null
                    ? re.getCause().getClass().getSimpleName()
                    : re.getClass().getSimpleName();
            return DEFAULT_SUCCESS_RESPONSE + " (audit: " + cause + ")";
        }
    }

    private String buildErrorResponse(Throwable ex) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("ERROR: ")
                .append(ex.getClass().getSimpleName());
        if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
            sb.append(": ").append(trimTo(ex.getMessage(), 1000));
        }
        return sb.toString();
    }

    private static String trimTo(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}
