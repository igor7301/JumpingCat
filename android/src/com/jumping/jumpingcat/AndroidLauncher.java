package com.jumping.jumpingcat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jumping.jumpingcat.JumpingCat;


public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;

    protected AdView adView;
    protected View gameView;

    protected AdView banner;



    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        //создём главный слой
        RelativeLayout layout = new RelativeLayout(this);
        //устанавливаем флаги, которые устанавливались в методе initialize() вместо нас
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags( WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        //представление для LibGDX
        gameView = initializeForView(new JumpingCat(this), config);

        //представление и настройка AdMob
//        AdView adView = new AdView(this, AdSize.BANNER, "ваш_ID_в_AdMob");
        adView = new AdView(this);
        //ca-app-pub-идентификатор_баннера
        adView.setAdUnitId("ca-app-pub-7135211728909018/4468793488");
        adView.setAdSize(AdSize.BANNER);
        //идентификатор_тестового_устройства
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("emulator-5554").build();
        adView.loadAd(adRequest);
        //добавление представление игрык слою
        layout.addView(gameView);

        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //добавление представление рекламы к слою
        layout.addView(adView, adParams);

        //всё соединяем в одной слое
        setContentView(layout);
    }

    protected Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_ADS:
                    adView.setVisibility(View.VISIBLE);
                    break;
                case HIDE_ADS:
                    adView.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void showAdMob(boolean show){
        handler.sendEmptyMessage(show ? 1 : 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
