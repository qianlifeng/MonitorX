package monitorx.domain.forewarning.firerules;

import monitorx.domain.forewarning.FireRuleContext;

public class ImmediatelySendRule extends BaseFireRule {
    public boolean isSatisfied(FireRuleContext context) {
        return true;
    }
}
