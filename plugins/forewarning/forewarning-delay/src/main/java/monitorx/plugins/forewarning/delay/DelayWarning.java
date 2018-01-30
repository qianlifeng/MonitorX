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
        return "<img src='http://ww1.sinaimg.cn/large/5d7c1fa4ly1fnyhe4ylirj20kv0613yl.jpg'/>";
    }

    @Override
    public boolean shouldWarning(ForewarningContext context) {
        DelayForewarningConfig forewarningConfig = (DelayForewarningConfig) context.getForewarningConfig();

        if (context.hasCheckPoint()) {
            ForewarningCheckPoint lastCheckPoint = context.findLastCheckPoint();
            if (lastCheckPoint.getSnippetResult()) {
                ForewarningCheckPoint lastUpCheckPoint = context.findLastUpCheckPoint();
                if (lastUpCheckPoint == null) {
                    lastUpCheckPoint = context.getCheckPoints().get(0);
                }
                long intervalBetweenLastUpAndCurrentDownPoint = (lastCheckPoint.getDatetime().getTime() - lastUpCheckPoint.getDatetime().getTime()) / 1000;
                if (intervalBetweenLastUpAndCurrentDownPoint > forewarningConfig.getDelaySeconds()) {
                    ForewarningCheckPoint lastNotifiedCheckPoint = context.findLastNotifiedCheckPoint();
                    if (lastNotifiedCheckPoint != null) {
                        long intervalBetweenLastAndLastNotifiedPoint = (lastCheckPoint.getDatetime().getTime() - lastNotifiedCheckPoint.getDatetime().getTime()) / 1000;
                        return intervalBetweenLastAndLastNotifiedPoint > forewarningConfig.getWarningBySeconds();
                    } else {
                        return true;
                    }
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