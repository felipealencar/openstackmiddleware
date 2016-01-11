package distribution.services.jcloud;

import java.util.List;
import java.util.Set;

import distribution.VMManagerCallback;
import distribution.services.model.RunningVM;;

public interface VMManager {
	
	/**
	 * Returns the number of vms running for a type
	 * 
	 * @param cluster the cluster
	 * @param type the specified type
	 * 
	 * @return the number of vms
	 */
	int countRunningVms(String cluster, String type);

	/**
	 * Adds a new vm in a cluster for a type
	 * 
	 * @param vmBuilder the vm builder
	 * 
	 * @return true if ok and false if not ok
	 */
	boolean add(VmBuilder vmBuilder, VMManagerCallback vmManagerCallback) throws Exception, Throwable;

	/**
	 * Removes a vm in a cluster of a type
	 * 
	 * @param String the vm builder
	 * 
	 * @return true if removed and false if not
	 */
	boolean remove(String id) throws Exception, Throwable;

	/**
	 * Returns the running vm types in a cluster
	 * 
	 * @param cluster the cluster
	 * 
	 * @return the types
	 */
	Set<String> getRunningTypes(String cluster);
	
	/**
	 * Returns the running vms for a type in a cluster
	 * 
	 * @param cluster the cluster
	 * @param type the type
	 * 
	 * @return the running vms
	 */
	List<RunningVM> getRunningVms(String cluster, String type);
	
	/**
	 * Returns the running vms for a type in a cluster
	 * 
	 * @param cluster the cluster
	 * 
	 * @return the running vms
	 */
	List<RunningVM> getRunningVms(String cluster)  throws Exception, Throwable;
}
