package monitorx.domain.forewarning;

import java.util.Date;

public class ForewarningCheckPoint {
    Date datetime;

    /**
     * The result that snippet return
     */
    boolean snippetResult;

    public boolean isSnippetResult() {
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
