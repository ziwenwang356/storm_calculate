package com.tangdou.panda.meta;

public enum PriceUnitEnum {
	小时(0),
	天(1),
	周(2),
	月(3),
	点击(4),
	曝光(5);
	
	private int unit;
	
	private PriceUnitEnum(int state) {
		this.unit = state;
	}
	
	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public static PriceUnitEnum get(int unit){
		for (PriceUnitEnum ade : PriceUnitEnum.values()) {
			if (ade.unit == unit){
				return ade;
			}
		}
		return null;
	}
}
