package monitorx.domain.forewarning.firerules;

import monitorx.domain.forewarning.FireRuleContext;
import org.springframework.stereotype.Component;

@Component("firerule-immediately")
public class ImmediatelySendRule extends BaseFireRule {
    public boolean isSatisfied(FireRuleContext context) {
        return true;
    }
}
