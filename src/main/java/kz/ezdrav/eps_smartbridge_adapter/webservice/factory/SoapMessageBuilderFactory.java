package kz.ezdrav.eps_smartbridge_adapter.webservice.factory;

import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.*;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SoapMessageBuilderFactory {
    private final Map<Class<?>, SoapMessageBuilder> builders;

    public SoapMessageBuilderFactory(
            GetReferralBuilder getReferralBuilder,
            DefectElementBuilder defectElementBuilder,
            ChekListBuilder chekListBuilder,
            GetRefferalByPeriodBuilder getRefferalByPeriodBuilder) {
        this.builders = Map.ofEntries(
                Map.entry(GetReferral.class, getReferralBuilder),
                Map.entry(DefectElement.class, defectElementBuilder),
                Map.entry(ChekList.class, chekListBuilder),
                Map.entry(GetRefferalByPeriod.class, getRefferalByPeriodBuilder)
        );
    }

    public SoapMessageBuilder getBuilder(Class<?> requestType) {
        return builders.get(requestType);
    }

    public boolean supports(Class<?> requestType) {
        return builders.containsKey(requestType);
    }
}
