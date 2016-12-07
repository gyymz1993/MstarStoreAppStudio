package cn.mstar.store.entity;
public class Version {
		public String version;
		public String downloadurl;
		public boolean isUpdate;

	public void setVersion(String version) {
		this.version = version;
	}

	public void setDownloadurl(String downloadurl) {
		this.downloadurl = downloadurl;
	}

	public String getVersion() {
		return version;
	}

	public String getDownloadurl() {
		return downloadurl;
	}

	public Version(String downloadurl, String version,boolean isUpdate) {
		this.downloadurl = downloadurl;
		this.version = version;
		this.isUpdate=isUpdate;
	}

	public void setIsUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public boolean getIsUpdate() {
		return isUpdate;
	}
}