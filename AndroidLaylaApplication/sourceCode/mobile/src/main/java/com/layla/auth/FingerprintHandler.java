package com.layla.auth;

import de.adorsys.android.securestoragelibrary.*;

public class FingerprintHandler
{
    public static void saveData(String email, String password)
    {
        SecurePreferences.setValue("EMAIL", email);
        SecurePreferences.setValue("PASSWORD", password);
    }

    public static String getEmail()
    {
        return SecurePreferences.getStringValue("EMAIL", "");
    }

    public static String getPassword()
    {
        return SecurePreferences.getStringValue("PASSWORD", "");
    }

}
