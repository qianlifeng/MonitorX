package monitorx.domain.forewarning;

import java.util.List;

public class FireRuleContext {
    List<ForewarningCheckPoint> checkPoints;

    public List<ForewarningCheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(List<ForewarningCheckPoint> checkPoints) {
        this.checkPoints = checkPoints;
    }
}
