package com.tangdou.panda.meta;

public enum BudgetTypeEnum {

	CONSUME(0,"consume", "金额"),
	CLICK(1,"click","点击"),
	DISPLAY(2,"display","曝光");
	
	private String title;
	private String value;
	private Integer id;
	private BudgetTypeEnum(Integer id, String value, String title) {
		this.id=id;
		this.title = title;
		this.value = value;
	}
	
	/**
	 * 标题
	 * @return 标题
	 */
	public String title() {
		return title;
	}
	
	/**
	 * 值
	 * @return 值
	 */
	public String value() {
		return value;
	}
	/**
	 * 值
	 * @return 值
	 */
	public Integer id() {
		return id;
	}
	
    
	public static BudgetTypeEnum get(int id){
		for (BudgetTypeEnum vtype : BudgetTypeEnum.values()) {
			if (vtype.id.intValue() == id){
				return vtype;
			}
		}
		return null;
	}
}
