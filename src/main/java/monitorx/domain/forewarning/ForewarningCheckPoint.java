package monitorx.domain.forewarning;

import java.util.Date;

public class ForewarningCheckPoint {
    Date datetime;

    /**
     * The result that snippet return
     */
    boolean snippetResult;

    boolean hasSendNotify = false;

    public boolean isHasSendNotify() {
        return hasSendNotify;
    }

    public void setHasSendNotify(boolean hasSendNotify) {
        this.hasSendNotify = hasSendNotify;
    }


    public boolean getSnippetResult() {
        return snippetResult;
    }

    public void setSnippetResult(boolean snippetResult) {
        this.snippetResult = snippetResult;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
