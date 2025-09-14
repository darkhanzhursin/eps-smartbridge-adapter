package kz.ezdrav.eps_smartbridge_adapter.service;

import kz.ezdrav.eh.erdb_common.model.domain.ShepServiceEvent;
import kz.ezdrav.eh.erdb_common.repository.ShepServiceEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShepServiceEventService {
    private final ShepServiceEventRepository shepServiceEventRepository;

    @Transactional
    public void save(ShepServiceEvent shepServiceEvent) {
        shepServiceEventRepository.save(shepServiceEvent);
    }
}
