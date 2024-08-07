package com.example.tracker;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class MpesaService {
    private static final String CONSUMER_KEY = "your_consumer_key";  // Replace with your consumer key
    private static final String CONSUMER_SECRET = "your_consumer_secret";  // Replace with your consumer secret
    private static final String BASE_URL = "https://sandbox.safaricom.co.ke/";

    public static String getAccessToken() throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String auth = okhttp3.Credentials.basic(CONSUMER_KEY, CONSUMER_SECRET);

        Request request = new Request.Builder()
                .url(BASE_URL + "oauth/v1/generate?grant_type=client_credentials")
                .get()
                .addHeader("Authorization", auth)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            return jsonObject.getString("access_token");
        } else {
            throw new Exception("Failed to get access token. Response code: " + response.code());
        }
    }

    public static String getAccountBalance(String accessToken) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("Initiator", "testapi");
        jsonBody.put("SecurityCredential", "your_security_credential");  // Replace with your security credential
        jsonBody.put("CommandID", "AccountBalance");
        jsonBody.put("PartyA", "600584");
        jsonBody.put("IdentifierType", "4");
        jsonBody.put("Remarks", "Remarks");
        jsonBody.put("QueueTimeOutURL", "https://yourdomain.com/AccountBalance/queue/");
        jsonBody.put("ResultURL", "https://yourdomain.com/AccountBalance/result/");

        RequestBody body = RequestBody.create(mediaType, jsonBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "mpesa/accountbalance/v1/query")
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            String responseBody = response.body().string();
            // Handle and parse the response as needed
            return responseBody;  // Adjust based on actual response format
        } else {
            throw new Exception("Failed to get account balance. Response code: " + response.code());
        }
    }
}
