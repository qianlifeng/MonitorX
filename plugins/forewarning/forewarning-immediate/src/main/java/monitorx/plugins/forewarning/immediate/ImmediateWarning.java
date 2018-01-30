package monitorx.plugins.forewarning.immediate;

import monitorx.plugins.forewarning.ForewarningCheckPoint;
import monitorx.plugins.forewarning.ForewarningContext;
import monitorx.plugins.forewarning.IForewarning;
import monitorx.plugins.forewarning.IForewarningConfig;
import org.pf4j.Extension;

/**
 * @author qianlifeng
 */
@Extension
public class ImmediateWarning implements IForewarning {

    @Override
    public String getCode() {
        return "forewarning-immediate";
    }

    @Override
    public String getName() {
        return "Immediate";
    }

    @Override
    public String getDescription() {
        return "<img src='http://ww1.sinaimg.cn/large/5d7c1fa4ly1fnyguw3uodj20fw04rgll.jpg'/>";
    }

    @Override
    public boolean shouldWarning(ForewarningContext context) {
        ImmediateForewarningConfig forewarningConfig = (ImmediateForewarningConfig) context.getForewarningConfig();

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
        return new ImmediateForewarningConfig();
    }
}