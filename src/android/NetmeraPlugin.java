package org.apache.cordova.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.netmera.mobile.NetmeraCallback;
import com.netmera.mobile.NetmeraDeviceDetail;
import com.netmera.mobile.NetmeraEvent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraGeoLocation;
import com.netmera.mobile.NetmeraProperties;
import com.netmera.mobile.NetmeraPushService;

public class NetmeraPlugin extends CordovaPlugin {
	public static final String TAG = "NetmeraPlugin";
	public static final String ACTION_REGISTER = "register";
	public static final String ACTION_SET_TAGS = "setTags";
	public static final String ACTION_UPDATE_LOCATION = "updateUserLocation";
	public static final String ACTION_SET_CUSTOM_FIELDS = "setCustomFields";
	public static final String ACTION_SEND_EVENT = "sendCustomEvent";
	public static final String ACTION_UNREGISTER = "unregister";
	public static final String ACTION_REMOVE_TAGS = "removeTags";
	public static final String ACTION_GET_DEVICE_DETAIL = "getDeviceDetail";

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
		if (action.equals(ACTION_REGISTER)) {
			this.register(callbackContext);
			return true;
		}

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

			return false;
		}

		if (action.equals(ACTION_UPDATE_LOCATION)) {
			this.updateUserLocation(args.getDouble(0), args.getDouble(1), callbackContext);
			return true;
		}

		if (action.equals(ACTION_SET_CUSTOM_FIELDS)) {
			this.setCustomFields(args.getJSONObject(0), callbackContext);
			return true;
		}

		if (action.equals(ACTION_UNREGISTER)) {
			this.unregister(null, callbackContext);
			return true;
		}

		if (action.equals(ACTION_REMOVE_TAGS)) {
			this.unregister(convertJSONArrayToStringList(args.getJSONArray(0)), callbackContext);
			return true;
		}

		if (action.equals(ACTION_GET_DEVICE_DETAIL)) {
			this.getDeviceDetail(callbackContext);
			return true;
		}

		return false;
	}

	private void register(final CallbackContext callbackContext) {
		try {
			NetmeraPushService.register(app.getApplicationContext(), googleProjectId, pushActivityClass);
			callbackContext.success();
		} catch (NetmeraException e) {
			callbackContext.error(e.getMessage());
		}
	}

	private void getDeviceDetail(final CallbackContext callbackContext) {
		NetmeraPushService.getDeviceDetailInBackground(app.getApplicationContext(), new NetmeraCallback<NetmeraDeviceDetail>() {
			@Override
			public void onSuccess(NetmeraDeviceDetail deviceDetail) {
				try {
					callbackContext.success(convertDeviceDetailToJSONObject(deviceDetail));
				} catch (JSONException e) {
					callbackContext.error(e.getMessage());
				}
			}

			@Override
			public void onFail(NetmeraException e) {
				callbackContext.error(e.getMessage());
			}
		});
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

	private void updateUserLocation(Double latitude, Double longitude, final CallbackContext callbackContext) {
		NetmeraDeviceDetail deviceDetail = new NetmeraDeviceDetail(app.getApplicationContext(), googleProjectId, pushActivityClass);
		try {
			deviceDetail.setDeviceLocation(new NetmeraGeoLocation(latitude, longitude));
			NetmeraPushService.register(deviceDetail);
			callbackContext.success();
		} catch (NetmeraException e) {
			callbackContext.error(e.getMessage());
		}
	}

	private void setCustomFields(JSONObject customFieldsObj, final CallbackContext callbackContext) throws JSONException {
		NetmeraDeviceDetail deviceDetail = new NetmeraDeviceDetail(app.getApplicationContext(), googleProjectId, pushActivityClass);
		try {
			Map<String, Object> customFields = new HashMap<String, Object>();

			Iterator<String> keys = customFieldsObj.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				customFields.put(key, customFieldsObj.get(key));
			}

			deviceDetail.setCustomFields(customFields);
			NetmeraPushService.register(deviceDetail);
			callbackContext.success();
		} catch (NetmeraException e) {
			callbackContext.error(e.getMessage());
		}
	}

	private void unregister(List<String> tags, final CallbackContext callbackContext) {
		if (tags == null) {
			NetmeraPushService.unregister(app.getApplicationContext());
		} else {
			NetmeraDeviceDetail deviceDetail = new NetmeraDeviceDetail(app.getApplicationContext(), googleProjectId, pushActivityClass);
			deviceDetail.setTags(tags);
			NetmeraPushService.unregister(deviceDetail);
		}
		callbackContext.success();
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

	private JSONObject convertDeviceDetailToJSONObject(NetmeraDeviceDetail deviceDetail) throws JSONException {
		JSONObject json = new JSONObject();
		NetmeraGeoLocation deviceLocation = deviceDetail.getDeviceLocation();
		if (deviceLocation != null) {
			JSONObject locationObj = new JSONObject();
			locationObj.put("latitude", deviceLocation.getLatitude());
			locationObj.put("longitude", deviceLocation.getLongitude());
			json.put("location", locationObj);
		}

		Map<String, Object> customFields = deviceDetail.getCustomFields();
		if (customFields != null) {
			JSONObject customFieldsObj = new JSONObject(customFields);
			json.put("customFields", customFieldsObj);
		}

		List<String> tags = deviceDetail.getTags();
		if (tags != null) {
			JSONArray tagsArr = new JSONArray(tags);
			json.put("tags", tagsArr);
		}

		return json;
	}
}
