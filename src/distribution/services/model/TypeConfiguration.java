package distribution.services.model;

import com.google.gson.annotations.SerializedName;

public class TypeConfiguration {

	private String type;
	
	@SerializedName("hardware_name")
	private String hardwareName;
	
	@SerializedName("image_name")
	private String imageName;
	
	private String network;
	
	@SerializedName("min_vms")
	private int minVms;
	
	@SerializedName("max_vms")
	private int maxVms;
	
	@SerializedName("consecutive_periods_to_check")
	private int consecutivePeriodsToCheck;
	
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getHardwareName() {
		return hardwareName;
	}

	public void setHardwareName(String hardwareName) {
		this.hardwareName = hardwareName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public int getMinVms() {
		return minVms;
	}

	public void setMinVms(int minVms) {
		this.minVms = minVms;
	}

	public int getMaxVms() {
		return maxVms;
	}

	public void setMaxVms(int maxVms) {
		this.maxVms = maxVms;
	}

	public int getConsecutivePeriodsToCheck() {
		return consecutivePeriodsToCheck;
	}

	public void setConsecutivePeriodsToCheck(int consecutivePeriodsToCheck) {
		this.consecutivePeriodsToCheck = consecutivePeriodsToCheck;
	}
}
