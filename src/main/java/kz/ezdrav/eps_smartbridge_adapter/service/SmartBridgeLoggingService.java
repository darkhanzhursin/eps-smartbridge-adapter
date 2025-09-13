// src/main/java/kz/ezdrav/eps_smartbridge_adapter/service/SmartBridgeLoggingService.java
package kz.ezdrav.eps_smartbridge_adapter.service;

import kz.ezdrav.eps_smartbridge_adapter.entity.SmartBridgeInteractionLog;
import kz.ezdrav.eps_smartbridge_adapter.repository.SmartBridgeInteractionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmartBridgeLoggingService {
    private final SmartBridgeInteractionLogRepository logRepository;

    @Transactional
    public SmartBridgeInteractionLog logInteraction(
        String serviceName,
        String senderId,
        String requestXml,
        String responseXml,
        String status,
        String ipAddress) {

        SmartBridgeInteractionLog log = new SmartBridgeInteractionLog();
        log.setServiceName(serviceName);
        log.setSenderId(senderId);
        log.setRequestXml(requestXml);
        log.setResponseXml(responseXml);
        log.setStatus(status);
        log.setIpAddress(ipAddress);

        return logRepository.save(log);
    }

    public String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            return xForwardedForHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}