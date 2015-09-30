package io.branch.branchandroiddemo;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

//import com.pinterest.activity.webhook.WebhookActivity;
//import com.pinterest.api.remote.AnalyticsApi;
//import com.pinterest.base.Constants;
//import com.pinterest.kit.data.Preferences;
//import com.pinterest.network.json.PinterestJsonObject;
//import com.pinterest.schemas.event.AppStartType;
//import com.pinterest.schemas.event.EventType;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import io.branch.referral.Branch;

/**
 * Helper for deep link tracking.
 */
public class DeepLinkLogging {

    private HashMap<String, String> _auxData;
    private PinterestJsonObject _metaData;

    public DeepLinkLogging() {
        _auxData = new HashMap<>();
    }


    /**
     * Report app start and type with metadata if applicable
     * @param activity the report is coming from (probably Webhook Activity)
     * @param source of the deeplink
     * @return Future
     */
    FutureTask<Void> future;
    public FutureTask<Void> reportAppStart(final Activity activity, final String source) {
        final Uri uri = activity.getIntent().getData();
        future = new FutureTask<>(new Callable<Void>() {
            @Override
            public Void call() {
                _metaData = PinterestDeepLinkMetaData.getMetaData(activity);
                Log.d("BranchTesting","Metadata is" +  _metaData.toString() );
                Log.d("BranchTesting","Latest Param  is" + Branch.getInstance().getLatestReferringParams());
//                String appStartSource = putAppStartParamsInAuxData(source, uri.toString());
//                Pinalytics.event(EventType.APP_START, null, _auxData);
//                AnalyticsApi.submitTrackFunnel(FunnelActions.APP_START + appStartSource, null);
                return null;
            }
        });

//        Runnable future1 = new Runnable() {
//            @Override
//            public void run() {
//                _metaData = PinterestDeepLinkMetaData.getMetaData(activity);
//                Log.d("BranchTesting","Metadata is" +  _metaData.getParamJson());
//            }
//        };
        new Thread(future).start();
        return future;
    }


//    /**
//     * Report deeplink metadata about the first ever authentication after an install
//     * @param eventType the type of authentication event we are recording (login or signup)
//     */
//    public void reportInstallAuth(EventType eventType) {
//        boolean firstAuth = Preferences.persisted().getBoolean(Constants.PREF_FIRST_AUTH, true);
//        if (!firstAuth) {
//            return;
//        }
//
//        _metaData = PinterestDeepLinkMetaData.getInstallMetaData();
//        if (PinterestDeepLinkMetaData.isReferredDeepLink(_metaData)) {
//            putGeneralParamsInAuxData();
//            putFullURLInAuxData();
//            Pinalytics.event(eventType, null, _auxData);
//            AnalyticsApi.submitRegisterEvent(eventType.name().toLowerCase(), null);
//        }
//
//        Preferences.persisted().set(Constants.PREF_FIRST_AUTH, false);
//    }
//
//    /**
//     * Get any params we need from a deeplink and add them to the auxData hashmap
//     */
//    private void putGeneralParamsInAuxData() {
//        String[] auxDataParams = {AuxDataKey.UTM_SOURCE, AuxDataKey.UTM_MEDIUM,
//            AuxDataKey.UTM_CAMPAIGN};
//        for (String param : auxDataParams) {
//            if (_metaData.has(param)) {
//                _auxData.put(param, _metaData.getString(param));
//            }
//        }
//        if (_metaData.has(Constants.YOZIO_KEY_UNAUTHID)) {
//            _auxData.put(AuxDataKey.MWEB_UNAUTH_ID, _metaData.getString(Constants.YOZIO_KEY_UNAUTHID));
//        }
//    }
//
//    /**
//     *  Determine the source of an app start
//     */
//    private String putAppStartParamsInAuxData(String source, String uri) {
//        String appStartSource = "";
//        if (source != null) {
//            if (WebhookActivity.SOURCE_PUSH_NOTIF.equals(source) ||
//                    WebhookActivity.SOURCE_PULL_NOTIF.equals(source)) {
//                _auxData.put(AuxDataKey.APP_START_SOURCE, AppStartType.NOTIFICATION.toString());
//                appStartSource = "_notif";
//            }
//        } else {
//            _auxData.put(AuxDataKey.FULL_URL, uri);
//            putGeneralParamsInAuxData();
//            if (PinterestDeepLinkMetaData.isReferredDeepLink(_metaData)) {
//                _auxData.put(AuxDataKey.APP_START_SOURCE, AppStartType.DEEPLINK.toString());
//                appStartSource = "_deeplink";
//            } else {
//                _auxData.put(AuxDataKey.APP_START_SOURCE, AppStartType.WEB_URL.toString());
//                appStartSource = "_weburl";
//            }
//        }
//        return appStartSource;
//    }
//
//    private void putFullURLInAuxData() {
//        if (_metaData.has(WebhookActivity.YOZIO_URL_KEY)) {
//            _auxData.put(AuxDataKey.FULL_URL, _metaData.getString(WebhookActivity.YOZIO_URL_KEY));
//        } else if (_metaData.has(BranchMetaData.BRANCH_URL)) {
//            _auxData.put(AuxDataKey.FULL_URL, _metaData.getString(BranchMetaData.BRANCH_URL));
//        }
//    }

}
