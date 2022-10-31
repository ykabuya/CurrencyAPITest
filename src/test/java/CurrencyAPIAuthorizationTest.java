import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

    public class CurrencyAPIAuthorizationTest {

        public static Response response;

        @Test
        public void validTokenTest() {
            response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.LIVE + Consts.TOKEN);
            System.out.println(response.asString());
            response.then().statusCode(200);
            response.then().body("success", equalTo(true));
            response.then().body("source", equalTo("USD"));
        }
        @Test
        public void invalidTokenTest() {
            response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.LIVE + Consts.INVALID_TOKEN);
            System.out.println(response.asString());
            response.then().statusCode(401);
            response.then().body("message", containsString("Invalid authentication credentials"));
        }

        @Test
        public void missingTokenTest() {
            response = given().contentType("application/json").get(Consts.CURRENCY_API_URL + Consts.LIVE + null);
            System.out.println(response.asString());
            response.then().statusCode(401);
            response.then().body("message", containsString("No API key found in request"));
        }



    }


