package com.shiftvisionapi.test;
import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.path.xml.XmlPath.from;


public class HeatClinicTest {

    @BeforeMethod
    public void beforeMethod() {
        RestAssured.baseURI = "http://heatclinic.shiftedtech.com";
        RestAssured.port = 80;
        RestAssured.basePath = "/api/v1";
    }

    @Test
    public void test_01() {
        given()
                .log().all()
                .param("q", "hot")
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .when()
                .get("/catalog/search")
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test
    public void test_02() {
        given()
                .log().all()
                .param("q", "hot")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/catalog/search")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void test_03() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .param("q", "hot")
        .when()
                .get("/catalog/search")
        .then()
                .log().body()
                .statusCode(200);

    }
    @Test
    public void test_04() {
        given()
                .header(new Header("Accept", "application/XML"))
                .header(new Header("Content-Type", "application/XML"))
                .param("q", "hot")
        .when()
                .get("/catalog/search")
        .then()
                .log().body()
                .statusCode(200)
                .body(hasXPath("boolean(/searchResults)",containsString("true")))
                .body(hasXPath("boolean(/searchResults/totalResults)",containsString("true")))
                .body(hasXPath("boolean(/searchResults/products/product/name)",containsString("true")));


    }

    @Test
    public void test_05() {
        given()
                .header(new Header("Accept", "application/XML"))
                .header(new Header("Content-Type", "application/XML"))
                .param("q", "hot")
        .when()
                .get("/catalog/search")
        .then()
                .log().body()
                .statusCode(200)
                .body(hasXPath("/searchResults/page",containsString("1")))
                .body(hasXPath("/searchResults/pageSize",containsString("15")))
                .body(hasXPath("/searchResults/totalResults",containsString("14")))
                .body(hasXPath("/searchResults/totalPages",containsString("1")))
                .body(hasXPath("/searchResults/products/product/name",containsString("Bull Snort Smokin' Toncils Hot Sauce")));
    }


    @Test
    public void test_06() {
        given()
                .header(new Header("Accept", "application/XML"))
                .header(new Header("Content-Type", "application/XML"))
                .param("q", "hot")
        .when()
                .get("/catalog/search")
        .then()
                .log().body()
                .statusCode(200)
                .body(hasXPath("/searchResults//product[1]/id",containsString("13")))
                .body(hasXPath("/searchResults//product[1]/name",containsString("Bull Snort Smokin' Toncils Hot Sauce")));
    }

    @Test
    public void searchTestProducts_01(){

        RequestSpecification req = given()
                .log().body()
                .param("q","hot")
                .header(new Header("Accept","application/XML"))
                .header(new Header("Content-Type","application/XML"))
                ;

        Response res=req.when().get("/catalog/search");

        String body =res.asString();
        System.out.println(body);

        List<String> products= from(body).getList("searchResults.products.product.name");
        System.out.println("Total number of products are : " + products.size());
        assertThat(products.size(),equalTo(14));

        String[] expectedProduct= {
                   "Bull Snort Smokin' Toncils Hot Sauce",
                   "Hoppin' Hot Sauce",
                   "Roasted Garlic Hot Sauce",
                   "Scotch Bonnet Hot Sauce",
                   "Hurtin' Jalepeno Hot Sauce",
                   "Blazin' Saddle XXX Hot Habanero Pepper Sauce",
                   "Dr. Chilemeister's Insane Hot Sauce",
                   "Cool Cayenne Pepper Hot Sauce",
                   "Day of the Dead Habanero Hot Sauce",
                   "Day of the Dead Chipotle Hot Sauce",
                   "Armageddon The Hot Sauce To End All",
                   "Bull Snort Cowboy Cayenne Pepper Hot Sauce",
                   "Day of the Dead Scotch Bonnet Hot Sauce",
                   "Roasted Red Pepper & Chipotle Hot Sauce"
        };

        String[] actualProducts = new String[products.size()];
        actualProducts = products.toArray(actualProducts);
        System.out.println("Actual Products are:  "+ Arrays.toString(actualProducts));

        assertThat(expectedProduct,arrayContainingInAnyOrder(actualProducts));



    }


    @AfterMethod
    public void afterMethod() {
    }
}