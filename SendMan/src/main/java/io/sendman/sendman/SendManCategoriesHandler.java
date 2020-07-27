package io.sendman.sendman;

import java.util.ArrayList;

import io.sendman.sendman.models.SendManCategory;

class SendManCategoriesHandler {

}

//+ (id)sharedManager {
//static SMCategoriesHandler *sharedMyManager = nil;
//static dispatch_once_t onceToken;
//        dispatch_once(&onceToken, ^{
//        sharedMyManager = [[self alloc] init];
//        });
//        return sharedMyManager;
//        }
//
//TODO retries
//        + (void)getCategories {
//        [SMAPIHandler getDataForUrl:[NSString stringWithFormat:@"categories/user/%@", [Sendman getUserId]] responseHandler:^(NSHTTPURLResponse *httpResponse, NSDictionary *jsonData) {
//        if(httpResponse.statusCode == 200) {
//        NSArray *categories = jsonData ? jsonData[@"categories"] : [[NSArray alloc] init];
//        [Sendman setUserCategories: categories];
//        } else {
//        NSLog(@"Failed to get categories");
//        }
//        }];
//        }
//
//        + (void)updateCategories:(NSArray *)categories {
//        [SMAPIHandler sendDataWithJson:@{@"categories": categories} forUrl:[NSString stringWithFormat:@"categories/user/%@", [Sendman getUserId]] responseHandler:^(NSHTTPURLResponse *httpResponse) {
//        if(httpResponse.statusCode != 200) {
//        NSLog(@"Failed to update categories");
//        } else {
//        [SMDataCollector addSdkEventWithName:@"User categories saved" andValue:nil];
//        }
//        }];
//        }
