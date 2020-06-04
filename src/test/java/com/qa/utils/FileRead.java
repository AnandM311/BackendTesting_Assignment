/*
 * Author  Anand S. Mane
 * Email anand31187@gmail.com */

package com.qa.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.qa.base.TestBase;
import com.qa.data.Response;

import io.restassured.specification.RequestSpecification;

public class FileRead {
	
	RequestSpecification httpRequest;
	Response response1;
	Response response2;

	public List<ApiUrl> fileRead(String file1path, String file2path) throws IOException {
		File file = new File(file1path);
		File file2 = new File(file2path);
		BufferedReader br = null;
		BufferedReader br2 = null;
		List<ApiUrl> apis = new ArrayList<FileRead.ApiUrl>();
		try {
			java.io.FileReader fr = new java.io.FileReader(file);
			br = new BufferedReader(fr);
			java.io.FileReader fr2 = new java.io.FileReader(file2);
			br2 = new BufferedReader(fr2);
			ApiUrl apiUrl = new ApiUrl();

			String line;
			String line2;
			while ((line = br.readLine()) != null && (line2 = br2.readLine()) != null) {

				apiUrl = new ApiUrl();
				apiUrl.setUrl1(line);
				apiUrl.setUrl2(line2);
				apis.add(apiUrl);
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.toString());
		} finally {
			br.close();
			br2.close();
		}
		return apis;
	}

	public static class ApiUrl {
		private String url1;
		private String url2;

		public String getUrl1() {
			return url1;
		}

		public void setUrl1(String url1) {
			this.url1 = url1;
		}

		public String getUrl2() {
			return url2;
		}

		public void setUrl2(String url2) {
			this.url2 = url2;
		}

	}

}
