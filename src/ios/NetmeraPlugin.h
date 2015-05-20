//
//  NetmeraPlugin.h
//  NetmeraPhonegap
//
//  Created by ufukSerdogan on 18/05/15.
//
//
#import "AppDelegate.h"
#import <Cordova/CDV.h>

@interface NetmeraPlugin : CDVPlugin


- (void)initNetmera: (CDVInvokedUrlCommand *)command;
- (void)getInstallationId: (CDVInvokedUrlCommand *)command;
- (void)addTags: (CDVInvokedUrlCommand *)command;
- (void)overrideTags: (CDVInvokedUrlCommand *)command;
- (void)removeTags: (CDVInvokedUrlCommand *)command;
- (void)unregisterAllTags: (CDVInvokedUrlCommand *)command;
- (void)getAvailableTags: (CDVInvokedUrlCommand *)command;
- (void)addCustomFields: (CDVInvokedUrlCommand *)command;
- (void)overrideCustomFields: (CDVInvokedUrlCommand *)command;
- (void)getDeviceDetail: (CDVInvokedUrlCommand *)command;
- (void)sendEventWithKey: (CDVInvokedUrlCommand *)command;
- (NSDictionary *)getDeviceDetailDictionary:(NMDeviceDetail*)deviceDetail;

@end
