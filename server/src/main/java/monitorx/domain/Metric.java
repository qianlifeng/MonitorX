package monitorx.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Metric {
    String title;
    String type;
    String value;
    List<String> historyValue;
    Double width = 0.5;
    String context;

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public List<String> getHistoryValue() {
        return historyValue;
    }

    public void setHistoryValue(List<String> historyValue) {
        this.historyValue = historyValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @JsonIgnore
    public boolean isLineMetric() {
        return type.equals("line");
    }


}