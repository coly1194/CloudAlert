package com.aiops.cloudalert.util;

import com.dtflys.forest.annotation.GetRequest;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.PostRequest;
import com.dtflys.forest.extensions.BasicAuth;

import java.util.Map;

public interface HttpClient {
    static final String url = "https://caweb.aiops.com/api/alert/rest/api/v1";
//    static final String url = "http://newca.aiops.com/api/alert/rest/api/v1/";

    @PostRequest(url = url+"/alert?size=${2}&current=${3}")
    @BasicAuth(username = "${0}",password = "${1}")
    String getAlerts(String username,String password,int size,int current,@JSONBody Map<String,Object> map) throws Exception;

    @GetRequest(url = url+"/alert1/ack/${2}")
    @BasicAuth(username = "${0}",password = "${1}")
    String ack(String username,String password,String id);

    @GetRequest(url = url+"/alert1/resolve/${2}?comments=${3}&validStatus=&{4}")
    @BasicAuth(username = "${0}",password = "${1}")
    String close(String username,String password,String id,String comments,int validStatus);

    @GetRequest(url = url+"/apps")
    @BasicAuth(username = "${0}",password = "${1}")
    String apps(String username,String password);
}
