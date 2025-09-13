// src/main/java/kz/ezdrav/eps_smartbridge_adapter/dto/SmartBridgeLogDto.java
package kz.ezdrav.eps_smartbridge_adapter.dto;

import lombok.Data;

@Data
public class SmartBridgeLogDto {
    private String serviceName;
    private String senderId;
    private String requestXml;
    private String responseXml;
    private String status;
    private String ipAddress;
}