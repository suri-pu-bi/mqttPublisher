package harang.harangClient;

import harang.harangClient.service.MqttPublisher;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    // MqttConnectOptions 빈 생성
    // Mqtt Client와 Mqtt broker 간의 연결 설정을 위해 사용
    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        // brokerUrl 프로퍼티값을 사용하여 Mqtt broker URL을 설정
        options.setServerURIs(new String[]{brokerUrl});
        return options;
    }
    // MqttPahoCLientFactory 빈 생성
    // Mqtt Client를 생성하고, Mqtt 연결에 필요한 MqttConnectOptions를 설정
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        // MqttConnectOptions() 메소드를 호출하여 MqttConnectOptions 객체를 가져온 후
        // MqttPahoClientFactory의 commectionOptions 프로퍼티에 설정

        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

//    1. MqttPublisher 객체를 검색하기 위해 @Bean으로 등록된 메서드 중에 리턴 타입이 MqttPublisher인 메서드를 찾는다
//    2. 해당 메서드가 호출되면 new MqttPublisher(handler) 코드에 의해 객체가 생성되어 리턴
//    3. 이후 검색된 MqttPublisher 객체를 주입받는 코드에서는 해당 객체를 사용가능
    @Bean
    public MqttPublisher mqttPublisher() {
        // 클라이언트 id: 메세지를 발행하는 클라이언트를 고유하게 식별하는 값
        String clientId = "my-client-id";
        // MqttPahoMessageHandler: MQTT broker와 통신하는데 핵심 클래스
        // MqttPahoClientFactory를 사용하여 client를 생성하고, MQTT broker에 연결
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, mqttPahoClientFactory());
        // MqttPahoMessageHandler가 비동기로 동작하도록 설정
        // -> 메세지가 보내는 작업을 백그라운드에서 처리, 빠르고 효율적, but, 에러처리 복잡
        handler.setAsync(true);
        return new MqttPublisher(handler);
    }
}
