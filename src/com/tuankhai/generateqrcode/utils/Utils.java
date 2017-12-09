/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuankhai.generateqrcode.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author khai.it
 */
public class Utils {

    private static boolean isRun = false;

    private static String LOCATION = "D://GenerateQRCode/";

    public static void generateImage(String input) throws IOException {
        if (input.trim().isEmpty()) {
            return;
        }
        // Numeric mode encoding (3.33 bits per digit)
        QrCode qr = QrCode.encodeText(input, QrCode.Ecc.HIGH);
        writePng(qr.toImage(23, 3), LOCATION + input + ".png");
    }

    private static void writePng(BufferedImage img, String filepath) throws IOException {
        ImageIO.write(img, "png", new File(filepath));
    }

    public static String[] splitString(String input) {
        input.trim();
        input.replace((char) 13, ' ');
        input.replace((char) 10, ' ');
        input.replace("\n", " ");
        input.replace("\t", " ");
        input.replace(".", " ");
        input.replace(",", " ");
        input.replaceAll(" +", " ");
        return input.split(" ");
    }

    public static void createFolder() {
        File theDir = new File("D://GenerateQRCode");
        if (!theDir.exists()) {
            System.out.println("Creating directory: " + theDir.getName());
            boolean result = false;
            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
            if (result) {
                System.out.println("DIR created");
            }
        }

    }

    synchronized public static boolean getIsRun() {
        return isRun;
    }

    synchronized public static void setIsRun(boolean flag) {
        isRun = flag;
    }
}
