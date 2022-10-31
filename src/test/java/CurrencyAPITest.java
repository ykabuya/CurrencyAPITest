import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;




public class CurrencyAPITest {

    public static Response response;


    @Test
    public void listOfALLCurrenciesTest() {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.TOKEN  + "&" + Consts.LIST);
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        System.out.println(response.asString());
    }

    @Test
    public void wrongCurrencyTest() {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.LIVE  + Consts.TOKEN + "&source=" + Consts.USD + Consts.CURRENCIES + "=DAC");
        response.then().body("error.code", equalTo(202));
        System.out.println(response.asString());

    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "100", "67809987"})
    public void USDToCADConversionTest(String amount) {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.CONVERT + Consts.CAD + "&from=" + Consts.USD + "&amount=" + amount + "&" + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("query.from", equalTo("USD"));
        response.then().body("query.to", equalTo("CAD"));
        response.then().body("query.amount", notNullValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "100", "67809987"})
    public void USDToEURConversionTest(String amount) {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.CONVERT + Consts.EUR + "&from=" + Consts.USD + "&amount=" + amount + "&" + Consts.TOKEN);
        System.out.println(response.asString());
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("query.to", equalTo("EUR"));
        response.then().body("query.from", equalTo("USD"));
        response.then().body("query.amount", notNullValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "100", "67809987"})
    public void USDToRUBConversionTest(String amount) {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.CONVERT + Consts.RUB + "&from=" + Consts.USD + "&amount=" + amount + "&" + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("query.from", equalTo("USD"));
        response.then().body("query.to", equalTo("RUB"));
        response.then().body("query.amount", notNullValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "100", "67809987"})
    public void USDToILSConversionTest(String amount) {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.CONVERT + Consts.ILS + "&from=" + Consts.USD + "&amount=" + amount + "&" + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("query.from", equalTo("USD"));
        response.then().body("query.to", equalTo("ILS"));
        response.then().body("query.amount", notNullValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", ".", "", " "})
    public void wrongCurrencyAmountTest(String amount) {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.CONVERT  + Consts.CAD + "&from=" + Consts.USD + "&amount=" + amount + "&" + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(xxx));
        response.then().body("error.info", containsString("xxx"));

    }

    @ParameterizedTest
    @ValueSource(strings = {"1991-01-21", "2001-03-07", "2021-02-21"})
    public void historicalConversionTest(String date) {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.HISTORICAL  + date + "&" + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("historical", equalTo(true));
    }
    @ParameterizedTest
    @ValueSource(strings = {"1891-01-21", "2031-03-07", "2021-02-30"})
    public void historicalConversionWrongDateTest(String date) {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.HISTORICAL  + date + "&" + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("error.code", equalTo(xxx));
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString("xxx"));
    }

    @Test
    public void wrongDateFormatTest() {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.HISTORICAL + "23-07-08" + "&" + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(xxx));
        response.then().body("error.info", containsString("xxx"));

    }


    @ParameterizedTest
    @ValueSource(strings = {"CAD", "EUR", "RUB", "ILS"})
    public void timeFrameConversionTest(String currencies) {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.TIMEFRAME + Consts.STARTDATE + "2021-01-01" + "&" + Consts.ENDDATE + "2021-08-01" + "&" + Consts.TOKEN + Consts.CURRENCIES + currencies);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("start_date", equalTo("2021-01-01"));
        response.then().body("end_date", equalTo("2021-08-01"));
        response.then().body("source", equalTo("USD"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"CAD", "EUR", "RUB", "ILS"})
    public void longTimeFrameTest(String currencies) {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.TIMEFRAME + Consts.STARTDATE + "1990-01-01" + "&" + Consts.ENDDATE + "2022-01-01" + "&" + Consts.TOKEN + Consts.CURRENCIES + currencies);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(xxx));
        response.then().body("error.info", containsString("xxx"));
    }

    @Test
    public void wrongStartDataTimeframeTest() {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.TIMEFRAME + Consts.STARTDATE + "01282019" + "&" + Consts.ENDDATE + "2022-07-07" + "&" + Consts.TOKEN + Consts.CURRENCIES + "CAD,EUR,RUB,ILS");
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(xxx));
        response.then().body("error.info", containsString("xxx"));
    }

    @Test
    public void wrongEndDataTimeframeTest() {
        response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.TIMEFRAME + "?" + Consts.STARTDATE + "2021-07-09" + "&" + Consts.ENDDATE + "2022" + "&" + Consts.TOKEN + Consts.CURRENCIES + "CAD,EUR,RUB,ILS");
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(xxx));
        response.then().body("error.info", containsString("xxx"));
    }

}