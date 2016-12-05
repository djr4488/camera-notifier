package org.djr.camera.cdi.logs;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Created by djr4488 on 11/22/16.
 */
@ApplicationScoped
public class LoggerProducer {
    private static final Logger log = LoggerFactory.getLogger(LoggerProducer.class);

    public LoggerProducer() {
    }

    @PostConstruct
    private void loadConfiguration() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//        try {
//            JoranConfigurator configurator = new JoranConfigurator();
//            configurator.setContext(context);
//            context.reset();
//            configurator.doConfigure("/app/camera/conf/logback.xml");
//        } catch (JoranException je) {
//            // StatusPrinter will handle this
//        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }


    @Produces
    public Logger getLogger(InjectionPoint ip) {
        Class<?> injectingClass = ip.getMember().getDeclaringClass();
        log.debug("getLogger() injectingClass:{}", injectingClass.getName());
        return LoggerFactory.getLogger(injectingClass);
    }
}