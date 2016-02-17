package com.jumping.pandajump;

/**
 * Created by ikomarov on 11.02.2016.
 */
public interface IActivityRequestHandler {
    public void showAdMob(boolean show);
    public void showAdMobInterstitial();
    boolean doesUserClickOnAd();

    void setUserClickOnAd(boolean clickOnAd);

    boolean userClosedAd();

    boolean adModIsLoaded();

    void loadAdMod();

    boolean getFailedToLoadInterstitialAd();

}