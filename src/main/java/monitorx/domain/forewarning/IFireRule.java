package monitorx.domain.forewarning;

public interface IFireRule {
    boolean isSatisfied(FireRuleContext context);
}
