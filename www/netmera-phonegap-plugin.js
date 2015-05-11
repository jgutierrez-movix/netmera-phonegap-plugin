var netmeraPlugin = {
	addTags: function (tags, successCallback, errorCallback) {
		cordova.exec(
			successCallback,
			errorCallback,
			'NetmeraPlugin',
			'setTags',
			[tags, false]
		);
	},
	
	overrideTags: function (tags, successCallback, errorCallback) {
		cordova.exec(
			successCallback,
			errorCallback,
			'NetmeraPlugin',
			'setTags',
			[tags, true]
		);
	},
	
	sendEvent: function (eventName, successCallback, errorCallback) {
		console.log("sending event: " + eventName);
		cordova.exec(
			successCallback,
			errorCallback,
			'NetmeraPlugin',
			'sendCustomEvent',
			[eventName]
		);
	},
	
	sendEventWithData: function (eventName, eventData, successCallback, errorCallback) {
		cordova.exec(
			successCallback,
			errorCallback,
			'NetmeraPlugin',
			'sendCustomEvent',
			[eventName, eventData]
		);
	},
	
	updateLocation: function (latitude, longitude, successCallback, errorCallback) {
		cordova.exec(
			successCallback,
			errorCallback,
			'NetmeraPlugin',
			'updateUserLocation',
			[latitude, longitude]
		);
	},
	
	setCustomFields: function (customFields, successCallback, errorCallback) {
		cordova.exec(
			successCallback,
			errorCallback,
			'NetmeraPlugin',
			'setCustomFields',
			[customFields]
		);
	},
	
	setCustomFields: function (successCallback, errorCallback) {
		cordova.exec(
			successCallback,
			errorCallback,
			'NetmeraPlugin',
			'unregister',
			[]
		);
	},
	
	removeTags: function (tags, successCallback, errorCallback) {
		cordova.exec(
			successCallback,
			errorCallback,
			'NetmeraPlugin',
			'removeTags',
			[tags]
		);
	},
	
	getDeviceDetail: function (successCallback, errorCallback) {
		cordova.exec(
			successCallback,
			errorCallback,
			'NetmeraPlugin',
			'getDeviceDetail',
			[]
		);
	}
};
module.exports = netmeraPlugin;
