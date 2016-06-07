package db;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.P2PFile;
import domain.RemoveFilePayload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DBWrapper {
	
	private static final String dbHost = "http://52.32.187.22:3005/";

	public P2PFile[] listFiles() {
		String json = getRequest(dbHost + "listFiles");

		//System.out.println(json);

		ObjectMapper mapper = new ObjectMapper();
		P2PFile[] fileList = null;

		try {

			// Convert JSON string from file to Object
			fileList = mapper.readValue(json, P2PFile[].class);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileList;
	}
	
	public void addFile(P2PFile file) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			String payload = mapper.writeValueAsString(file);
			
			System.out.println("Payload: \n" + payload);
			
			postRequest(dbHost + "addFile", payload);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeFile(String filename, String host) {

		ObjectMapper mapper = new ObjectMapper();

		RemoveFilePayload payloadObject = new RemoveFilePayload(filename, host);

		try {

			String payload = mapper.writeValueAsString(payloadObject);
			
			System.out.println("Payload: \n" + payload);
			
			postRequest(dbHost + "removeFile", payload);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public P2PFile getFile(String filename) {
		String json = getRequest(dbHost + "getFile/" + filename);

		//System.out.println(json);

		ObjectMapper mapper = new ObjectMapper();
		P2PFile file = null;

		try {

			// Convert JSON string from file to Object
			file = mapper.readValue(json, P2PFile.class);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return file;
	}
	
	public void removeAllFiles() {
		getRequest(dbHost + "removeAll");
	}

	private static String getRequest(String path) {
		String output = "";

		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			output = br.readLine();
			// System.out.println("Output from Server .... \n");
			// System.out.println(output);
			/*
			 * while ((output = br.readLine()) != null) {
			 * System.out.println(output); }
			 */

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return output;
	}

	private static String postRequest(String path, String payload) {
		String output = "";
		try {

			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(payload.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			// System.out.println("Output from Server .... \n");
			// while ((output = br.readLine()) != null) {
			// System.out.println(output);
			// }

			output = br.readLine();

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return output;

	}

}
