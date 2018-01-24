package monitorx.plugins.forewarning.immediate;

import monitorx.plugins.annotation.UIField;
import monitorx.plugins.forewarning.IForewarningConfig;
import org.pf4j.Extension;

/**
 * @author qianlifeng
 */
@Extension
public class ImmediateForewarningConfig implements IForewarningConfig {
    /**
     * send warning for every x seconds
     */
    @UIField(code = "warningBySeconds", name = "Notify interval", description = "MonitorX will send notification for every x seconds")
    int warningBySeconds = 60;

    public int getWarningBySeconds() {
        return warningBySeconds;
    }

    public void setWarningBySeconds(int warningBySeconds) {
        this.warningBySeconds = warningBySeconds;
    }
}