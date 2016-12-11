package org.djr.securus.log;

import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Created by djr4488 on 12/10/16.
 */
@ApplicationScoped
public class ApplicationLogger {
    @Inject
    private Logger log;

    public void logException(@Observes LogEvent logEvent) {
        switch (logEvent.getLogType()) {
            case ERROR: {
                log.error(logEvent.getLogMessage(), logEvent.getParameters());
                break;
            }
            case DEBUG: {
                log.debug(logEvent.getLogMessage(), logEvent.getParameters());
                break;
            }
            case INFO: {
                log.info(logEvent.getLogMessage(), logEvent.getParameters());
                break;
            }
            case TRACE: {
                log.trace(logEvent.getLogMessage(), logEvent.getParameters());
            }
        }
    }
}
