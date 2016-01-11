package distribution.services.jcloud;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import distribution.VMManagerCallback;
import distribution.services.model.RunningVM;;

@Component
public class VMManagerImpl implements VMManager {

	@Autowired
	private JCloudsClient jCloudsClient;
	private Logger logger;

	@PostConstruct
	public void init(){
		logger =  Logger.getLogger(this.getClass());
	}
	
	public int countRunningVms(String cluster, String type) {
		int nodesRunning = jCloudsClient.countRunningVms(cluster, type);
		logger.debug(nodesRunning + " nodes running.");
		return nodesRunning;
	}

	public boolean add(VmBuilder vmBuilder)
			throws RunNodesException 
	{
		NodeMetadata node = jCloudsClient.createVm(vmBuilder.getCluster(), vmBuilder.getType(), 
				vmBuilder.getHardwareName(), vmBuilder.getImageName(), vmBuilder.getNetwork());
		
		if (node != null){
			logger.debug("node=" + node.getId() + " type=" + vmBuilder.getType() + " cluster" + vmBuilder.getCluster() + " added");
		
			
			return true;
		}
		
		return false;
	}

	public boolean remove(String id) throws Exception, Throwable {
		
		return jCloudsClient.destroyVM(id);
	}

	public Set<String> getRunningTypes(String cluster) {
		return jCloudsClient.getRunningTypes(cluster);
	}

	public List<RunningVM> getRunningVms(String cluster, String type) {
		List<RunningVM> runningVms = new ArrayList<RunningVM>();
		List<ComputeMetadata> nodesMetadata = jCloudsClient.getRunningNodes(cluster, type);
		
		for (ComputeMetadata computeMetadata : nodesMetadata) {
			RunningVM vm = parse(computeMetadata);
			runningVms.add(vm);
		}
		
		return runningVms;
	}
	
	public List<RunningVM> getRunningVms(String cluster) {
		
		List<RunningVM> runningVms = new ArrayList<RunningVM>();
		
		List<ComputeMetadata> nodesMetadata = jCloudsClient.getRunningNodes(cluster);
		
		for (ComputeMetadata computeMetadata : nodesMetadata) {
			RunningVM vm = parse(computeMetadata);
			runningVms.add(vm);
		}
		
		return runningVms;
	}
	

	private RunningVM parse(ComputeMetadata meta){
		NodeMetadata metadata = jCloudsClient.getNodeMetadata(meta.getId());
		
		String id = meta.getId();
		String cluster = meta.getLocation().getId();
		String type = metadata.getGroup();
		String hardwareName = metadata.getHardware().getName();
		String imageName = jCloudsClient.getImageById(metadata.getImageId()).getName();
		Set<String> publicIps = metadata.getPublicAddresses();
		Set<String> privateIps = metadata.getPrivateAddresses();

		RunningVM vm = new RunningVM(id, type, cluster, hardwareName, imageName, publicIps, privateIps);
		
		return vm;
	}

	@Override
	public boolean add(VmBuilder vmBuilder, VMManagerCallback vmManagerCallback) throws Exception, Throwable {
		return this.add(vmBuilder);
	}

}
