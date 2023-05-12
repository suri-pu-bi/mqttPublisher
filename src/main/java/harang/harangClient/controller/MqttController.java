package harang.harangClient.controller;

import harang.harangClient.service.MyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mqtt")
@AllArgsConstructor
public class MqttController {

    private MyService myService;

    @PostMapping("/publish")
    public ResponseEntity<List<Map<String, Object>>> publishMessage() {
        List<Map<String, Object>> msg = myService.sendMessage();
        return ResponseEntity.ok(msg);
    }
}
