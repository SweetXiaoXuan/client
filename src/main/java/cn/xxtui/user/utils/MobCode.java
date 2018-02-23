package cn.xxtui.user.utils;

/**
 * Mob platform error code
 * @author liumengwei
 * @Time 2017/8/2
 *
 */
public enum MobCode {
    verification_successful(200, "Verification is successful"),
    appKey_empty(405, "AppKey is empty"),
    appKey_invalid(406, "AppKey is invalid"),
    code_phone_empty(456, "The country code or phone number is empty"),
    phone_format_error(457, "Wrong format of phone number"),
    code_empty(466, "The verification code for the requested verification is empty"),
    code_frequent(467, "Request verification verification code is frequent(5 minutes with the same appkey the same number can only be verified three times)"),
    code_error(468, "Verification code error"),
    not_server_switch(474, "Did not open the server-side authentication switch")
    ;

    MobCode(int value, String description) {
        this.value = value;
        this.description = description;
    }
    private int value;
    private String description;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
