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
	}
};
module.exports = netmeraPlugin;
