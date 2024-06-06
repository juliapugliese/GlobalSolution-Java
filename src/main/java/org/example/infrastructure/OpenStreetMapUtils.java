package org.example.infrastructure;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class OpenStreetMapUtils {

    public final static Logger log = Logger.getLogger("OpenStreeMapUtils");

    private static OpenStreetMapUtils instance = null;
    private JSONParser jsonParser;

    public OpenStreetMapUtils() {
        jsonParser = new JSONParser();
    }

    public static OpenStreetMapUtils getInstance() {
        if (instance == null) {
            instance = new OpenStreetMapUtils();
        }
        return instance;
    }

    private String getRequest(String url) throws Exception {

        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        if (con.getResponseCode()!= 200) {
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine())!= null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public String getEstado(double lat, double lon) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + lat + "," + lon + "&format=json&addressdetails=1";

        try {
            String response = getRequest(url);
            Object obj = JSONValue.parse(response);

            log.debug("obj=" + obj);

            if (obj instanceof JSONArray) {
                JSONArray array = (JSONArray) obj;
                if (array.size() > 0) {
                    JSONObject jsonObject = (JSONObject) array.get(0);
                    JSONObject address = (JSONObject) jsonObject.get("address");

                    String state = (String) address.get("state");
                    log.debug("Estado=" + state);
                    return state;
                }
            }

        } catch (Exception e) {
            log.error("Error getting state", e);
        }

        return null;
    }
    public String getCidade(double lat, double lon) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + lat + "," + lon + "&format=json&addressdetails=1";

        try {
            String response = getRequest(url);
            Object obj = JSONValue.parse(response);

            log.debug("obj=" + obj);

            if (obj instanceof JSONArray) {
                JSONArray array = (JSONArray) obj;
                if (array.size() > 0) {
                    JSONObject jsonObject = (JSONObject) array.get(0);
                    JSONObject address = (JSONObject) jsonObject.get("address");

                    String city = (String) address.get("city");
                    log.debug("Cidade=" + city);
                    return city;
                }
            }

        } catch (Exception e) {
            log.error("Error getting city", e);
        }

        return null;
    }
    public String getBairro(double lat, double lon) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + lat + "," + lon + "&format=json&addressdetails=1";

        try {
            String response = getRequest(url);
            Object obj = JSONValue.parse(response);

            log.debug("obj=" + obj);

            if (obj instanceof JSONArray) {
                JSONArray array = (JSONArray) obj;
                if (array.size() > 0) {
                    JSONObject jsonObject = (JSONObject) array.get(0);
                    JSONObject address = (JSONObject) jsonObject.get("address");

                    String bairro = (String) address.get("suburb");
                    log.debug("bairro=" + bairro);
                    return bairro;
                }
            }

        } catch (Exception e) {
            log.error("Error getting neighbourhood", e);
        }

        return null;
    }
    public String getCep(double lat, double lon) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + lat + "," + lon + "&format=json&addressdetails=1";

        try {
            String response = getRequest(url);
            Object obj = JSONValue.parse(response);

            log.debug("obj=" + obj);

            if (obj instanceof JSONArray) {
                JSONArray array = (JSONArray) obj;
                if (array.size() > 0) {
                    JSONObject jsonObject = (JSONObject) array.get(0);
                    JSONObject address = (JSONObject) jsonObject.get("address");

                    String postcode = (String) address.get("postcode");
                    log.debug("CEP=" + postcode);
                    return postcode;
                }
            }

        } catch (Exception e) {
            log.error("Error getting postcode", e);
        }

        return null;
    }
    public String getEndereco(double lat, double lon) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + lat + "," + lon + "&format=json&addressdetails=1";

        try {
            String response = getRequest(url);
            Object obj = JSONValue.parse(response);

            log.debug("obj=" + obj);

            if (obj instanceof JSONArray) {
                JSONArray array = (JSONArray) obj;
                if (array.size() > 0) {
                    JSONObject jsonObject = (JSONObject) array.get(0);

                    String address =  lat + ", " + lon + ", " + jsonObject.get("display_name");
                    log.debug("address=" + address);
                    return address;
                }
            }

        } catch (Exception e) {
            log.error("Error getting address", e);
        }

        return null;
    }

}