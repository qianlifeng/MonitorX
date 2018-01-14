package monitorx.domain.forewarning;

public interface IFireRule {
    boolean shouldFireNotify(FireRuleContext context);
}
