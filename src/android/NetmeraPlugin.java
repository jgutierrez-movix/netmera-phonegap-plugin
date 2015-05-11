package org.apache.cordova.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.netmera.mobile.Logging.LogLevel;
import com.netmera.mobile.Netmera;
import com.netmera.mobile.NetmeraDeviceDetail;
import com.netmera.mobile.NetmeraEvent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraProperties;
import com.netmera.mobile.NetmeraPushService;

public class NetmeraPlugin extends CordovaPlugin {
	public static final String TAG = "NetmeraPlugin";
	public static final String ACTION_SET_TAGS = "setTags";
	public static final String ACTION_SEND_EVENT = "sendCustomEvent";

	private static Application app;
	private static Class<? extends Activity> pushActivityClass;
	private static String googleProjectId;

	public static void initNetmera(Application app, Class<? extends Activity> pushActivityClass) {
		NetmeraPlugin.app = app;
		NetmeraPlugin.pushActivityClass = pushActivityClass;

		String apiKey = getStringByKey(app, "netmera_api_key");
		NetmeraPlugin.googleProjectId = getStringByKey(app, "netmera_google_project_id");

		Log.d(TAG, "Initializing Netmera.. ApiKey: " + apiKey + " and googleProjectId:" + googleProjectId);

		NetmeraProperties prop = new NetmeraProperties.Builder(apiKey).googleProjectNumber(googleProjectId).pushActivityClass(pushActivityClass).exceptionReportingEnabled(true)
				.logLevel(LogLevel.INFO).locationTrackingEnabled(true).exceptionReportingEnabled(true).pushInboxEnabled(false).popupEnabled(false).build();
		Netmera.init(app, prop);
	}

	private static String getStringByKey(Application app, String key) {
		int resourceId = app.getResources().getIdentifier(key, "string", app.getPackageName());
		return app.getString(resourceId);
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals(ACTION_SET_TAGS)) {
			this.setTags(convertJSONArrayToStringList(args.getJSONArray(0)), args.getBoolean(1), callbackContext);
			return true;
		}

		if (action.equals(ACTION_SEND_EVENT)) {
			if (args.length() == 1) {
				this.sendCustomEvent(args.getString(0), null, callbackContext);
				return true;
			} else if (args.length() == 2) {
				this.sendCustomEvent(args.getString(0), args.getJSONObject(1), callbackContext);
				return true;
			}
		}

		return false;
	}

	private void setTags(final List<String> tags, final boolean overrideTags, final CallbackContext callbackContext) {
		NetmeraDeviceDetail deviceDetail = new NetmeraDeviceDetail(app.getApplicationContext(), googleProjectId, pushActivityClass);
		deviceDetail.setTags(tags);
		deviceDetail.setOverrideTags(overrideTags);
		try {
			NetmeraPushService.register(deviceDetail);
			callbackContext.success();
		} catch (NetmeraException e) {
			callbackContext.error(e.getMessage());
		}
	}

	private void sendCustomEvent(final String eventName, final JSONObject eventData, final CallbackContext callbackContext) {
		try {
			if (eventData == null) {
				NetmeraEvent.sendCustomEvent(eventName);
				callbackContext.success();
			} else {
				NetmeraEvent.sendCustomEventWithData(eventName, eventData);
				callbackContext.success();
			}
		} catch (NetmeraException e) {
			callbackContext.error(e.getMessage());
		}
	}

	private List<String> convertJSONArrayToStringList(JSONArray jsonArray) throws JSONException {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(jsonArray.getString(i));
		}
		return list;
	}
}
