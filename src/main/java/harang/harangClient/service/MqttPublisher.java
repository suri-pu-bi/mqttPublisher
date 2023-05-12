package harang.harangClient.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

// @Bean으로 등록했기 때문에 @Service 필요 X
public class MqttPublisher {
    // @Autowired: 클래스의 객체를 스프링 컨테이너에서 자동으로 주입받을 때 사용


// @Autowired는 @Bean으로 등록된 객체를 자동으로 주입
// @Autowired를 사용하지 않으면, 생성자를 통해 객체 주입

    // Handler NullPoint 발생 에러 해결 => @Autowired 주입
    // 의문점? 나는 handler를 추가한 적이 없어!
    // mqttPublisher() 메소드를 호출하여 MqttPublisher 객체를 생성할 때 handler 객체를 생성자를 통해 전달
    // 그리고 handler 객체는 MqttPahoMessageHandler(clientId, mqttPahoClientFactory())를 호출하여 생성
    // mqttPahoClientFactory()가 이미 MqttPahoClientFactory 빈을 생성하여 반환하고 있음
    // handler 객체가 생성될 때 이 빈이 함께 사용
    //따라서 handler 빈을 따로 등록하지 않아도 MqttPahoMessageHandler 객체를 생성할 때
    // mqttPahoClientFactory()에서 반환되는 MqttPahoClientFactory 빈이 함께 사용되므로 @Autowired 필요!

    @Autowired
    private final MessageHandler mqttMessageHandler;


    // MessageHandler를 인자로 받는 생성자 -> @Bean에서 return할 때 사용
    // harang.harangClient.MqttConfig ... return new MqttPublisher(handler);
    public MqttPublisher(MessageHandler mqttMessageHandler) {
        this.mqttMessageHandler = mqttMessageHandler;
    }


    // 주어진 topic에 대해 주어진 payload를 Mqtt broker에 발행
    // payload: Mqtt 메세지에 실제로 포함될 데이터 ex) 센서에서 측정한 온도나 습도 값
    public void publish(Message message) {
        // handleMessage() -> 메세지 발행
        // 3. mqttMessageHandler.handleMessage() 메소드를 호출하여 MQTT 메시지를 발행
        mqttMessageHandler.handleMessage(MessageBuilder
                // Mqtt 메세지 페이로드 설정
                .withPayload(message.getPayload())
                // Mqtt 메세지 헤더 설정, 헤더에 발행할 토픽 이름 설정
                .setHeader(MqttHeaders.TOPIC, "harang/data")
                .build());
    }


}
