package monitorx.domain.notifier;

public enum NotifierTypeEnum {
    WECHAT("wechat", "wexin"),
    EMAIL("email", "email");

    String code;
    String description;

    NotifierTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static NotifierTypeEnum getByCode(String code) {
        if ("wechat".equals(code)) return WECHAT;
        if ("email".equals(code)) return EMAIL;

        return null;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }
}
