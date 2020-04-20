package com.matrixdeveloper.aivita.ApiServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "InsectDefence";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String DEVICE_TOKEN = "NA";
    private static final String LOGIN_REMEMBER = "IsLogin";
    private static final String MOBILE = "No_Mobile";
    private static final String USER_NAME = "No_Username";
    private static final String UNIQUE_ID = "No_UniqueId";
    private static final String SALES_PERSON_ID = "No_SalesId";
    private static final String PASSWORD = "No_Password";
    private static final String ADDRESS = "No_Address";
    private static final String IDPROOFTYPE = "No_IdProofType";
    private static final String IMAGE = "No_Image";
    private static final String EMAIL = "No_Email";
    private static final String FILE_NAME = "No_File";
    private static final String REMEMBER_ME = "No_Remember";
    private static final String FINGERPRINT_KEY = "NO_FINGERPRINT";
    private static final String MyLatitude = "No_Latitude";
    private static final String MyLongitude = "No_Longitude";
    private static final String ProfilePic = "NoPic";
    private static final String EmployeeType = "NoType";
    private static final String EmployeeId = "NoId";
    private static final String EmployeeName = "NoName";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String MacAddress = "NoMac";

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clearSharedPreference(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    public void setLoginRemember(String isFirstTime) {
        editor.putString(LOGIN_REMEMBER, isFirstTime);
        editor.apply();
    }

    public void setUniqueId(String uniqueId) {
        editor.putString(UNIQUE_ID, uniqueId);
        editor.apply();
    }

    public void setMacAddress(String macAddress) {
        editor.putString(MacAddress, macAddress);
        editor.apply();
    }

    public void setEmployeeType(String employeeType) {
        editor.putString(EmployeeType, employeeType);
        editor.apply();
    }


    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public String getIsLogin() {
        return pref.getString(LOGIN_REMEMBER, LOGIN_REMEMBER);
    }

    public String getDeviceToken() {
        return pref.getString(DEVICE_TOKEN, DEVICE_TOKEN);
    }

    public void setDeviceToken(String deviceToken) {
        editor.putString(DEVICE_TOKEN, deviceToken);
        editor.apply();
    }

    public String getUserName() {
        return pref.getString(USER_NAME, USER_NAME);
    }

    public void setUserName(String userName) {
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public String getMobile() {
        return pref.getString(MOBILE, MOBILE);
    }

    public void setMobile(String userMobile) {
        editor.putString(MOBILE, userMobile);
        editor.apply();
    }

    public String getuniqueId() {
        return pref.getString(UNIQUE_ID, UNIQUE_ID);
    }

    public String getSalesPersonId() {
        return pref.getString(SALES_PERSON_ID, SALES_PERSON_ID);
    }

    public void setSalesPersonId(String salesPersonId) {
        editor.putString(SALES_PERSON_ID, salesPersonId);
        editor.apply();
    }

    public String getAddress() {
        return pref.getString(ADDRESS, ADDRESS);
    }

    public void setAddress(String address) {
        editor.putString(ADDRESS, address);
        editor.apply();
    }

    public String getIdprooftype() {
        return pref.getString(IDPROOFTYPE, IDPROOFTYPE);
    }

    public void setIdprooftype(String idprooftype) {
        editor.putString(IDPROOFTYPE, idprooftype);
        editor.apply();
    }

    public String getImage() {
        return pref.getString(IMAGE, IMAGE);
    }

    public void setImage(String image) {
        editor.putString(IMAGE, image);
        editor.apply();
    }

    public String getEmail() {
        return pref.getString(EMAIL, EMAIL);
    }

    public void setEmail(String email) {
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getFingerprintKey() {
        return pref.getString(FINGERPRINT_KEY, FINGERPRINT_KEY);
    }

    public void setFingerprintKey(String fingerprintKey) {
        editor.putString(FINGERPRINT_KEY, fingerprintKey);
        editor.apply();
    }

    public String getFileName() {
        return pref.getString(FILE_NAME, FILE_NAME);
    }

    public void setFileName(String fileName) {
        editor.putString(FILE_NAME, fileName);
        editor.apply();
    }

    public String getPassword() {
        return pref.getString(PASSWORD, PASSWORD);
    }

    public void setPassword(String password) {
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public String getRememberMe() {
        return pref.getString(REMEMBER_ME, REMEMBER_ME);
    }

    public void setRememberMe(String rememberMe) {
        editor.putString(REMEMBER_ME, rememberMe);
        editor.apply();
    }

    public String getMyLatitude() {
        return pref.getString(MyLatitude, MyLatitude);
    }

    public void setMyLatitude(String myLatitude) {
        editor.putString(MyLatitude, myLatitude);
        editor.apply();
    }

    public String getMyLongitude() {
        return pref.getString(MyLongitude, MyLongitude);
    }

    public String getMacAddress() {
        return pref.getString(MacAddress, MacAddress);
    }


    public void setMyLongitude(String myLongitude) {
        editor.putString(MyLongitude, myLongitude);
        editor.apply();
    }

    public void setEmployeeId(String employeeId) {
        editor.putString(EmployeeId, employeeId);
        editor.apply();
    }

    public String getProfilePic() {
        return pref.getString(ProfilePic, ProfilePic);
    }

    public String getEmployeeType() {
        return pref.getString(EmployeeType, EmployeeType);
    }

    public String getEmployeeId() {
        return pref.getString(EmployeeId, EmployeeId);
    }
    public String getEmployeeName() {
        return pref.getString(EmployeeName, EmployeeName);
    }


    public void setProfilePic(String profilePic) {
        editor.putString(ProfilePic, profilePic);
        editor.apply();
    }

    public void setEmployeeName(String employeeName) {
        editor.putString(EmployeeName, employeeName);
        editor.apply();
    }

}