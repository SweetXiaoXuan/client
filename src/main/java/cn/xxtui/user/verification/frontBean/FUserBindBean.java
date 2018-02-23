package cn.xxtui.user.verification.frontBean;

import javax.ws.rs.FormParam;

/**
 * Third-party login related parameters
 * @since V1.0
 */
public class FUserBindBean {
	@FormParam("id")
	private String id;
	@FormParam("platform")
	private String platform;
	@FormParam("platformUid")
	private String platform_uid;
	@FormParam("avatar")
	private String avatar;
	@FormParam("username")
	private String username;
	@FormParam("nickname")
	private String nickname;
	@FormParam("phone")
	private String phone;
	@FormParam("gender")
	private Integer gender;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPlatform_uid() {
		return platform_uid;
	}
	public void setPlatform_uid(String platform_uid) {
		this.platform_uid = platform_uid;
	}
	public String getAvatar() {
		
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
