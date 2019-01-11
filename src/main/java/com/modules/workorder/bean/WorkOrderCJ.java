package com.modules.workorder.bean;import io.swagger.annotations.ApiModelProperty;/******************************************************************************* * javaBeans * WORKORDER --> Workorder  * <table explanation> * @author 2018-11-22 14:44:29 *  */	/** * 工单_撤机 * @author DoubleLi * @time 2018年11月22日 *  */public class WorkOrderCJ extends WorkOrderBase implements java.io.Serializable {	/**	 * 	 */	private static final long serialVersionUID = -6098677751790611556L;	/** 电话权限，隔开 字典 **/	@ApiModelProperty(value="电话权限，隔开",required=true)	private String phonePermission;	/** 电话业务，隔开 字典 **/	@ApiModelProperty(value="电话业务，隔开",required=true)	private String phoneBusiness;	/** 撤机地址 **/	private String withdrawAddress;	/** 撤机费用 **/	private String withdrawMoney;	/** 话机处理-字典 **/	private String dhjpro;	/** 月租 **/	@ApiModelProperty(value="月租",required=true)	private String monthRent;	/** 结算金额 **/	@ApiModelProperty(value="结算金额",required=true)	private String settleMoney;	@ApiModelProperty(value="话机类型",required=true)	private Integer dhjtype;	@ApiModelProperty(value="机务",required=false)	private String jw;	@ApiModelProperty(value="线务",required=false)	private String xw;		public String getJw() {		return jw;	}	public void setJw(String jw) {		this.jw = jw;	}	public String getXw() {		return xw;	}	public void setXw(String xw) {		this.xw = xw;	}	public String getPhonePermission() {		return phonePermission;	}	public void setPhonePermission(String phonePermission) {		this.phonePermission = phonePermission;	}	public String getPhoneBusiness() {		return phoneBusiness;	}	public void setPhoneBusiness(String phoneBusiness) {		this.phoneBusiness = phoneBusiness;	}	public String getWithdrawAddress() {		return withdrawAddress;	}	public void setWithdrawAddress(String withdrawAddress) {		this.withdrawAddress = withdrawAddress;	}	public String getWithdrawMoney() {		return withdrawMoney;	}	public void setWithdrawMoney(String withdrawMoney) {		this.withdrawMoney = withdrawMoney;	}	public String getDhjpro() {		return dhjpro;	}	public void setDhjpro(String dhjpro) {		this.dhjpro = dhjpro;	}	public String getMonthRent() {		return monthRent;	}	public void setMonthRent(String monthRent) {		this.monthRent = monthRent;	}	public String getSettleMoney() {		return settleMoney;	}	public void setSettleMoney(String settleMoney) {		this.settleMoney = settleMoney;	}	public Integer getDhjtype() {		return dhjtype;	}	public void setDhjtype(Integer dhjtype) {		this.dhjtype = dhjtype;	}		}