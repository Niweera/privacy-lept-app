package gq.niweera.pl;


import android.app.KeyguardManager;
import android.content.Context;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class BackgroundTask extends Worker {
    public BackgroundTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);

        IntStream.range(0, 90).forEach(n -> {
            if (!isStopped()) {
                if (!keyguardManager.isDeviceLocked()) {
                    try {
                        notifyOnlineStatus();
                    } catch (InvalidKeyException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                SystemClock.sleep(10000);
            }
        });

        return Result.success();
    }

    void notifyOnlineStatus() throws InvalidKeyException, NoSuchAlgorithmException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        String crc_token = UUID.randomUUID().toString();
        String nonce = UUID.randomUUID().toString();
        String message = String.format("crc_token=%s&nonce=%s", crc_token, nonce);
        String SECRET = getApplicationContext().getString(R.string.secret);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        String hmac = Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes()), Base64.NO_WRAP);

        String signature = String.format("sha256=%s", hmac);

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://online.niweera.gq")).newBuilder();
        urlBuilder.addQueryParameter("crc_token", crc_token);
        urlBuilder.addQueryParameter("nonce", nonce);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("privacy-lept-host-type", "MOBILE")
                .header("privacy-lept-signature", signature)
                .url(url)
                .build();

        try {
            client.newCall(request).execute();
            Log.d("notifyOnlineStatus", "The user is online: MOBILE");
        } catch (IOException e) {
            Log.e("notifyOnlineStatus", e.toString());
        }
    }
}