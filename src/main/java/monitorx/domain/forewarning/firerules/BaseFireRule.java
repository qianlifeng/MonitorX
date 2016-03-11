package monitorx.domain.forewarning.firerules;

import monitorx.domain.forewarning.FireRuleContext;
import monitorx.domain.forewarning.ForewarningCheckPoint;
import monitorx.domain.forewarning.IFireRule;
import monitorx.domain.forewarning.IFireRuleConfig;

public abstract class BaseFireRule implements IFireRule {
    IFireRuleConfig config;

    public boolean isLastNotifyTooShort(FireRuleContext context) {
        if (context.getCheckPoints().size() == 1) return false;

        ForewarningCheckPoint lastNotifiedCheckPoint = findLastNotifiedCheckPoint(context);
        if (lastNotifiedCheckPoint != null) {
            ForewarningCheckPoint lastCheckPoint = context.getCheckPoints().get(context.getCheckPoints().size() - 1);
            long minutes = (lastCheckPoint.getDatetime().getTime() - lastNotifiedCheckPoint.getDatetime().getTime()) / (1000 * 60);
            if (minutes < 10) {
                return true;
            }
        }

        return false;
    }

    protected boolean isLastCheckPointReturnTrue(FireRuleContext context) {
        if (context.getCheckPoints().size() > 0) {
            return context.getCheckPoints().get(context.getCheckPoints().size() - 1).getSnippetResult();
        }

        return false;
    }

    private ForewarningCheckPoint findLastNotifiedCheckPoint(FireRuleContext context) {
        for (int i = context.getCheckPoints().size() - 1; i >= 0; i--) {
            ForewarningCheckPoint point = context.getCheckPoints().get(i);
            if (point.getHasSendNotify()) return point;
        }

        return null;
    }
}
