package prot.rich;

public class UserData {
	private static UserData instance;
	private String name, licenseDate, id,uid;

	public String getName() {
		return this.name;
	}

	public String getLicenseDate() {
		return this.licenseDate;
	}

	public String getID() {
		return this.id;
	}
	public String getUID() {
		return this.uid;
	}
	public void setUID(String uid) {
		this.uid = uid;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setLicenseDate(String date) {
		this.licenseDate = date;
	}

	public void setID(String id) {
		this.id = id;
	}

	public static UserData instance() {
		return instance == null ? instance = new UserData() : instance;
	}
}
