package harang.harangClient.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Scheduled 어노테이션은 메서드 레벨에서만 사용
//@Scheduled 어노테이션 실행이 안됨 왜?
// => @EnableScheduling 어노테이션을 사용하여 스케줄링을 활성화해야함

@EnableScheduling
@Service
public class InfluxDBService {

    @Autowired
    private InfluxDBClient influxDBClient;


    // measurement 필터링 조건 생성
    private String getMeasurementFilterClause(String[] measurements) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < measurements.length; i++) {
            if (i != 0) {
                sb.append(" or ");
            }
            sb.append("r._measurement ==").append("\"").append(measurements[i]).append("\"");
        }
//        System.out.println(sb.toString());
        return sb.toString();
    }

    public List<Map<String, Object>> queryData() {

        String influxOrg = "belab";
        String influxBucket = "belab";


        // 쿼리 실행
        // \"%s\" InflexDB 쿼리 "" 큰따옴표 처리 중요
        String[] measurements = {"Thermostat_Amb_Humidity", "Thermostat_Amb_Temperature", "Irradi1", "Irradi2"};
        String query = String.format("from(bucket:\"%s\") |> range(start: -1m) |> filter(fn: (r) => %s)", influxBucket, getMeasurementFilterClause(measurements));
//        System.out.println(query);
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> queryResult = queryApi.query(query, influxOrg);


        List<Map<String, Object>> resultList = new ArrayList<>();


        for (FluxTable table : queryResult) {
            for (FluxRecord record : table.getRecords()) {
                String measurement = record.getMeasurement();
//                String time = record.getTime().toString();
                String value = record.getValue().toString();

                Map<String, Object> result = new HashMap<>();
                result.put("measurement", measurement);
//                result.put("time", time);
                result.put("value", value);

                resultList.add(result);
                System.out.println(String.format("measurement=%s, value=%s", measurement, value));
            }

        }

        return resultList;
    }
}


