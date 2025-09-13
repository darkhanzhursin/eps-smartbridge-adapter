// src/main/java/kz/ezdrav/eps_smartbridge_adapter/repository/SmartBridgeInteractionLogRepository.java
package kz.ezdrav.eps_smartbridge_adapter.repository;

import kz.ezdrav.eps_smartbridge_adapter.entity.SmartBridgeInteractionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmartBridgeInteractionLogRepository extends JpaRepository<SmartBridgeInteractionLog, Long> {
}