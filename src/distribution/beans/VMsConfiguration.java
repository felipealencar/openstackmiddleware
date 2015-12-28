package distribution.beans;

import com.google.gson.annotations.SerializedName;

import distribution.services.model.TypeConfiguration;

public class VMsConfiguration {

	@SerializedName("cluster")
	private String cluster;
	@SerializedName("interval_in_minutes")
	private int intervalInMinutes;
	private TypeConfiguration[] types;
	
	
	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public int getIntervalInMinutes() {
		return intervalInMinutes;
	}

	public void setIntervalInMinutes(int intervalInMinutes) {
		this.intervalInMinutes = intervalInMinutes;
	}
	
	public TypeConfiguration[] getTypes() {
		return types;
	}
	
	public void setTypes(TypeConfiguration[] types) {
		this.types = types;
	}
}
