package com.jeremypunsalan.takehome.deliverycostcalculator.model.view;

public class Delivery {
	
	Double weight;
	Double height;
	Double width;
	Double length;
	Double volume;
	String voucherCode;
	String key;
	Double cost;
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
