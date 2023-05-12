package harang.harangClient.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// 직접 bean에 등록하지 않았기 때문에 @Service 필요
@Service
@AllArgsConstructor
public class MyService {
    @Autowired
    private MqttPublisher mqttPublisher;

    // 다른 서비스를 이용할 때 @Autowired 사용해줘야 함
    @Autowired
    private InfluxDBService influxDBService;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public List<Map<String, Object>> sendMessage() {
//        String message = "Hello, world!";
        List<Map<String, Object>> message = influxDBService.queryData();

        // withPayload: 매개변수로 전달된 객체를 메시지의 페이로드로 설정
        // 1. 메세지 builder를 통해 메세지 생성
        Message mqttMessage = MessageBuilder.withPayload(message).build();
        // 2. 메세지 발행
        mqttPublisher.publish(mqttMessage);
        return message;
    }
}
