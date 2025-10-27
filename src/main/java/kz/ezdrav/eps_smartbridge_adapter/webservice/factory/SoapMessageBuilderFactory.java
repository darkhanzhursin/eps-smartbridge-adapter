package kz.ezdrav.eps_smartbridge_adapter.webservice.factory;

import kz.ezdrav.eps_smartbridge_adapter.model.ws.eps.*;
import kz.ezdrav.eps_smartbridge_adapter.webservice.factory.builders.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SoapMessageBuilderFactory {
    private final Map<Class<?>, SoapMessageBuilder> builders;

    public SoapMessageBuilderFactory(
            FinanceSourceBuilder financeSourceBuilder,
            GetCtrlBuilder getCtrlBuilder,
            GetData2Builder getData2Builder,
            GetDataByIDsBuilder getDataByIDsBuilder,
            GetDataByPeriodBuilder getDataByPeriodBuilder,
            GetIdByDateBuilder getIdByDateBuilder,
            GetIDsBuilder getIDsBuilder,
            GetReferralBuilder getReferralBuilder,
            GetRefferalByIDBuilder getRefferalByIDBuilder,
            GetRefferalByPersonBuilder getRefferalByPersonBuilder,
            GetServicesByDateBuilder getServicesByDateBuilder,
            SetDataBuilder setDataBuilder,
            SetDataAmbBuilder setDataAmbBuilder,
            SetDefectsBuilder setDefectsBuilder,
            SetReferralBuilder setReferralBuilder,
            DefectElementBuilder defectElementBuilder,
            ChekListBuilder chekListBuilder,
            GetRefferalByPeriodBuilder getRefferalByPeriodBuilder) {
        this.builders = Map.ofEntries(
                Map.entry(FinanceSource.class, financeSourceBuilder),
                Map.entry(GetCtrl.class, getCtrlBuilder),
                Map.entry(GetData2.class, getData2Builder),
                Map.entry(GetDataByIDs.class, getDataByIDsBuilder),
                Map.entry(GetDataByPeriod.class, getDataByPeriodBuilder),
                Map.entry(GetIdByDate.class, getIdByDateBuilder),
                Map.entry(GetIDs.class, getIDsBuilder),
                Map.entry(GetReferral.class, getReferralBuilder),
                Map.entry(GetRefferalByID.class, getRefferalByIDBuilder),
                Map.entry(GetRefferalByPerson.class, getRefferalByPersonBuilder),
                Map.entry(GetServicesByDate.class, getServicesByDateBuilder),
                Map.entry(SetData.class, setDataBuilder),
                Map.entry(SetDataAmb.class, setDataAmbBuilder),
                Map.entry(SetDefects.class, setDefectsBuilder),
                Map.entry(SetReferral.class, setReferralBuilder),
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
