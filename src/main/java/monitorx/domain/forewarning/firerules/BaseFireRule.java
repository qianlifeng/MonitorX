package monitorx.domain.forewarning.firerules;

import monitorx.domain.forewarning.IFireRule;
import monitorx.domain.forewarning.IFireRuleConfig;

public abstract class BaseFireRule implements IFireRule {
    IFireRuleConfig config;
}
