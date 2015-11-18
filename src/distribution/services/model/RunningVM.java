package distribution.services.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RunningVM {

	private String id;
	private String type;
	private String cluster;
	private String hardware;
	private String image;
	private Set<String> publicIps = new HashSet<String>();
	private Set<String> privateIps = new HashSet<String>();

	public RunningVM(String id, String type, String cluster, String hardware,
			String image, Set<String> publicIps, Set<String> privateIps) {
		super();
		this.id = id;
		this.type = type;
		this.cluster = cluster;
		this.hardware = hardware;
		this.image = image;
		this.publicIps = publicIps;
		this.privateIps = privateIps;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getHardware() {
		return hardware;
	}

	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Set<String> getPublicIps() {
		return publicIps;
	}

	public void setPublicIps(Set<String> publicIps) {
		this.publicIps = publicIps;
	}

	public Set<String> getPrivateIps() {
		return privateIps;
	}

	public void setPrivateIps(Set<String> privateIps) {
		this.privateIps = privateIps;
	}

	@Override
	public String toString() {
		return "RunningVM [id=" + id + ", type=" + type + ", cluster="
				+ cluster + ", hardware=" + hardware + ", image=" + image
				+ ", publicIps=" + Arrays.toString(publicIps.toArray()) + ", privateIps=" + Arrays.toString(privateIps.toArray())
				+ "]";
	}
	
}
