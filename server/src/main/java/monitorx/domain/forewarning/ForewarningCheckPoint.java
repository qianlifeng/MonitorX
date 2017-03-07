package monitorx.domain.forewarning;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ForewarningCheckPoint {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date datetime;
    /**
     * The result that snippet return
     */
    boolean snippetResult;

    boolean hasSendNotify = false;

    public boolean getHasSendNotify() {
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
