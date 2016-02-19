package monitorx.domain.forewarning.firerules;

import monitorx.domain.forewarning.FireRuleContext;
import org.springframework.stereotype.Component;

/**
 * snippet continually returns false for x times
 */
@Component("firerule-continuallyDown")
public class ContinuallyDownRule extends BaseFireRule {
    public boolean isSatisfied(FireRuleContext context) {
        //todo:
        return true;
    }
}
