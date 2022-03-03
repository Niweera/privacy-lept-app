package gq.niweera.pl;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    SwitchMaterial goPrivateSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goPrivateSwitch = findViewById(R.id.go_private);

        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        final PeriodicWorkRequest periodicWorkReq = new PeriodicWorkRequest.Builder(BackgroundTask.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();

        WorkManager workManager = WorkManager.getInstance(this);
        workManager.cancelAllWork();

        workManager.getWorkInfoByIdLiveData(periodicWorkReq.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null) {
                        Log.d("notifyOnlineStatus", "Status changed to : " + workInfo.getState());
                    }
                });

        goPrivateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                workManager.cancelAllWork();
                Toast.makeText(getApplicationContext(), "Privacy mode activated!",
                        Toast.LENGTH_LONG).show();
                Log.d("notifyOnlineStatus", "Privacy mode activated");
            } else {
                workManager.enqueueUniquePeriodicWork("NOTIFY-ONLINE-STATUS", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkReq);
                Toast.makeText(getApplicationContext(), "Your Privacy Lept!",
                        Toast.LENGTH_LONG).show();
                Log.d("notifyOnlineStatus", "Your Privacy Lept!");
            }

        });
    }
}