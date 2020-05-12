package com.jeremypunsalan.takehome.deliverycostcalculator.model.view;

public class DeliveryInput {
	
	Double weight;
	Double height;
	Double width;
	Double length;
	String voucherCode;
	String key;
	
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
	@Override
	public String toString() {
		return "DeliveryInput [weight=" + weight + ", height=" + height + ", width=" + width + ", length=" + length
				+ ", voucherCode=" + voucherCode + ", key=" + key + "]";
	}

}
