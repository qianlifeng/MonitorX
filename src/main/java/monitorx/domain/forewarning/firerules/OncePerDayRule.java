package monitorx.domain.forewarning.firerules;

import monitorx.domain.forewarning.FireRuleContext;
import monitorx.domain.forewarning.ForewarningCheckPoint;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("firerule-oncePerDay")
public class OncePerDayRule extends BaseFireRule {

    public boolean shouldFireNotify(FireRuleContext context) {
        return !checkIfTodayFired(context.getCheckPoints());
    }

    private boolean checkIfTodayFired(List<ForewarningCheckPoint> checkPoints) {
        for (ForewarningCheckPoint point : checkPoints) {
            if (DateUtils.isSameDay(new Date(), point.getDatetime())) {
                if (point.hasSendNotify()) {
                    return true;
                }
            }
        }

        return false;
    }
}