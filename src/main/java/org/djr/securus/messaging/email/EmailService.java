package org.djr.securus.messaging.email;

import com.codahale.metrics.annotation.Timed;
import com.djr4488.metrics.config.Configurator;
import org.djr.securus.CameraPostEvent;
import org.djr.securus.CameraUtilities;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;

/**
 * Created by djr4488 on 11/22/16.
 */

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Stateless
public class EmailService {
    @Inject
    private Configurator configurator;
    @Inject
    private Logger log;
    @Resource(name = "mail")
    private Session session;

    @Timed
    public void sendFileAttachmentEmail(CameraPostEvent cameraPostEvent, String destination) {
        log.debug("sendFileAttachmentEmail() cameraPostEvent:{}", cameraPostEvent);
        EmailConfig config = configurator.getConfiguration(EmailConfig.class);
        String from = config.from();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destination));
            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(buildPostEventMessage(cameraPostEvent));
            multipart.addBodyPart(messageBodyPart);
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(cameraPostEvent.getFile());
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(cameraPostEvent.getFile().getName());
            attachmentBodyPart.setHeader("content-type", "video/x-msvideo");
            multipart.addBodyPart(attachmentBodyPart);
            message.setSubject("Motion detected by camera " + cameraPostEvent.getCameraName());
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Timed
    public void sendNotifyEmail(String cameraName) {
        log.debug("sendNotifyEmail() cameraName:{}", cameraName);
        EmailConfig config = configurator.getConfiguration(EmailConfig.class);
        String to = config.destination();
        String from = config.from();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Camera has seen motion;");
            multipart.addBodyPart(messageBodyPart);
            message.setSubject("Motion detected by camera " + cameraName);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildPostEventMessage(CameraPostEvent cameraPostEvent) {
        StringBuffer stringBuffer = new StringBuffer("Camera ");
        DateTime now = DateTime.now();
        stringBuffer.append(cameraPostEvent.getCameraName())
                .append(" triggered by ")
                .append(cameraPostEvent.getxTriggerType())
                .append(". \n")
                .append("With details \n")
                .append("host camera: ").append(cameraPostEvent.getHost())
                .append("\n")
                .append("content length: ").append(cameraPostEvent.getContentLength())
                .append("\n")
                .append("timestamped: ").append(getDateAsString(cameraPostEvent))
                .append("\n")
                .append("event info: ").append(cameraPostEvent.getxEventInfo());
        return stringBuffer.toString();
    }

    private String getDateAsString(CameraPostEvent cameraPostEvent) {
        return CameraUtilities.getDateTime(cameraPostEvent).toString();
    }
}
