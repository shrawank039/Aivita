package org.itniorwings.aivita.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("bio")
    @Expose
    private Object bio;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("block")
    @Expose
    private String block;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("signup_type")
    @Expose
    private String signupType;
    @SerializedName("tokon")
    @Expose
    private Object tokon;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("fb_url")
    @Expose
    private Object fbUrl;
    @SerializedName("youtube_url")
    @Expose
    private Object youtubeUrl;
    @SerializedName("instagram_url")
    @Expose
    private Object instagramUrl;
    @SerializedName("m_code")
    @Expose
    private String mCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Object getBio() {
        return bio;
    }

    public void setBio(Object bio) {
        this.bio = bio;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getSignupType() {
        return signupType;
    }

    public void setSignupType(String signupType) {
        this.signupType = signupType;
    }

    public Object getTokon() {
        return tokon;
    }

    public void setTokon(Object tokon) {
        this.tokon = tokon;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getFbUrl() {
        return fbUrl;
    }

    public void setFbUrl(Object fbUrl) {
        this.fbUrl = fbUrl;
    }

    public Object getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(Object youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public Object getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(Object instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getMCode() {
        return mCode;
    }

    public void setMCode(String mCode) {
        this.mCode = mCode;
    }

}