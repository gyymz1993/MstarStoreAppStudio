package cn.mstar.store.entity;

import java.util.List;

public class FavoriteManagerItem {

	public String response, error, message;
	public List<InnerData> data;

	@Override
	public String toString() {
		return "FavoriteManagerItem{" +
				"response='" + response + '\'' +
				", error='" + error + '\'' +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}

	public class InnerData {

		public int categoryId;
		public String categoryName;
		public int count;
		public List<Favorite> favorites;

		@Override
		public String toString() {
			return "InnerData{" +
					"categoryId=" + categoryId +
					", categoryName='" + categoryName + '\'' +
					", count=" + count +
					", favorites=" + favorites +
					'}';
		}

		public class Favorite {
			
		 
			
			@Override
			public String toString() {
				return "Favorite [Id=" + Id + ", proId=" + proId + ", pic="
						+ pic + ", title=" + title + ", price=" + price
						+ ", addDate=" + addDate + ", categoryName="
						+ categoryName + ", categoryId=" + categoryId + "]";
			}
			public int Id, proId;
			public String pic, title;
			public Double price;
			public String addDate, categoryName;
			public int categoryId;
		}

	}

}
