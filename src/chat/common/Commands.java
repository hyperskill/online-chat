package chat.common;

public enum Commands {

    EXIT("/exit"), LIST("/list");

    Commands(String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }
}
