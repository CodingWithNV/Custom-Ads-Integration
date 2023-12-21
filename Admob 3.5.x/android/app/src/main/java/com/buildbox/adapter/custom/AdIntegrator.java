package com.buildbox.adapter.custom;

import android.app.Activity;
import android.util.Log;

import com.buildbox.AdIntegratorInterface;
import com.buildbox.AdIntegratorManagerInterface;
import com.buildbox.CustomIntegrator;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class AdIntegrator implements AdIntegratorInterface, CustomIntegrator {

    // TODO - Set to implemented network name here
    private static String customNetworkName = "";
    private static String adNetworkId = "custom";
    private static String TAG = "AdIntegratorCustom";
    private static WeakReference<Activity> activity;
    private static AdIntegratorManagerInterface adIntegratorManager;

    // internal state variables for coordinating with the main Ad Controller system
    private AdLoadState bannerLoadState = AdLoadState.NONE;
    private AdLoadState interstitialLoadState = AdLoadState.NONE;
    private AdLoadState rewardedVideoLoadState = AdLoadState.NONE;
    private AdLoadState sdkLoadState = AdLoadState.NONE;

    // you can add any other variables you need like custom ad views

    /**
     * Use this method to initialize the Ad network only. Do not start loading ads here. If you are supporting banners,
     * then make sure to set the bannerView to View.GONE to start
     * @param initValues - Ignore this for custom integration
     * @param act - Use activity if you need a reference to activity or context
     */
    @Override
    public void initAds(HashMap<String, String> initValues, WeakReference<Activity> act, AdIntegratorManagerInterface managerInterface) {
        if(!sdkNeedsInit()) {
            return;
        }
        activity = act;
        adIntegratorManager = managerInterface;
        sdkLoadState = AdLoadState.LOADING;
        bannerLoadState = AdLoadState.NONE;
        interstitialLoadState = AdLoadState.NONE;
        rewardedVideoLoadState = AdLoadState.NONE;

        //Use this method to initialize the Ad network only. Do not start loading ads here. If you are supporting banners,
        //then make sure to set them to a GONE/hidden state to start

        //TODO: Remove networkFailed() and log statement below, and replace with networkLoaded() when you implement
        Log.e(TAG, "Custom ad integrator needs to be implemented");
        networkFailed();
    }

    /**
     * BEGIN: State management for SDK Init and ad readiness
     *   a default implementation is included throughout the file, it could be
     *   suitable for your custom integration or it could need modification
     *   depending on your network's SDK and its behaviors and expectations
     */

    /*
     * for reporting to the ad manager whether your SDK needs to be initialized or re-initialized
     */
    public boolean sdkNeedsInit() {
        if(sdkLoadState == AdLoadState.NONE ||
                sdkLoadState == AdLoadState.WAITING ||
                sdkLoadState == AdLoadState.FAILED) {
            return true;
        }
        return false;
    }

    /*
     * for reporting to the ad manager whether your SDK is done initializing (not necessarily that individual ads are ready)
     */
    @Override
    public boolean sdkIsReady() {
        if(sdkLoadState == AdLoadState.LOADED) {
            return true;
        }
        return false;
    }


    /*
     * as you load ads, update the adapter status so the ad manager knows when it's ready to show
     */
    @Override
    int bannerLoadState() {
        return bannerLoadState.toInt();
    }
    @Override
    int interstitialLoadState() {
        return interstitialLoadState.toInt();
    }
    @Override
    int rewardedVideoLoadState() {
        return rewardedVideoLoadState.toInt();
    }

    @Override
    public void initBanner() {
        //Load a banner. If banner is one time use, then initialize a new one before loading.
        bannerLoadState = AdLoadState.LOADING;

        //TODO: Remove bannerFailed() and call bannerLoaded() in the appropriate callback from the ad network
        bannerFailed();
    }

    @Override
    public void initInterstitial() {
        //Load an interstitial. If interstitial is one time use, then initialize a new one before loading.
        interstitialLoadState = AdLoadState.LOADING;

        //TODO: Remove interstitialFailed() and call interstitialLoaded() in the appropriate callback from the ad network
        interstitialFailed();
    }

    @Override
    public void initRewardedVideo() {
        //Load a rewarded video. If rewarded video is one time use, then initialize a new one before loading.
        rewardedVideoLoadState = AdLoadState.LOADING;

        //TODO: Remove rewardedVideoFailed() and call rewardedVideoLoaded() in the appropriate callback from the ad network
        rewardedVideoFailed();
    }

    @Override
    public boolean showBanner() {
        //Show banner
        // TODO - Call the below method when a banner ad is successfully shown to the user
        //  return whether the ad is shown or not
        //  you may need to ensure running on the ui thread:
        // if(canShowAd()) {
        //   activity.get().runOnUiThread(new Runnable() {
        //       @Override
        //       public void run() {
        //           showAd()
        //       }
        //   });
        //   return true;
        // }
        adIntegratorManager.bannerImpression(adNetworkId + " - " + customNetworkName);
    }


    @Override
    public void hideBanner() {
        //Hide banner.
    }

    @Override
    public boolean showInterstitial() {
        //show an interstitial
        // TODO - Call the below method when an interstitial ad is successfully shown to the user
        //  return whether the ad is shown or not
        //  you may need to ensure running on the ui thread:
        // if(canShowAd()) {
        //   activity.get().runOnUiThread(new Runnable() {
        //       @Override
        //       public void run() {
        //           showAd()
        //       }
        //   });
        //   return true;
        // }        
        adIntegratorManager.interstitialImpression(adNetworkId + " - " + customNetworkName);
    }

    @Override
    public boolean showRewardedVideo() {
        //Show a rewarded video
        // TODO - Call the below method when an interstitial ad is successfully shown to the user
        //  return whether the ad is shown or not
        //  you may need to ensure running on the ui thread:
        // if(canShowAd()) {
        //   activity.get().runOnUiThread(new Runnable() {
        //       @Override
        //       public void run() {
        //           showAd()
        //       }
        //   });
        //   return true;
        // }        
        adIntegratorManager.rewardedVideoImpression(adNetworkId + " - " + customNetworkName);
    }

    @Override
    public boolean isBannerVisible() {
        //return true if the banner is currently visible
        return false;
    }

    @Override
    public boolean isRewardedVideoAvailable() {
        //return true if a rewarded video is loaded and ready to show
        if(rewardedVideoLoadState == AdLoadState.LOADED) {
            return true;
        }
        return false;
    }

    @Override
    public void setUserConsent(boolean consentGiven) {
        //Use this method to set GDPR consent for ad network
    }

    @Override
    public void setTargetsChildren(boolean targetsChildren) {
        //Use this method to let ad network know children are targeted for COPPA compliance
    }

    /**
     * Call this method when a user closes an interstitial
     */
    @Override
    public void interstitialClosed() {
        Log.d(TAG, "interstitial closed");
        interstitialLoadState = AdLoadState.WAITING;
        adIntegratorManager.interstitialClosed(adNetworkId);
    }

    /**
     *  Call this method passing true if a rewarded video reward is successfully received.
     *  Call this method passing false if a rewarded video
     * @param value - was a reward received
     */
    @Override
    public void rewardedVideoDidReward(boolean value) {
        Log.d(TAG, "rewarded video did reward " + value);
        adIntegratorManager.rewardedVideoDidReward(adNetworkId, value);
    }

    /**
     * Call this method passing true if a rewarded video completes without failure
     * Call this method passing false if a rewarded video fails to show
     * @param value - did the video complete without failure
     */
    @Override
    public void rewardedVideoDidEnd(boolean value) {
        Log.d(TAG, "rewarded video did end " + value);
        rewardedVideoLoadState = AdLoadState.WAITING;
        adIntegratorManager.rewardedVideoDidEnd(adNetworkId, value);
    }

    /**
     * Call this method when the network is initialized
     */
    @Override
    public void networkLoaded() {
        Log.d(TAG, "Network loaded");
        sdkLoadState = AdLoadState.LOADED;
        adIntegratorManager.sdkLoaded(adNetworkId);
    }

    /**
     * Call this method when a banner is successfully loaded
     */
    @Override
    public void bannerLoaded() {
        bannerLoadState = AdLoadState.LOADED;
        adIntegratorManager.bannerLoaded(adNetworkId);
    }

    /**
     * Call this method when an interstitial is successfully loaded
     */
    @Override
    public void interstitialLoaded() {
        Log.d(TAG, "interstitial loaded");
        interstitialLoadState = AdLoadState.LOADED;
        adIntegratorManager.interstitialLoaded(adNetworkId);
    }

    /**
     * Call this method when a rewarded video is successfully loaded
     */
    @Override
    public void rewardedVideoLoaded() {
        Log.d(TAG, "rewarded loaded");
        rewardedVideoLoadState = AdLoadState.WAITING;
        adIntegratorManager.rewardedVideoLoaded(adNetworkId);
    }

    /**
     * Call this method when if a network fails
     */
    @Override
    public void networkFailed() {
        Log.d(TAG, "network failed");
        sdkLoadState = AdLoadState.FAILED;
        adIntegratorManager.sdkFailed(adNetworkId);
    }

    /**
     * Call this method when a banner fails for any reason
     */
    @Override
    public void bannerFailed() {
        Log.d(TAG, "banner failed");
        bannerLoadState = AdLoadState.FAILED;
        adIntegratorManager.bannerFailed(adNetworkId);
    }

    /**
     * Call this method when an interstitial fails for any reason
     */
    @Override
    public void interstitialFailed() {
        Log.d(TAG, "interstitial failed");
        interstitialLoadState = AdLoadState.FAILED;
        adIntegratorManager.interstitialFailed(adNetworkId);
    }

    /**
     * Call this method when a rewarded video fails for any reason
     */
    @Override
    public void rewardedVideoFailed() {
        Log.d(TAG, "rewarded video failed");
        rewardedVideoLoadState = AdLoadState.FAILED;
        adIntegratorManager.rewardedVideoFailed(adNetworkId);
    }

    @Override
    public void onActivityCreated(Activity activity) {
        //Use this method for handling activity lifecycle if the network requires it
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //Use this method for handling activity lifecycle if the network requires it
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //Use this method for handling activity lifecycle if the network requires it
    }


    @Override
    public void onActivityPaused(Activity activity) {
        //Use this method for handling activity lifecycle if the network requires it
    }


    @Override
    public void onActivityStopped(Activity activity) {
        //Use this method for handling activity lifecycle if the network requires it
    }


    @Override
    public void onActivityDestroyed(Activity activity) {
        //Use this method for handling activity lifecycle if the network requires it
    }

    /**
     * These methods are called automatically to reset your state after an error to allow retrying
     */
    @Override
    void clearBannerLoadStateErrors() {
        if(bannerLoadState == AdLoadState.FAILED) { 
            bannerLoadState = AdLoadState.WAITING; 
        }
    }
    @Override
    void clearInterstitialLoadStateErrors() {
        if(interstitialLoadState == AdLoadState.FAILED) {
            interstitialLoadState = AdLoadState.WAITING;
        }
    }
    @Override
    void clearRewardedVideoLoadStateErrors() {
        if(rewardedVideoLoadState == AdLoadState.FAILED) { 
            rewardedVideoLoadState = AdLoadState.WAITING; 
        }
    }

    /**
     * optionally, you can explicitly deallocate your resources here when your game quits
     * this is used for BBWorld switching between bits; normal games don't have to use it
     */
    @Override
    void cleanup() {
    }
}
