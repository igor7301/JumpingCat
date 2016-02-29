package com.jumping.pandajump;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;


public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {

    private static final int LOAD_ADS = 2;
    private static final int ADS_STATE = 3;
    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;

    protected AdView adView;
    protected View gameView;
    protected InterstitialAd mInterstitialAd;
    protected boolean interstitialAdLoaded;
    private AdRequest adRequestInterstisial;
    private boolean failedToLoadInterstitialAd;


    private boolean clickOnAd;
    private boolean adClosed;
    private Game game;





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
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        //представление для LibGDX


        game = new MyMainGame(this, new StartAppAd(this));
        gameView = initializeForView(game, config);

        //представление и настройка AdMob
//        AdView adView = new AdView(this, AdSize.BANNER, "ваш_ID_в_AdMob");
        adView = new AdView(this);
        //ca-app-pub-идентификатор_баннера
        adView.setAdUnitId("ca-app-pub-testbanner");
        adView.setAdSize(AdSize.BANNER);
        //идентификатор_тестового_устройства
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("emulator-5554").build();
        adView.loadAd(adRequest);
        //добавление представление игрык слою
        layout.addView(gameView);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7135211728909018/1055191884");

        adRequestInterstisial = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("emulator-5554").build();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adClosed = true;

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                adClosed  = false;


            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                failedToLoadInterstitialAd = false;
                adClosed = false;
                showAdMobInterstitial();

            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                clickOnAd = true;
                adClosed = false;

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);

                switch (errorCode) {
                    case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                        Gdx.app.log("ADS_ERROR", "ERROR_CODE_INTERNAL_ERROR");
                        break;
                    case AdRequest.ERROR_CODE_INVALID_REQUEST:
                        Gdx.app.log("ADS_ERROR", "ERROR_CODE_INVALID_REQUEST");
                        break;
                    case AdRequest.ERROR_CODE_NETWORK_ERROR:
                        Gdx.app.log("ADS_ERROR", "ERROR_CODE_NETWORK_ERROR");
                        break;
                    case AdRequest.ERROR_CODE_NO_FILL:
                        Gdx.app.log("ADS_ERROR", "ERROR_CODE_NO_FILL");
                        break;
                }

                failedToLoadInterstitialAd = true;

                ((MyMainGame) game).getMainGameScreen().resume();
                game.setScreen(((MyMainGame) game).getMainGameScreen());


            }
        });





        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //добавление представление рекламы к слою
        layout.addView(adView, adParams);

        //всё соединяем в одной слое
        setContentView(layout);


        StartAppSDK.init(this, "106159305", "201925421", true);
        StartAppAd.showSplash(this, savedInstanceState);



    }

    public boolean getFailedToLoadInterstitialAd() {
        return failedToLoadInterstitialAd;
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

    protected Handler handlerInterstitial = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_ADS:
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    else {
                        mInterstitialAd.loadAd(adRequestInterstisial);
                        //для проверки загрузки рекламы
//                        if (mInterstitialAd.isLoading()) {
//                            while (mInterstitialAd.isLoading()) {
//
//                            }
//                        }
                    }
                    break;
                case LOAD_ADS:
                    if (!mInterstitialAd.isLoaded()) {

                        mInterstitialAd.loadAd(adRequestInterstisial);
                    }
                    break;
                case ADS_STATE:
                    if (!mInterstitialAd.isLoaded()) {

                        interstitialAdLoaded = true;

                    }
                    else {
                        interstitialAdLoaded = false;

                    }
                    break;

            }
        }
    };



    @Override
    public void showAdMob(boolean show){
        handler.sendEmptyMessage(show ? 1 : 0);
    }

    @Override
    public void showAdMobInterstitial(){
        handlerInterstitial.sendEmptyMessage(1);
        //mInterstitialAd.show();
    }

    @Override
    public boolean userClosedAd() {
        return adClosed;
    }

    @Override
    public boolean adModIsLoaded() {
        handlerInterstitial.sendEmptyMessage(3);
        return interstitialAdLoaded;
    }

    @Override
    public void loadAdMod() {
        handlerInterstitial.sendEmptyMessage(2);
    }

    @Override
    public boolean doesUserClickOnAd() {
        return clickOnAd;
    }

    @Override
    public void setUserClickOnAd(boolean clickOnAd) {
        this.clickOnAd = clickOnAd;
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
