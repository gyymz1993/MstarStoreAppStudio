package cn.mstar.store.entity;

/**
 * 存放门店信息
 * @author wenjundu 2015-7-16
 *
 */
public class StoreInfo {

	private String store_id;
	private String store_label;//门店图像URL
	private String  store_name;//门店名称
	private String distance;//距离
	private String wtime;//营业时间
	private String tel;//联系电话
	private Double latitude;//纬度
	private Double longitude;//经度

	public StoreInfo(String store_id,String store_label, String store_name, String distance, String wtime, String tel, Double latitude, Double longitude) {
		this.store_label = store_label;
		this.store_name = store_name;
		this.distance = distance;
		this.wtime = wtime;
		this.tel = tel;
		this.latitude = latitude;
		this.longitude = longitude;
		this.store_id=store_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getStore_label() {
		return store_label;
	}

	public void setStore_label(String store_label) {
		this.store_label = store_label;
	}
	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getWtime() {
		return wtime;
	}

	public void setWtime(String wtime) {
		this.wtime = wtime;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	//	public Double getLatitude() {
//		return latitude;
//	}
//	public void setLatitude(Double latitude) {
//		this.latitude = latitude;
//	}
//	public Double getLongitude() {
//		return longitude;
//	}
//	public void setLongitude(Double longitude) {
//		this.longitude = longitude;
//	}
//	public StoreInfo(String storeURL,String  name,String distance,String time,String phone,Double longitude,Double latitude){
//		this.storeURL=storeURL;
//		this.name=name;
//		this.distance=distance;
//		this.time=time;
//		this.phone=phone;
//		this.longitude=longitude;
//		this.latitude=latitude;
//	}
//	public String getStoreURL() {
//		return storeURL;
//	}
//	public void setStoreURL(String storeURL) {
//		this.storeURL = storeURL;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getDistance() {
//		return distance;
//	}
//	public void setDistance(String distance) {
//		this.distance = distance;
//	}
//	public String getTime() {
//		return time;
//	}
//	public void setTime(String time) {
//		this.time = time;
//	}
//	public String getPhone() {
//		return phone;
//	}
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}

}
