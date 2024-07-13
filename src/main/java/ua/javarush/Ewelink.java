package ua.javarush;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.Timer;
import java.util.TimerTask;

import static io.restassured.RestAssured.given;

public class Ewelink {
    private static final String API_URL = "https://eu-apia.coolkit.cc/v2/device/thing";
    private static String AUTHORIZATION_HEADER = "Bearer 8b45ba3be40ee8e0cd3105794448ca1227c913e5";
    private static final String CONTENT_TYPE_HEADER = "application/json";
    private static final String ORIGIN_HEADER = "origin: https://web.ewelink.cc";
    private static final String REQUEST_BODY = "{\"thingList\":[{\"id\":\"100063cabd\"}]}";
    private static boolean online = false;

    public static void main(String[] args) throws InterruptedException {
        login();
        Status();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
               // String authToken = login();
                System.out.println("Received auth token: " );
            }
        };
        long delay = 0;
        long period = 23 * 60 * 60 * 1000; // 23 часа в миллисекундах
        timer.scheduleAtFixedRate(task, delay, period);
        timer.cancel();
        task.cancel();

    }

    public static String login (){
        Response response =   given()
                .baseUri("https://eu-apia.coolkit.cc/v2/user/login")
                .header("Authorization", "Sign ccuYCNZ+FXsBZMNTlNb2GxEP1f/4nfZg/ecXCosjZLg=")
                .header("Content-Type", CONTENT_TYPE_HEADER)
                .headers("x-ck-appid","K0OCDSvIaBWdEaU4zxlKEwk26kmshoXK")
                .header("Origin", ORIGIN_HEADER)
                .body("{\"countryCode\":\"+380\",\"password\":\"12345678\",\"lang\":\"en\",\"email\":\"tryamkin@gmail.com\"}")
                .when()
                .post()
                .then()
                .log().body()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        System.out.println(jsonPath.get("data.at").toString());
        AUTHORIZATION_HEADER = "Bearer " + jsonPath.get("data.at").toString();
        return AUTHORIZATION_HEADER;
    }




    public static boolean Status (){
        Response response =   given()
                .baseUri(API_URL)
                .header("Authorization", AUTHORIZATION_HEADER)
                .header("Content-Type", CONTENT_TYPE_HEADER)
                .header("Origin", ORIGIN_HEADER)
                .body(REQUEST_BODY)
                .when()
                .post()
                .then()
                // .statusCode(200) // Assert successful response (optional)
                // .log().body()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        online = jsonPath.get("data.thingList[0].itemData.online");
        System.out.println(online);
        System.out.println("Online - " +jsonPath.get("data.thingList[0].itemData.online").toString());
        System.out.println("switch - " +jsonPath.get("data.thingList[0].itemData.params.switch").toString());
        //  System.out.println("pulse - " +jsonPath.get("data.thingList[0].itemData.params.pulse").toString());
    return online;
    }
 }

