package com.jeremypunsalan.takehome.deliverycostcalculator.model.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Delivery item information.")
public class Delivery {
	
	@ApiModelProperty(value = "Weight of the delivery parcel. Must be in KG.", required = true)
	Double weight;
	@ApiModelProperty(value = "Height of the delivery parcel. Must be in CM.", required = true)
	Double height;
	@ApiModelProperty(value = "Width of the delivery parcel. Must be in CM.", required = true)
	Double width;
	@ApiModelProperty(value = "Length of the delivery parcel. Must be in CM.", required = true)
	Double length;
	@ApiModelProperty(hidden = true)
	Double volume;
	@ApiModelProperty(value = "Voucher code that can be applied to redeem discount. This field is optional.", required = false)
	String voucherCode;
	@ApiModelProperty(value = "API key to be used together with the voucherCode. This field is optional.", required = false)
	String key;
	@ApiModelProperty(hidden = true)
	Double cost;
	@ApiModelProperty(hidden = true)
	Double discount;
	
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public String getVoucherCode() {
		return voucherCode;
	}
	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	@Override
	public String toString() {
		return "Delivery [weight=" + weight + ", height=" + height + ", width=" + width + ", length=" + length
				+ ", volume=" + volume + ", voucherCode=" + voucherCode + ", key=" + key + ", cost=" + cost
				+ ", discount=" + discount + "]";
	}

	

}
