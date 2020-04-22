#import <Cordova/CDVViewController.h>
#import <Cordova/CDVCommandDelegateImpl.h>
#import <Cordova/CDVCommandQueue.h>

@interface WebViewViewController : CDVViewController

@end

@interface WebViewCommandDelegate : CDVCommandDelegateImpl
@end

@interface WebViewCommandQueue : CDVCommandQueue
@end
