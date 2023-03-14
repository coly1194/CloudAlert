package com.aiops.cloudalert.util;

import com.dtflys.forest.annotation.GetRequest;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.PostRequest;
import com.dtflys.forest.extensions.BasicAuth;

import java.util.Map;

public interface HttpClient {
//    url = "https://caweb.aiops.com/api/alert/rest/api/v1/alert/historymonth?appKey=97bb4c8347df4580974717a0e1c0f665&startTime=2023-2-1&endTime=2023-2-20"
    @PostRequest(url = "https://caweb.aiops.com/api/alert/rest/api/v1/alert?size=${2}&current=${3}")
    @BasicAuth(username = "${0}",password = "${1}")
    String getAlerts(String username,String password,int size,int current,@JSONBody Map<String,Object> map);

    @GetRequest(url = "https://caweb.aiops.com/api/alert/rest/api/v1/alert1/ack/${2}")
    @BasicAuth(username = "${0}",password = "${1}")
    String ack(String username,String password,String id);

    @GetRequest(url = "https://caweb.aiops.com/api/alert/rest/api/v1/alert1/resolve/${2}")
    @BasicAuth(username = "${0}",password = "${1}")
    String close(String username,String password,String id);
}
