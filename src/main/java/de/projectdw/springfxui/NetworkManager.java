package de.projectdw.springfxui;

import com.google.gson.Gson;
import de.projectdw.springfxui.data.InputLogData;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NetworkManager {
    private static String url = "https://ampel.projectdw.de";
    private List<InputLogData> failedConnections = new ArrayList<>();

    public NetworkManager() {
        this.loadFromFile();

    }

    public boolean tryToConnect(InputLogData logData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.ALL));
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/ping");

            HttpEntity<?> request = new HttpEntity<>(logData, headers);

            HttpEntity<String> response = restTemplate.exchange(
                    builder.build().toUriString(),
                    HttpMethod.POST,
                    request,
                    String.class
            );
            this.writeToFile();
            return true;
        } catch (Exception ex) {
            failedConnections.add(logData);
            this.writeToFile();
            return false;
        }
    }

    public boolean uploadLogs() {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.ALL));
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/uploadLogs");


            HttpEntity<?> request = new HttpEntity<>(failedConnections, headers);

            HttpEntity<String> response = restTemplate.exchange(
                    builder.build().toUriString(),
                    HttpMethod.POST,
                    request,
                    String.class
            );
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void writeToFile() {
        Gson gson = new Gson();
        try(FileWriter fileWriter = new FileWriter("logs.json")) {
            gson.toJson(failedConnections.toArray(), fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        Gson gson = new Gson();
        try {
            InputLogData[] data = gson.fromJson(new FileReader("logs.json"), InputLogData[].class);
            if (data != null && data.length > 0)
                failedConnections = new ArrayList<>(List.of(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
