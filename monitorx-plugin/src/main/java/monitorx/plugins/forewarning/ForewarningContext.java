package monitorx.plugins.forewarning;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qianlifeng
 */
public class ForewarningContext {
    IForewarningConfig forewarningConfig;

    List<ForewarningCheckPoint> checkPoints = new ArrayList<ForewarningCheckPoint>();

    public IForewarningConfig getForewarningConfig() {
        return forewarningConfig;
    }

    public void setForewarningConfig(IForewarningConfig forewarningConfig) {
        this.forewarningConfig = forewarningConfig;
    }

    public List<ForewarningCheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(List<ForewarningCheckPoint> checkPoints) {
        this.checkPoints = checkPoints;
    }

    public void addCheckPoint(ForewarningCheckPoint point) {
        checkPoints.add(point);
    }

    public ForewarningCheckPoint findLastCheckPoint() {
        if (hasCheckPoint()) {
            return getCheckPoints().get(getCheckPoints().size() - 1);
        }

        return null;
    }

    public boolean hasCheckPoint() {
        return getCheckPoints().size() > 0;
    }

    public ForewarningCheckPoint findLastNotifiedCheckPoint() {
        if (hasCheckPoint()) {
            for (int i = getCheckPoints().size() - 1; i >= 0; i--) {
                ForewarningCheckPoint point = getCheckPoints().get(i);
                if (point.getHasSendNotify()) {
                    return point;
                }
            }
        }

        return null;
    }
}