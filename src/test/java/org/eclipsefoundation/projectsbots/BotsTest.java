package org.eclipsefoundation.projectsbots;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BotsTest {

    @Test
    public void testGetByUsername() {
        given()
          .when().get("bots/?username=che-bot")
          .then()
             .statusCode(200)
             .body(is("[{\"kind\":\"github\",\"id\":1,\"email\":\"che-bot@eclipse.org\",\"projectId\":\"ecd.che\",\"username\":\"che-bot\"}]"));
    }
    
    @Test
    public void testGetById() {
    	
    	given()
      .when().get("bots/2")
      .then()
         .statusCode(200)
         .body(is("{\"kind\":\"github\",\"id\":2,\"email\":\"sprotty-bot@eclipse.org\",\"projectId\":\"ecd.sprotty\",\"username\":\"eclipse-sprotty-bot\"}"));
    }

}