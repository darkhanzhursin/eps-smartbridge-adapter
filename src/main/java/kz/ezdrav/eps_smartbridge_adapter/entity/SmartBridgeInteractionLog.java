// src/main/java/kz/ezdrav/eps_smartbridge_adapter/entity/SmartBridgeInteractionLog.java
package kz.ezdrav.eps_smartbridge_adapter.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "smartbridge_interaction_log")
@Getter
@Setter
public class SmartBridgeInteractionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;

    @Column(name = "senderid", length = 100)
    private String senderId;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "request_xml", columnDefinition = "NVARCHAR(MAX)")
    private String requestXml;

    @Column(name = "response_xml", columnDefinition = "NVARCHAR(MAX)")
    private String responseXml;

    @Column(length = 255)
    private String status;

    @Column(length = 16)
    private String ipAddress;
}