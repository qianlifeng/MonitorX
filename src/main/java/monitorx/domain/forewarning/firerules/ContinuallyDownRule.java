package monitorx.domain.forewarning.firerules;

import monitorx.domain.forewarning.FireRuleContext;
import org.springframework.stereotype.Component;

/**
 * snippet continually returns false for x times
 */
@Component("firerule-continuallyDown")
public class ContinuallyDownRule extends BaseFireRule {

    private static int continuallyCount = 3;

    public boolean shouldFireNotify(FireRuleContext context) {
        if (isLastNotifyTooShort(context)) {
            return false;
        }

        if (context.getCheckPoints().size() >= continuallyCount) {
            boolean statisfied = true;
            for (int i = 0; i < continuallyCount; i++) {
                statisfied &= context.getCheckPoints().get(context.getCheckPoints().size() - 1 - i).getSnippetResult();
            }

            return statisfied;
        }

        return false;
    }
}
