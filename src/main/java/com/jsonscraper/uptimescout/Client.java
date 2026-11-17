package com.jsonscraper.uptimescout;
import java.net.*;import java.net.http.*;import java.time.Duration;import java.util.*;import java.util.stream.Collectors;import java.nio.charset.StandardCharsets;
public final class Client {
 private final String key;private final HttpClient http;private final String base;
 public Client(String licenseKey){this(licenseKey,"https://api.uptimescout.com");}
 public Client(String licenseKey,String baseUrl){if(licenseKey==null||licenseKey.isBlank()||licenseKey.contains("\n")||licenseKey.contains("\r"))throw new IllegalArgumentException("License key required");key=licenseKey;base=baseUrl.replaceAll("/$","");http=HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).followRedirects(HttpClient.Redirect.NEVER).build();}
 public String request(String method,Map<String,String> parameters)throws Exception{if(!Set.of("services","status","statuses","summary","history","stats","incidents").contains(method))throw new IllegalArgumentException("Unknown method");String query=parameters.entrySet().stream().map(e->URLEncoder.encode(e.getKey(),StandardCharsets.UTF_8)+"="+URLEncoder.encode(e.getValue(),StandardCharsets.UTF_8)).collect(Collectors.joining("&"));var request=HttpRequest.newBuilder(URI.create(base+"/v1/"+method+"?"+query)).timeout(Duration.ofSeconds(30)).header("Authorization","Bearer "+key).header("Accept","application/json").GET().build();var response=http.send(request,HttpResponse.BodyHandlers.ofString());if(response.statusCode()!=200)throw new IllegalStateException("UptimeScout HTTP "+response.statusCode());return response.body();}
}
