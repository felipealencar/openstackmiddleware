package distribution.services.jcloud;

import java.io.File;
import java.io.Serializable;

public class VmBuilder implements Serializable {

	private String cluster;
	private String type;
	private String hardwareName;
	private String imageName;
	private String network;
	private File scriptToRun;
	private int applicationPort;

	private String loadBalancerModel;
	private String loadBalancerRule;
	private String loadBalancerUrl;
	private String loadBalancerUser;
	private String loadBalancerPassword;
	
	public static VmBuilder newBuilder(){
		return new VmBuilder();
	}

	public String getCluster() {
		return cluster;
	}

	public VmBuilder cluster(String cluster) {
		this.cluster = cluster;
		return this;
	}

	public String getType() {
		return type;
	}

	public VmBuilder type(String type) {
		this.type = type;
		return this;
	}

	public String getHardwareName() {
		return hardwareName;
	}

	public VmBuilder hardwareName(String hardwareName) {
		this.hardwareName = hardwareName;
		return this;
	}

	public String getImageName() {
		return imageName;
	}

	public VmBuilder imageName(String imageName) {
		this.imageName = imageName;
		return this;
	}

	public String getNetwork() {
		return network;
	}

	public VmBuilder network(String network) {
		this.network = network;
		return this;
	}

	public File getScriptToRun() {
		return scriptToRun;
	}

	public VmBuilder scriptToRun(File scriptToRun) {
		this.scriptToRun = scriptToRun;
		return this;
	}

	public int getApplicationPort() {
		return applicationPort;
	}

	public VmBuilder applicationPort(int applicationPort) {
		this.applicationPort = applicationPort;
		return this;
	}

	public String getLoadBalancerModel() {
		return loadBalancerModel;
	}

	public VmBuilder loadBalancerModel(String loadBalancerModel) {
		this.loadBalancerModel = loadBalancerModel;
		return this;
	}

	public String getLoadBalancerRule() {
		return loadBalancerRule;
	}

	public VmBuilder loadBalancerRule(String loadBalancerRule) {
		this.loadBalancerRule = loadBalancerRule;
		return this;
	}

	public String getLoadBalancerUrl() {
		return loadBalancerUrl;
	}

	public VmBuilder loadBalancerUrl(String loadBalancerUrl) {
		this.loadBalancerUrl = loadBalancerUrl;
		return this;
	}

	public String getLoadBalancerUser() {
		return loadBalancerUser;
	}

	public VmBuilder loadBalancerCredentials(String loadBalancerUser, String loadBalancerPassword) {
		this.loadBalancerUser = loadBalancerUser;
		this.loadBalancerPassword = loadBalancerPassword;
		return this;
	}

	public String getLoadBalancerPassword() {
		return loadBalancerPassword;
	}
}
