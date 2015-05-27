var netmeraPlugin = {
initialize: function(apiKey, successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'initialize', [apiKey]
                 );
},
    
register: function(successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'registerPush', []
                 );
},
    
addTags: function(tags, successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'setTags', [tags, false]
                 );
},
    
overrideTags: function(tags, successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'setTags', [tags, true]
                 );
},
    
getAllAplicationTags: function(successCallback, errorCallback) {
    cordova.exec(
                 successCalback,
                 errorCallback,
                 'NetmeraPlugin',
                 'getAllAplicationTags', []
                 );
},
    
sendEvent: function(eventName, successCallback, errorCallback) {
    console.log("sending event: " + eventName);
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'sendCustomEvent', [eventName]
                 );
},
    
sendEventWithData: function(eventName, eventData, successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'sendCustomEvent', [eventName, eventData]
                 );
},
    
updateLocation: function(latitude, longitude, successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'updateLocation', [latitude, longitude]
                 );
},
    
setCustomFields: function(customFields, successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'setCustomFields', [customFields]
                 );
},
    
overrideCustomFields: function(customFields, successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'overrideCustomFields', [customFields]
                 );
},
    
unregister: function(successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'unregister', []
                 );
},
    
removeTags: function(tags, successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'removeTags', [tags]
                 );
},
    
getDeviceDetail: function(successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'getDeviceDetail', []
                 );
},
    
getInstallationId: function(successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'getInstallationId', []
                 );
},
    
getDeviceTokenId: function(successCallback, errorCallback) {
    cordova.exec(
                 successCallback,
                 errorCallback,
                 'NetmeraPlugin',
                 'getDeviceTokenId', []
                 );
}
    
};
module.exports = netmeraPlugin;