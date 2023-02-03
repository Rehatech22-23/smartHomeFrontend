package de.rehatech2223.lgg_frontend.services;

import java.io.IOException;
import java.util.ArrayList;
import datamodel.device;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DevicesService {

    private OkHttpClient httpClient;
    private String baseUrl = "localhost/";

    public DevicesService() {
        httpClient = new OkHttpClient();
    }

    public ArrayList<device> getDevicesList() {
        String res = "";
        try {
            res = run(baseUrl + "devices");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(res.isEmpty()) return new ArrayList<>();
        return createArrayListFromJSON(res);
    }

    private ArrayList<device> createArrayListFromJSON(String json) {
        //TODO
        return new ArrayList<>();
    }

    String run(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

}
