package com.morgans_eletranic_ltd;

public class LabourData {
	
	private String labour="";
	private String labourType="Day";
	private String PricePerDay="";
	private String PricePerHour="";
	private String UpdatedBy="";
	private String UpdatedOn="";
	private String CreatedBy="";
	private String CreatedOn="";
	private String LabourCostID="";
	private String qty="1";
	private int margin=20;
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getLabour() {
		return labour;
	}
	public void setLabour(String labour) {
		this.labour = labour;
	}
	public String getLabourType() {
		return labourType;
	}
	public void setLabourType(String labourType) {
		this.labourType = labourType;
	}
	public String getPricePerDay() {
		return PricePerDay;
	}
	public void setPricePerDay(String pricePerDay) {
		PricePerDay = pricePerDay;
	}
	public String getPricePerHour() {
		return PricePerHour;
	}
	public void setPricePerHour(String pricePerHour) {
		PricePerHour = pricePerHour;
	}
	public String getUpdatedBy() {
		return UpdatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		UpdatedBy = updatedBy;
	}
	public String getUpdatedOn() {
		return UpdatedOn;
	}
	public void setUpdatedOn(String updatedOn) {
		UpdatedOn = updatedOn;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	public String getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(String createdOn) {
		CreatedOn = createdOn;
	}
	public String getLabourCostID() {
		return LabourCostID;
	}
	public void setLabourCostID(String labourCostID) {
		LabourCostID = labourCostID;
	}
	public int getMargin() {
		return margin;
	}
	public void setMargin(int margin) {
		this.margin = margin;
	}

}
