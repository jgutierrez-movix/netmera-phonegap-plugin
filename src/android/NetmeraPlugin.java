package org.apache.cordova.core;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.netmera.mobile.Netmera;
import com.netmera.mobile.NetmeraProperties;
import com.netmera.mobile.NetmeraProperties.Builder;
import com.netmera.mobile.Logging.LogLevel;
import com.netmera.mobile.NetmeraDeviceDetail;
import com.netmera.mobile.NetmeraPushService;
import com.netmera.mobile.NetmeraException;

public class NetmeraPlugin extends CordovaPlugin {
    public static final String TAG = "NetmeraPlugin";
    public static final String ACTION_REGISTER = "register";
	
	private static Application app;
	private static Class<? extends Activity> pushActivityClass;
	private static String googleProjectId;

    public static void initNetmera(Application app, Class<? extends Activity> pushActivityClass) {
		NetmeraPlugin.app = app;
		NetmeraPlugin.pushActivityClass = pushActivityClass;
		
        String apiKey = getStringByKey(app, "netmera_api_key");
        NetmeraPlugin.googleProjectId = getStringByKey(app, "netmera_google_project_id");
        
        Log.d(TAG, "Initializing Netmera.. ApiKey: " + apiKey + " and googleProjectId:" + googleProjectId);
		
		NetmeraProperties prop = new NetmeraProperties.Builder(apiKey)
			.googleProjectNumber(googleProjectId)
			.pushActivityClass(pushActivityClass)
			.exceptionReportingEnabled(true)
			.logLevel(LogLevel.INFO)
			.locationTrackingEnabled(true)
			.exceptionReportingEnabled(true)
			.pushInboxEnabled(false)
			.popupEnabled(false)
			.build();
		Netmera.init(app, prop);
    }

    private static String getStringByKey(Application app, String key) {
        int resourceId = app.getResources().getIdentifier(key, "string", app.getPackageName());
        return app.getString(resourceId);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals(ACTION_REGISTER)) {
            this.register(args, callbackContext);
            return true;
        }
		
        return false;
    }
	
	private void register(JSONArray args, CallbackContext callbackContext) {
		cordova.getThreadPool().execute(new Runnable() {
            public void run() {
				NetmeraDeviceDetail deviceDetail = new NetmeraDeviceDetail(app.getApplicationContext(), googleProjectId, pushActivityClass);
				List<String> tags = new ArrayList<String>();
				tags.add("News");
				tags.add("Sports");
				deviceDetail.setTags(tags);
				try {
					NetmeraPushService.register(deviceDetail);
				} catch (NetmeraException e) {
					// Handle exception
				}
				callbackContext.success();
			}
		});
	}
}

