import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
public class TestApi {
	 public static void  main(String[] args)throws IOException, JSONException {
	       String clientid = "06e1316b-fa4e-4fb7-9d9c-44188155c4b8";
	       String clientsecret = "L5_Q~_.61.uic_Kpqtze4pwg-Ohk~.we6l";
	       String baseUrl = "https://login.microsoft.com/common/oauth2/v2.0/";
	       String authorizationCode = "authorization_code";
	       String redirectUri = "http://localhost:8080/podium/login";
	       String scope = "onedrive.readwrite";
	       String readLine = null;
	       String accesstoken = null ;
	       int responseCode = 0;
	       
	       File file = new File("D:\\123.txt");
	       //get code for access token
	       String code = getCode(baseUrl, clientid, redirectUri, scope);
	       //get access token
	       accesstoken =  getAccessToken(baseUrl,clientid,redirectUri,authorizationCode, code,clientsecret);
	       //upload api
	       URL urlforfileupload = new URL("https://api.onedrive.com/v1.0/drives/E215DB2592756A39/items/E215DB2592756A39%21623:/myfile.txt:/content");
	       HttpURLConnection conection2 = (HttpURLConnection) urlforfileupload.openConnection();
	       System.out.println(conection2);
	       conection2.setRequestMethod("PUT");
	       conection2.setRequestProperty("Authorization", "Bearer " + accesstoken);
	       conection2.setDoOutput(true);
	       //send the above info in the body
	       byte[] outputInBytes = readFileToByteArray(file);
	       OutputStream os = conection2.getOutputStream();
	       os.write( outputInBytes );    
	       os.close();
	       responseCode = conection2.getResponseCode();
	       if (responseCode == HttpURLConnection.HTTP_OK) {
	           BufferedReader in = new BufferedReader(
	               new InputStreamReader(conection2.getInputStream()));
	           StringBuffer response = new StringBuffer();
	           while ((readLine = in .readLine()) != null) {
	               response.append(readLine);
	           } in .close();
	           // print result
	           System.out.println("JSON String Result " + response);
	       }else {
	    	   System.out.println("Bad Request");
	       }
	     
	 
	 }
	 
	private static byte[] readFileToByteArray(File file){
		    FileInputStream fis = null;
		    // Creating a byte array using the length of the file
		    // file.length returns long which is cast to int
		    byte[] bArray = new byte[(int) file.length()];
		    try{
		      fis = new FileInputStream(file);
		      fis.read(bArray);
		      fis.close();                   
		    }catch(IOException ioExp){
		      ioExp.printStackTrace();
		    }
		    return bArray;
		  } 
	private static String getCode(String baseUrl, String clientid, String redirectUri, String scope) throws IOException, JSONException {
	       URL urlForCode = new URL(baseUrl+"authorize?"+"client_id="+clientid+"&scope="+scope+"&response_type=code"+"&redirect_uri="+redirectUri);
	       HttpURLConnection codeConection = (HttpURLConnection) urlForCode.openConnection();
	       codeConection.setRequestMethod("GET");
	       String readLine = null;
	       int responseCode = 0;
	       String code;
	       responseCode = codeConection.getResponseCode();
	       if (responseCode == HttpURLConnection.HTTP_OK) {
	           BufferedReader in = new BufferedReader(
	               new InputStreamReader(codeConection.getInputStream()));
	           StringBuffer response = new StringBuffer();
	           while ((readLine = in .readLine()) != null) {
	        	   response.append(readLine);
	           } in .close();
	           try {
	           JSONObject obj = new JSONObject(response.toString());
	           code =(String) obj.get("code");
	           }
	           catch(Exception e) {
	        	   System.out.println(e.getMessage());
	           }
	           
	           // print result
	           System.out.println("JSON String Result " + response);  
	       } else {
	           System.out.println("Bad Request");
	       }
	       
	       
	        code ="Mef31cf54-266a-6de0-a8bb-b7e3e51c5082";     
		return code;
	}
	
	private static String getAccessToken(String baseUrl, String clientid, String redirectUri, String authorizationCode, String code, String clientsecret) throws IOException, JSONException{
		URL urlForAccessToken = new URL(baseUrl+"token");
	       String readLine = null;
	       int responseCode = 0;
	       String accesstoken = null;
	       HttpURLConnection conection = (HttpURLConnection) urlForAccessToken.openConnection();
	       System.out.println(conection);
	       conection.setRequestMethod("POST");
	       conection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	       conection.setDoOutput(true);
	       String str =  "client_id="+clientid+"&redirect_uri="+redirectUri+"&client_secret="+clientsecret+"&code="+code+"&grant_type="+authorizationCode;

	       //send the above info in the body
	       byte[] outputInBytes = str.getBytes("UTF-8");
	       OutputStream os = conection.getOutputStream();
	       os.write( outputInBytes );    
	       os.close();
	       responseCode = conection.getResponseCode();
	       if (responseCode == HttpURLConnection.HTTP_OK) {
	           BufferedReader in = new BufferedReader(
	               new InputStreamReader(conection.getInputStream()));
	           StringBuffer response = new StringBuffer();
	           while ((readLine = in .readLine()) != null) {
	        	   response.append(readLine);
	           } in .close();
	           // print result
	           JSONObject obj = new JSONObject(response.toString());
		       System.out.println(obj);
		       accesstoken =(String) obj.get("access_token");
		       System.out.println(accesstoken);
	       } else {
	           System.out.println("Bad Request");
	       }
	              


		return accesstoken;
	}
	 
}
