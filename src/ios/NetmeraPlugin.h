//
//

#import "AppDelegate.h"
#import <Cordova/CDV.h>

@interface NetmeraPlugin : CDVPlugin


- (void)initialize: (CDVInvokedUrlCommand *)command;
- (void)getInstallationId: (CDVInvokedUrlCommand *)command;
- (void)setTags: (CDVInvokedUrlCommand *)command;
- (void)removeTags: (CDVInvokedUrlCommand *)command;
- (void)unregister: (CDVInvokedUrlCommand *)command;
- (void)getAllAplicationTags: (CDVInvokedUrlCommand *)command;
- (void)setCustomFields: (CDVInvokedUrlCommand *)command;
- (void)overrideCustomFields: (CDVInvokedUrlCommand *)command;
- (void)getDeviceDetail: (CDVInvokedUrlCommand *)command;
- (void)sendEvent: (CDVInvokedUrlCommand *)command;
- (void)sendEventWithData: (CDVInvokedUrlCommand *)command;
- (void)registerPush:(CDVInvokedUrlCommand *)command;
- (void)updateLocation: (CDVInvokedUrlCommand *)command;

- (NSDictionary *)getDeviceDetailDictionary:(NMDeviceDetail*)deviceDetail;

@end
