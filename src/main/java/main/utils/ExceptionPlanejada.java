package main.utils;

public class ExceptionPlanejada extends Exception {

    private String title;

    public ExceptionPlanejada(String title) {
        super(title);
        this.title = title;
    }

    public ExceptionPlanejada(String message, String title) {
        super(message);
        this.title = title;
    }

    public ExceptionPlanejada(String message, Throwable cause, String title) {
        super(message, cause);
        this.title = title;
    }

    public ExceptionPlanejada(Throwable cause, String title) {
        super(cause);
        this.title = title;
    }

    public ExceptionPlanejada(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String title) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
