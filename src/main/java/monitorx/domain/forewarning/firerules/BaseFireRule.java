package monitorx.domain.forewarning.firerules;

import monitorx.domain.forewarning.FireRuleContext;
import monitorx.domain.forewarning.ForewarningCheckPoint;
import monitorx.domain.forewarning.IFireRule;
import monitorx.domain.forewarning.IFireRuleConfig;

public abstract class BaseFireRule implements IFireRule {
    IFireRuleConfig config;

    public boolean isLastNotifyTooShort(FireRuleContext context) {
        if (context.getCheckPoints().size() == 1) return false;

        ForewarningCheckPoint lastNotifyCheckPoint = findLastNotifyCheckPoint(context);
        if (lastNotifyCheckPoint != null) {
            ForewarningCheckPoint lastCheckPoint = context.getCheckPoints().get(context.getCheckPoints().size() - 1);
            long minutes = (lastCheckPoint.getDatetime().getTime() - lastNotifyCheckPoint.getDatetime().getTime()) / (1000 * 60); //minutes
            if (minutes < 5) {
                return true;
            }
        }

        return false;
    }

    private ForewarningCheckPoint findLastNotifyCheckPoint(FireRuleContext context) {
        for (int i = context.getCheckPoints().size() - 1; i >= 0; i--) {
            ForewarningCheckPoint point = context.getCheckPoints().get(i);
            if (point.hasSendNotify()) return point;
        }

        return null;
    }
}
