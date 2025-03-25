package vttp.final_backend.restcontroller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@RestController
@RequestMapping("/api/qr")
public class QrController {
    
    private static final String TELEGRAM_BOT_USERNAME = "doomscrollerrr_bot";

    @GetMapping(value = "/generate/{userId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@PathVariable String userId) throws WriterException, IOException {
        String telegramLink = "https://t.me/" + TELEGRAM_BOT_USERNAME + "?start=" + userId;
        int width = 300;
        int height = 300;
        System.out.println("getting qr...");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(telegramLink, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] qrImage = pngOutputStream.toByteArray();

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrImage);
    }
}



// import com.google.zxing.BarcodeFormat;
// import com.google.zxing.WriterException;
// import com.google.zxing.client.j2se.MatrixToImageWriter;
// import com.google.zxing.common.BitMatrix;
// import com.google.zxing.qrcode.QRCodeWriter;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.http.ResponseEntity;
// import org.springframework.http.MediaType;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.nio.file.FileSystems;
// import java.nio.file.Path;

// @RestController
// @RequestMapping("/qr")
// public class QRCodeController {

//     private static final String TELEGRAM_BOT_USERNAME = "YourTelegramBot"; 

//     @GetMapping(value = "/generate/{userId}", produces = MediaType.IMAGE_PNG_VALUE)
//     public ResponseEntity<byte[]> generateQRCode(@PathVariable String userId) throws WriterException, IOException {
//         String telegramLink = "https://t.me/" + TELEGRAM_BOT_USERNAME + "?start=" + userId;
//         int width = 300;
//         int height = 300;

//         QRCodeWriter qrCodeWriter = new QRCodeWriter();
//         BitMatrix bitMatrix = qrCodeWriter.encode(telegramLink, BarcodeFormat.QR_CODE, width, height);

//         ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//         MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
//         byte[] qrImage = pngOutputStream.toByteArray();

//         return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrImage);
//     }
// }
