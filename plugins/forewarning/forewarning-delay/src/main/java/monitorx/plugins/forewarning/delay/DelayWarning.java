package monitorx.plugins.forewarning.delay;

import monitorx.plugins.forewarning.ForewarningCheckPoint;
import monitorx.plugins.forewarning.ForewarningContext;
import monitorx.plugins.forewarning.IForewarning;
import monitorx.plugins.forewarning.IForewarningConfig;
import org.pf4j.Extension;

/**
 * @author qianlifeng
 */
@Extension
public class DelayWarning implements IForewarning {

    @Override
    public String getCode() {
        return "forewarning-delay";
    }

    @Override
    public String getName() {
        return "Delay";
    }

    @Override
    public String getDescription() {
        return "send notification after n seconds delay if this forewarning is still true";
    }

    @Override
    public boolean shouldWarning(ForewarningContext context) {
        DelayForewarningConfig forewarningConfig = (DelayForewarningConfig) context.getForewarningConfig();

        if (context.hasCheckPoint()) {
            ForewarningCheckPoint lastCheckPoint = context.findLastCheckPoint();
            if (lastCheckPoint.getSnippetResult()) {
                ForewarningCheckPoint lastNotifiedCheckPoint = context.findLastNotifiedCheckPoint();
                if (lastNotifiedCheckPoint != null) {
                    long intervalBetweenLastAndLastNotifiedPoint = (lastCheckPoint.getDatetime().getTime() - lastNotifiedCheckPoint.getDatetime().getTime()) / 1000;
                    return intervalBetweenLastAndLastNotifiedPoint > forewarningConfig.getWarningBySeconds();
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public IForewarningConfig getForewarningConfig() {
        return new DelayForewarningConfig();
    }
}