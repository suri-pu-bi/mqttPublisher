package harang.harangClient;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class InfluxDBConfig {

    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.token}")
    private String token;

    @Value("${influxdb.org}")
    private String org;

    @Value("${influxdb.bucket}")
    private String bucket;

    // InfluxDBClientFactory.create() 메서드를 호출하여 InfluxDBClient 객체 생성
    // 이 메서드의 인자로, InfluxDB 연결 정보를 나타내는 url, token, org, bucket 값이 전달
    // InfluxDBClinet 객체생성하면 influxDB에 데이터를 쓰거나, 쿼리를 통해 데이터조회가 가능
    // @Bean: 해당 메소드가 반환하는 객체를 스프링의 빈(Bean)으로 등록
    // 이렇게 등록된 빈은 스프링 애플리케이션에서 DI(Dependency Injection)를 받을 수 있고, 스프링 컨테이너에서 관리됨
    // 왜 influxDBClient을 @Bean으로 config에서 생성해야하는가?
    // InfluxDB를 사용하는 애플리케이션에서는 InfluxDBClient 객체를 여러 곳에서 공유해서 사용O
    // 이때, InfluxDBClient를 @Bean으로 등록하여 필요한 클래스에서 DI를 받아 사용하면,
    // 객체 생성과 초기화 등의 작업을 중복해서 수행하지 않아도 되므로 코드의 재사용성과 유지보수성이 향상

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }

//  Consider defining a bean , Parameter 0 of constructor in ~ Error 발생!
//  Config의 위치 중요 => package 안에 있어야함 !
}
