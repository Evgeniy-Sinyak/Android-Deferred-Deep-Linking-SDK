package io.branch.branchandroiddemo;

import android.app.Activity;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

/**
 * Handles metadata from Branch deep links
 */
public class BranchMetaData {

    public static final String IS_BRANCH_LINK = "+clicked_branch_link";
    public static final String BRANCH_URL = "$deeplink_path";
    private static final String IS_BRANCH_INSTALL = "+is_first_session";
    private static final int BRANCH_TIMEOUT = 5000;

    public static void initBranch(Branch branch, Activity activity, final CountDownLatch latch) {
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (latch != null) {
                    latch.countDown();
                }
                if (error == null) {
                    if (referringParams.optBoolean(IS_BRANCH_INSTALL, false)) {
                        //saveMetaData(referringParams.toString());
                    }
                } else {
                    //PLog.error("Branch metadata not retrieved: " + error.getMessage());
                }
            }
        }, activity.getIntent().getData(), activity);
    }

    //Why not init in init callback

//    public static void initBranch1(Branch branch, Activity activity, final CountDownLatch latch) {
//        branch.initSession(new Branch.BranchReferralInitListener() {
//            @Override
//            public void onInitFinished(JSONObject referringParams, BranchError error) {
//
//                if (error == null) {
//                    if (referringParams.optBoolean(IS_BRANCH_INSTALL, false)) {
//                        String appStartSource = putAppStartParamsInAuxData(source, uri.toString());
////                Pinalytics.event(EventType.APP_START, null, _auxData);
////                AnalyticsApi.submitTrackFunnel(FunnelActions.APP_START + appStartSource, null);
//                    }
//                } else {
//                    //PLog.error("Branch metadata not retrieved: " + error.getMessage());
//                }
//            }
//        }, activity.getIntent().getData(), activity);
//    }


    public static PinterestJsonObject getMetaData(Activity activity) {
        Branch branch = Branch.getInstance();
        final CountDownLatch latch = new CountDownLatch(1);

        initBranch(branch, activity, latch);

        try {
            latch.await(BRANCH_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignore) { }

        return new PinterestJsonObject(branch.getLatestReferringParams().toString());
    }

//    public static PinterestJsonObject getInstallMetaData() {
//        String metaData = Preferences.persisted().getString(Constants.PREF_BRANCH_DATA,
//            new PinterestJsonObject().toString());
//        return new PinterestJsonObject(metaData);
//    }
//
//    private static void saveMetaData(String metaData) {
//        Preferences.persisted().set(Constants.PREF_BRANCH_DATA, metaData);
//    }
}
