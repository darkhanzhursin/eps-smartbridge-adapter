package kz.ezdrav.eps_smartbridge_adapter.model.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShepServiceEventParams {
    private String event;
    private String iin;
}
