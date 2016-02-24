package monitorx.domain.forewarning.firerules;

import monitorx.domain.forewarning.FireRuleContext;
import monitorx.domain.forewarning.IFireRule;
import monitorx.domain.forewarning.IFireRuleConfig;

public abstract class BaseFireRule implements IFireRule {
    IFireRuleConfig config;

    public boolean isLastNotifyTooShort(FireRuleContext context) {
        if (context.getCheckPoints().size() == 1) return false;

        //todo
        return false;
    }

//    private ForewarningCheckPoint findLastNotifyCheckPoint(FireRuleContext context) {
//        for (int i = context.getCheckPoints().size() - 1; i >= 0; i--) {
//            ForewarningCheckPoint point = context.getCheckPoints().get(i);
//
//        }
//    }
}
