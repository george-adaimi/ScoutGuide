package com.georgeadaimi.scoutguide;

import android.os.Parcel;
import android.os.Parcelable;

public class Emplacement implements Parcelable {
	/**
	 * Item text
	 */
	@com.google.gson.annotations.SerializedName("name")
	private String campsiteName;
	
	@com.google.gson.annotations.SerializedName("type")
	private String campsiteType;
	
	@com.google.gson.annotations.SerializedName("owner")
	private String campsiteOwner;
	
	@com.google.gson.annotations.SerializedName("city")
	private String campsiteCity;
	
	@com.google.gson.annotations.SerializedName("telephone")
	private String campsitePhone;
	
	@com.google.gson.annotations.SerializedName("capacity")
	private String campsiteCapacity;
	
	@com.google.gson.annotations.SerializedName("district")
	private String campsiteDistrict;
	
	@com.google.gson.annotations.SerializedName("remarks")
	private String campsiteRemarks;

	/**
	 * Item Id
	 */
	@com.google.gson.annotations.SerializedName("id")
	private String campsiteId;


	/**
	 * ToDoItem constructor
	 */
	public Emplacement() {

	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Initializes a new ToDoItem
	 * 
	 * @param text
	 *            The item text
	 * @param id
	 *            The item id
	 */
	public Emplacement(String text, String id) {
		this.setName(text);
		this.setId(id);
	}

	/**
	 * Returns the item text
	 */
	public String getName() {
		return campsiteName;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setName(String text) {
		campsiteName = text;
	}

	/**
	 * Returns the item id
	 */
	public String getId() {
		return campsiteId;
	}

	/**
	 * Sets the item id
	 * 
	 * @param id
	 *            id to set
	 */
	public final void setId(String id) {
		campsiteId = id;
	}

	public String getType() {
		return campsiteType;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setType(String text) {
		campsiteType = text;
	}
	
	
	public String getOwner() {
		return campsiteOwner;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setOwner(String text) {
		campsiteOwner = text;
	}
	
	
	
	public String getCity() {
		return campsiteCity;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setCity(String text) {
		campsiteCity = text;
	}
	
	public String getPhone() {
		return campsitePhone;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setPhone(String text) {
		campsitePhone = text;
	}
	
	public String getCapacity() {
		return campsiteCapacity;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setCapacity(String text) {
		campsiteCapacity = text;
	}
	
	public String getDistrict() {
		return campsiteDistrict;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setDistrict(String text) {
		campsiteDistrict = text;
	}
	
	public String getRemarks() {
		return campsiteRemarks;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setRemarks(String text) {
		campsiteRemarks = text;
	}
	@Override
	public boolean equals(Object o) {
		return o instanceof Emplacement && ((Emplacement) o).campsiteName == campsiteName;
	}
	// 99.9% of the time you can just ignore this
	@Override
	public int describeContents() {
		return 0;
	}

	// write your object's data to the passed-in Parcel
	@Override
	public void writeToParcel(Parcel out, int flags) {

		out.writeString(campsiteName);
		out.writeString(campsiteType);
		out.writeString(campsiteOwner);
		out.writeString(campsiteCity);
		out.writeString(campsitePhone);
		out.writeString(campsiteCapacity);
		out.writeString(campsiteDistrict);
		out.writeString(campsiteRemarks);

	}

	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
	public static final Parcelable.Creator<Emplacement> CREATOR = new Parcelable.Creator<Emplacement>() {
		public Emplacement createFromParcel(Parcel in) {
			return new Emplacement(in);
		}

		public Emplacement[] newArray(int size) {
			return new Emplacement[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated with it's values
	private Emplacement(Parcel in) {
		campsiteName = in.readString();
		campsiteType = in.readString();
		campsiteOwner = in.readString();
		campsiteCity = in.readString();
		campsitePhone = in.readString();
		campsiteCapacity = in.readString();
		campsiteDistrict = in.readString();
		campsiteRemarks = in.readString();
	}

}
