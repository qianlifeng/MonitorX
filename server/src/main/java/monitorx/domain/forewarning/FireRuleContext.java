package monitorx.domain.forewarning;

import java.util.ArrayList;
import java.util.List;

public class FireRuleContext {
    List<ForewarningCheckPoint> checkPoints = new ArrayList<ForewarningCheckPoint>();

    public List<ForewarningCheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(List<ForewarningCheckPoint> checkPoints) {
        this.checkPoints = checkPoints;
    }

    public void addCheckPoint(ForewarningCheckPoint point) {
        checkPoints.add(point);
    }
}