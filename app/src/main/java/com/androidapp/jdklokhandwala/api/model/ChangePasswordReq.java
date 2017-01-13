package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 10-01-2017.
 */

public class ChangePasswordReq {

    Long UserID;
    String NewPassword;
    String OldPassword;

    public ChangePasswordReq(Long userID, String newPassword, String oldPassword) {
        UserID = userID;
        NewPassword = newPassword;
        OldPassword = oldPassword;
    }

    public Long getUserID() {
        return UserID;
    }

    public void setUserID(Long userID) {
        UserID = userID;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }
}
