package org.eclipsefoundation.projectsbots;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BotsTest {

	@Test
	public void testGetQuery() {
		given()
			.when().get("bots/?q=che-bot")
			.then()
				.statusCode(200)
				.body(is("[{\"id\":1,\"projectId\":\"ecd.che\",\"username\":\"genie.che\",\"email\":\"che-bot@eclipse.org\",\"github.com\":{\"username\":\"che-bot\",\"email\":\"che-bot@eclipse.org\"}}]"));
	}

	@Test
	public void testGetById() {
		given()
			.when().get("bots/2")
			.then()
				.statusCode(200)
				.body(is("{\"id\":2,\"projectId\":\"ecd.che.che4z\",\"username\":\"genie.che4z\",\"email\":\"che4z-bot@eclipse.org\",\"github.com\":{\"username\":\"eclipse-che4z-bot\",\"email\":\"che4z-bot@eclipse.org\"}}"));
	}

}