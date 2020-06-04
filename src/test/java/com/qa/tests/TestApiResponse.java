/*
 * Author  Anand S. Mane
 * Email anand31187@gmail.com */


package com.qa.tests;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.data.ArrayResponse;
import com.qa.utils.FileRead;
import com.qa.utils.FileRead.ApiUrl;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestApiResponse {

	TestBase testbase = new TestBase();
	RequestSpecification httpRequest;
	Response response1;
	Response response2;
	String file1Path = testbase.prop.getProperty("File1");
	String file2Path = testbase.prop.getProperty("File2");

	@Test
	public void testApiResponse() throws IOException {

		FileRead fileObj = new FileRead();
		List<ApiUrl> apis = fileObj.fileRead(file1Path, file2Path);

		for (ApiUrl urls : apis) {
			if (urls.getUrl1().contains("http") && urls.getUrl2().contains("http")) {
				try {
					RestAssured.baseURI = urls.getUrl1();
					RestAssured.baseURI = urls.getUrl2();
					httpRequest = RestAssured.given();
					httpRequest = RestAssured.given();
					response1 = httpRequest.request(Method.GET, urls.getUrl1());
					response2 = httpRequest.request(Method.GET, urls.getUrl2());
				} catch (Exception e) {
					System.out.println(urls.getUrl1() + " not equals  " + urls.getUrl2());
					return;
				}
				try {
					compareTo(urls.getUrl1(), response1, urls.getUrl2(), response2);
				} catch (ParseException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println(urls.getUrl1() + " not equals " + urls.getUrl2());
			}
		}

	}

	public void compareTo(String url1, Response response1, String url2, Response response2)
			throws JsonParseException, JsonMappingException, IOException, ParseException {
		String responseBody = response1.getBody().asString();
		String responseBody2 = response2.getBody().asString();
		if (isObjectEqual(responseBody, responseBody2)) {
			System.out.println(url1 + " equals " + url2);
		} else
			System.out.println(url1 + " not equals  " + url2);
	}

	public boolean isObjectEqual(String responseBody, String responseBody2)
			throws JsonParseException, JsonMappingException, IOException, ParseException {

		ObjectMapper mapper = new ObjectMapper();

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj1;
		JSONObject jsonObj2;

		Object data1;
		Object data2;

		jsonObj1 = (JSONObject) jsonParser.parse(responseBody);
		jsonObj2 = (JSONObject) jsonParser.parse(responseBody2);

		com.qa.data.Response responseObj1 = null;
		com.qa.data.Response responseObj2 = null;
		ArrayResponse obj1 = null;
		ArrayResponse obj2 = null;

		data1 = jsonObj1.get("data");
		data2 = jsonObj2.get("data");

		if (data1 instanceof JSONArray && data2 instanceof JSONArray) {
			obj1 = mapper.readValue(responseBody, ArrayResponse.class);
			obj2 = mapper.readValue(responseBody2, ArrayResponse.class);

		} else if (data1 instanceof JSONObject && data2 instanceof JSONObject) {
			responseObj1 = mapper.readValue(responseBody, com.qa.data.Response.class);
			responseObj2 = mapper.readValue(responseBody2, com.qa.data.Response.class);
		} else {
			return Boolean.FALSE;
		}

		if (responseObj1 != null) {
			if (responseObj1.getData().equals(responseObj2.getData())) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} else {
			if (obj1.getData().equals(obj2.getData())) {

				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}

	}

}
