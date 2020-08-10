package io.sendman.sendman;

import android.os.Build;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class SendManDataEnricher {
    private final static String SM_COUNTRY_CODE_KEY = "SMCountryCode";
    private final static String SM_LANGUAGE_KEY = "SMLanguageCode";
    private final static String SM_DEVICE_SYSTEM_NAME_KEY = "SMDeviceSystemName";
    private final static String SM_DEVICE_SYSTEM_VERSION_KEY = "SMDeviceSystemVersion";
    private final static String SM_DEVICE_MODEL_KEY = "SMDeviceModel";

    static Map<String, String> getUserEnrichedData() {
        HashMap<String, String> enrichedData = new HashMap<String, String>();
        enrichedData.put(SM_COUNTRY_CODE_KEY, Locale.getDefault().getCountry());
        enrichedData.put(SM_LANGUAGE_KEY, Locale.getDefault().getLanguage());
        enrichedData.put(SM_DEVICE_SYSTEM_NAME_KEY, "Android");
        enrichedData.put(SM_DEVICE_SYSTEM_VERSION_KEY, Build.VERSION.RELEASE);
        enrichedData.put(SM_DEVICE_MODEL_KEY, Build.MODEL);
        return enrichedData;
    }
}
