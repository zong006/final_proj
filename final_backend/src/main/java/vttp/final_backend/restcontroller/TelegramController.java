package vttp.final_backend.restcontroller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp.final_backend.service.TelegramService;

@RestController
@RequestMapping("/api/telegram")
public class TelegramController {

    @Autowired
    private TelegramService telegramService;

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> update) throws IOException, ParseException {
        System.out.println("telegram controller");
        telegramService.processUpdate(update);
        return ResponseEntity.ok("Received");
    }
}

// https://api.telegram.org/bot7836509893:AAF57TZsyKwkamNQ66IJJGKscXMxGggEXzk/setWebhook?url=https://dae4-119-74-30-128.ngrok-free.app/telegram/webhook
