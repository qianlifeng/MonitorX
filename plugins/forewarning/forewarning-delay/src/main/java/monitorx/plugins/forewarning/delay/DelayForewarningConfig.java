package monitorx.plugins.forewarning.delay;

import monitorx.plugins.annotation.UIField;
import monitorx.plugins.forewarning.IForewarningConfig;
import org.pf4j.Extension;

/**
 * @author qianlifeng
 */
@Extension
public class DelayForewarningConfig implements IForewarningConfig {
    /**
     * send warning for every x seconds
     */
    @UIField(code = "warningBySeconds", name = "Notify interval", description = "MonitorX will send notification for every x seconds")
    int warningBySeconds = 60;

    /**
     * send warning for every x seconds
     */
    @UIField(code = "delaySeconds", name = "Delay seconds", description = "MonitorX will send notification if this forewarning still returns true after x seconds delay")
    int delaySeconds = 60;

    public int getWarningBySeconds() {
        return warningBySeconds;
    }

    public void setWarningBySeconds(int warningBySeconds) {
        this.warningBySeconds = warningBySeconds;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }
}