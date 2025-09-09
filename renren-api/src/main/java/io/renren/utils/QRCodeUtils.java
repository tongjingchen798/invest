package io.renren.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 *
 * @author renren
 * @date 2024-01-01
 */
public class QRCodeUtils {
    
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private static final String DEFAULT_FORMAT = "PNG";
    
    /**
     * 生成二维码并返回Base64编码的字符串
     *
     * @param content 二维码内容
     * @return Base64编码的二维码图片
     */
    public static String generateQRCodeBase64(String content) {
        return generateQRCodeBase64(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    /**
     * 生成二维码并返回Base64编码的字符串
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @return Base64编码的二维码图片
     */
    public static String generateQRCodeBase64(String content, int width, int height) {
        try {
            BufferedImage image = generateQRCodeImage(content, width, height);
            return imageToBase64(image);
        } catch (Exception e) {
            throw new RenException(ErrorCode.GENERATE_QR_CODE_FAILED);
        }
    }
    
    /**
     * 生成二维码图片
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @return BufferedImage对象
     */
    public static BufferedImage generateQRCodeImage(String content, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    /**
     * 将BufferedImage转换为Base64字符串
     *
     * @param image 图片对象
     * @return Base64编码的字符串
     */
    private static String imageToBase64(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            javax.imageio.ImageIO.write(image, DEFAULT_FORMAT, baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RenException(ErrorCode.IMAGE_CONVERSION_FAILED);
        }
    }
    
    /**
     * 生成USDT转账二维码
     *
     * @param usdtAddress USDT-TRC20地址
     * @param amount      转账金额（可选）
     * @return Base64编码的二维码图片
     */
    public static String generateUSDTQRCode(String usdtAddress, String amount) {
        String content;
        if (amount != null && !amount.isEmpty()) {
            // 包含金额的USDT转账链接
            content = String.format("tron:%s?amount=%s", usdtAddress, amount);
        } else {
            // 仅包含地址的USDT转账链接
            content = String.format("tron:%s", usdtAddress);
        }
        return generateQRCodeBase64(content);
    }
}
