package io.branch.branchandroiddemo;

import android.app.Activity;

//import com.pinterest.activity.webhook.WebhookActivity;
//import com.pinterest.base.Constants;
//import com.pinterest.kit.data.Preferences;
//import com.pinterest.network.json.PinterestJsonObject;
//import com.yozio.android.Yozio;


import java.util.concurrent.CountDownLatch;

import io.branch.referral.Branch;


/**
 * Abstraction layer for deep link provider data.
 */
public class PinterestDeepLinkMetaData {

    public static PinterestJsonObject getMetaData(Activity activity) {
        PinterestJsonObject metaData = BranchMetaData.getMetaData(activity);

        return metaData;
    }

//    public static PinterestJsonObject getInstallMetaData() {
//        PinterestJsonObject metaData = BranchMetaData.getInstallMetaData();
//
//        return metaData;
//    }

//    public static String getInstallMetaDataAsString() {
//        String data = Preferences.persisted().getString(Constants.PREF_BRANCH_DATA, null);
//
//        return data;
//    }
//
//    public static void initialize(Activity activity) {
//        Branch branch = Branch.getInstance();
//        BranchMetaData.initBranch(branch, activity, null);
//        Yozio.initialize(activity);
//    }
//
//    public static boolean isReferredDeepLink(PinterestJsonObject metaData) {
//        return (metaData.has(WebhookActivity.YOZIO_URL_KEY)
//            || metaData.optBoolean(BranchMetaData.IS_BRANCH_LINK, false));
//    }

}
