package io.sendman.sendman;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import io.sendman.sendman.models.SendManCategories;
import io.sendman.sendman.models.SendManCategory;
import io.sendman.sendman.models.SendManData;
import io.sendman.sendman.models.SendManSDKEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendManAPIHandler {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static void sendData(final SendManData data, final APICallback callback) {
        OkHttpClient client = SendManAPIHandler.getSendManClient();
        String json = new Gson().toJson(data);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(SendManAPIHandler.getUrlFromPath("user/data"))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onDataSendError();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) {
                if (!response.isSuccessful()) {
                    callback.onDataSendError();
                } else {
                    System.out.println("Successfully set properties:" + new Gson().toJson(data));
                }
            }
        });
    }

    public static void getCategories(final APICallback callback) {
        SendManConfig config = SendMan.getConfig();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(config.getAppKey(), config.getAppSecret()))
                .build();
        Request request = new Request.Builder()
                .url(SendManAPIHandler.getUrlFromPath("categories/user/" + SendMan.getUserId()))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (callback != null) {
                    System.out.println("Failed to get categories");
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) {
                if (!response.isSuccessful()) {
                    if (callback != null) {
                        System.out.println("Failed to get categories");
                    }
                } else {
                    try {
                        JSONObject jsonResponse = new JSONObject(Objects.requireNonNull(response.body()).string());
                        List<SendManCategory> categories = SendManCategories.fromJson(jsonResponse.getJSONArray("categories"));
                        SendMan.setUserCategories(categories);
                        if (callback != null) {
                            callback.onCategoriesRetrieved();
                        }
                    } catch (JSONException | IOException | NullPointerException e) {
                        if (callback != null) {
                            System.out.println("Failed to get categories");
                        }
                    }
                }
            }
        });
    }

    public static void updateCategories(List<SendManCategory> categories) {
        OkHttpClient client = SendManAPIHandler.getSendManClient();
        JSONArray jsonCategories = SendManCategories.toJson(categories);
        JSONObject categoriesBody = new JSONObject();
        try {
            categoriesBody.put("categories", jsonCategories);
        } catch (JSONException e) {
            System.out.println("Failed to update categories");
        }
        System.out.println("@@@@ Hello" + categoriesBody.toString());
        RequestBody body = RequestBody.create(categoriesBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(SendManAPIHandler.getUrlFromPath("categories/user/" + SendMan.getUserId()))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("Failed to update categories");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) {
                if (!response.isSuccessful()) {
                    System.out.println("Failed to update categories");
                } else {
                    SendManDataCollector.addSdkEvent(new SendManSDKEvent("User categories saved", null));
                    System.out.println("Successfully updated categories");
                }
            }
        });
    }

    private static String getUrlFromPath(String path) {
        SendManConfig config = SendMan.getConfig();
        String serverUrl = config.getServerUrl() != null ? config.getServerUrl() : "https://api.sendman.io/app-sdk";
        return serverUrl + '/' + path;
    }

    public static class APICallback {
        public void onDataSendError() {}
        public void onCategoriesRetrieved() {}
    }

    private static OkHttpClient getSendManClient() {
        SendManConfig config = SendMan.getConfig();
        return new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(config.getAppKey(), config.getAppSecret()))
                .build();
    }
    public static class BasicAuthInterceptor implements Interceptor {

        private String credentials;

        BasicAuthInterceptor(String user, String password) {
            this.credentials = Credentials.basic(user, password);
        }

        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build();
            return chain.proceed(authenticatedRequest);
        }

    }

}

