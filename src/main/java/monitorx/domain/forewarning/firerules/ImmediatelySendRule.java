package monitorx.domain.forewarning.firerules;

import monitorx.domain.forewarning.FireRuleContext;
import org.springframework.stereotype.Component;

@Component("firerule-immediately")
public class ImmediatelySendRule extends BaseFireRule {
    public boolean shouldFireNotify(FireRuleContext context) {
        if (isLastNotifyTooShort(context)) {
            return false;
        }

        if (context.getCheckPoints().size() > 0) {
            return context.getCheckPoints().get(context.getCheckPoints().size() - 1).getSnippetResult();
        }

        return false;
    }
}
