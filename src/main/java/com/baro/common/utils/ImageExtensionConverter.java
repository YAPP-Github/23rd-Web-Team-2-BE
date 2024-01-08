package com.baro.common.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

public class ImageExtensionConverter {

    public static byte[] toJpeg(MultipartFile image) throws IOException {
        BufferedImage originalImage = ImageIO.read(image.getInputStream());
        BufferedImage jpegImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = jpegImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, null);
        graphics.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(jpegImage, "jpeg", baos);
        baos.close();

        return baos.toByteArray();
    }
}
