package harang.harangClient.controller;

import harang.harangClient.service.InfluxDBService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class DataController {

    @Autowired
    private InfluxDBService influxDBService;

    @GetMapping("/data")
//    @Scheduled 어노테이션이 붙어 있기 때문에, 해당 메서드는 1분마다 자동으로 실행
//    -> /data 호출할 때마다 최신 데이터가 반환!
    public List<Map<String, Object>> getData() {
        return influxDBService.queryData();
    }
}
