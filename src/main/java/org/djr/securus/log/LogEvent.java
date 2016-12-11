package org.djr.securus.log;

/**
 * Created by djr4488 on 12/10/16.
 */
public class LogEvent {
    private LogType logType;
    private String logMessage;
    private Object[] parameters;
    private Exception ex;

    public LogEvent() {
    }

    public LogEvent(LogType logType, String logMessage, Exception ex, Object... parameters) {
        this.logType = logType;
        this.logMessage = logMessage;
        this.ex = ex;
        this.parameters = parameters;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }
}
