package apextechies.gkquiz_hm.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import apextechies.gkquiz_hm.R;
import apextechies.gkquiz_hm.activity.MainActivity;

/**
 * Created by Shankar on 1/2/2018.
 */

public class SplasScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splas_activity);
        Thread lTimer = new Thread() {

            public void run() {

                try {
                    int lTimer1 = 0;
                    while (lTimer1 < 3000) {
                        sleep(100);
                        lTimer1 = lTimer1 + 100;
                    }
                    startActivity(new Intent(SplasScreen.this, MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                finally {
                    finish();
                }
            }
        };
        lTimer.start();

    }
}
