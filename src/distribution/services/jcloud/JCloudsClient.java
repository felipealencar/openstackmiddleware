package distribution.services.jcloud;

import static org.jclouds.compute.options.RunScriptOptions.Builder.wrapInInitScript;
import static org.jclouds.compute.options.TemplateOptions.Builder.networks;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.options.RunScriptOptions;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.scriptbuilder.domain.Statement;
import org.jclouds.scriptbuilder.domain.Statements;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import com.google.inject.Module;

@Component
public class JCloudsClient implements Closeable {

	public static final String NAME_REGEX = "-";
	
	private ComputeService computeService;
	private Logger logger;

	@Value("${jcloud.provider}")
	private String provider;
	@Value("${jcloud.identity}")
	private String identity;
	@Value("${jcloud.credential}")
	private String credential;
	@Value("${jcloud.endpoint}")
	private String endpoint;
	
	@PostConstruct
	public void init(){
		logger = Logger.getLogger(this.getClass());

		Iterable<Module> modules = ImmutableSet.<Module> of(
				new SshjSshClientModule(),
				new SLF4JLoggingModule());

		ContextBuilder builder  = ContextBuilder.newBuilder(provider)
				.endpoint(endpoint)
				.credentials(identity, credential)
				.modules(modules);

		computeService = builder.buildView(ComputeServiceContext.class).getComputeService();
		logger.info("JClouds client authenticated on host: " + endpoint + "\n");

	}

	/**
	 * Get running nodes in a group of a type
	 * 
	 * @param group the group
	 * @param type the type
	 * 
	 * @return the running nodes
	 */
	public List<ComputeMetadata> getRunningNodes(String group, String type) {
		if (computeService == null) 
			throw new IllegalStateException("Session not authenticated!");

		Set<? extends ComputeMetadata> nodes = computeService.listNodes();
		List<ComputeMetadata> filteredNodes = new LinkedList<ComputeMetadata>();

		logger.debug("Nodes:");
		for (ComputeMetadata node : nodes) {
		
			if (isRunning(node.getId()) && isInGroup(node, group) && isType(node, type)){ 
				logger.debug("Name = " + node.getName() + " | Id = " + node.getId());
				filteredNodes.add(node);
			}
		}

		return filteredNodes;
	}

	/**
	 * Get running types in a group
	 * 
	 * @param group the group
	 * 
	 * @return the running types in a group
	 */
	public Set<String> getRunningTypes(String group) {
		if (computeService == null) 
			throw new IllegalStateException("Session not authenticated!");

		Set<? extends ComputeMetadata> nodes = computeService.listNodes();
		Set<String> nodesTypes = new HashSet<String>();

		logger.debug("Nodes:");
		for (ComputeMetadata node : nodes) {
			if (isRunning(node.getId()) && isInGroup(node, group)){ 
				String type = getType(node);
				nodesTypes.add(type);
				
				logger.debug("Type = " + node.getName() + " | Id = " + node.getId());
			}
		}

		return nodesTypes;
	}


	/**
	 * Count the vms in a group of a type
	 * 
	 * @param group the group
	 * @param type the type
	 * 
	 * @return the amount
	 */
	public int countRunningVms(String group, String type){
		return getRunningNodes(group, type).size();
	}

	/**
	 * Get a hardware by name
	 * 
	 * @param name the name of the hardware
	 * 
	 * @return the hardware or null if not found
	 */
	public Hardware getHardwareByName(String name) {
		if (computeService == null) 
			throw new IllegalStateException("Session not authenticated!");

		Set<? extends Hardware> hardwareProfiles = computeService.listHardwareProfiles();
		for (Hardware hardware : hardwareProfiles) {
			if (hardware.getName().equals(name)) {
				return hardware;
			}
		}        

		logger.warn("Hardware not found!\n");
		return null;
	}

	/**
	 * Execute a script on a node
	 * 
	 * @param nodeId the node id
	 * @param scriptToRun the script to run
	 * 
	 * @return true is the script was run ok or false if not
	 */
	public boolean executeScript(String nodeId, File scriptToRun){
		if (computeService == null) 
			throw new IllegalStateException("Session not authenticated!");
		List<String> lines = new LinkedList<String>();
		lines.add("#!/bin/bash");
		lines.add("touch teste");
		lines.add("uname -a");
		
		Statement s = Statements.createOrOverwriteFile("/opt/script.sh", lines);
		RunScriptOptions options = RunScriptOptions.Builder.runAsRoot(false);
		//ExecResponse exec = computeService.runScriptOnNode(nodeId, s, options);
		
		ExecResponse exec =computeService.runScriptOnNode(nodeId,
				"echo hello1", wrapInInitScript(false)
				                .runAsRoot(false).overrideLoginPassword("root").overrideLoginUser("root")); 
		
		System.out.println("Output: " + exec.getOutput());
		
		return true;
	}
	
	/**
	 * Get an image by name
	 * 
	 * @param name the name of the image
	 * 
	 * @return the image or null
	 */
	public Image getImageByName(String name) {
		if (computeService == null) 
			throw new IllegalStateException("Session not authenticated!");

		Set<? extends Image> images = computeService.listImages();
		for (Image image : images) {
			if (image.getName().equals(name)) {
				return image;
			}
		}        

		logger.warn("Image not found!\n");
		return null;
	}
	
	/**
	 * Get an image by id
	 * 
	 * @param id the name of the image
	 * 
	 * @return the image or null
	 */
	public Image getImageById(String id) {
		if (computeService == null) 
			throw new IllegalStateException("Session not authenticated!");

		Set<? extends Image> images = computeService.listImages();
		for (Image image : images) {
			if (image.getId().equals(id)) {
				return image;
			}
		}        

		logger.warn("Image not found!\n");
		return null;
	}

	/**
	 * Create a vm in a group of a type
	 * 
	 * @param group the group (e.g. Brazil, Peru, ...)
	 * @param type the type of the vm (e.g. Runtime, Admanager, ...)
	 * @param hardwareName the hardware to be used
	 * @param imageName the image to be used
	 * 
	 * @return the node created
	 * @throws RunNodesException 
	 * @throws IOException 
	 */
	// Felipe: Aqui você pode ignorar a conexão com o servidor do openstack (como te falei, essa parte já está funcional)
	// Cuida só da parte de implementação da fila de mensagens
	public NodeMetadata createVm(final String group, final String type, final String hardwareName, final String imageName,
			final String network) 
			throws RunNodesException {
		
		if (computeService == null) 
			throw new IllegalStateException("Session not authenticated!");

		Hardware hardware;
		Image image;

		if ((hardware = getHardwareByName(hardwareName)) == null) 
			throw new IllegalStateException(String.format("Hardware [%s] not found!", hardwareName));

		if ((image = getImageByName(imageName)) == null) 
			throw new IllegalStateException(String.format("Image [%s] not found!", imageName));
		
		TemplateBuilder builder = computeService.templateBuilder()
				.hardwareId(hardware.getId())
				.imageId(image.getId());
//				.locationId(group)
//				.options(runScript(Files.toString(new File("/opt/script.sh"), Charsets.UTF_8)))
		
		if(network != null)
			builder.options(networks(network));

		Template template = builder.build();
		
		final String builtGroupName = buildGroupName(group, type);
		Set<? extends NodeMetadata> node = computeService.createNodesInGroup(builtGroupName, 1, template);
		for (NodeMetadata nodeMetadata : node) {
			return nodeMetadata;
		}
		
		logger.debug("New VM created!\n");

		return null;                
	}	

	
	/**
	 * Destroy the VM
	 * 
	 * @param id the id
	 * 
	 * @return the status
	 */
    public boolean destroyVM(String id){
    	boolean destroyed = false;
        if (isRunning(id)) {
        	computeService.destroyNode(id);
        	destroyed = true;
        }
        return destroyed;
    }
    
    
	
	/**
	 * Close the context
	 */
	public void close() throws IOException {
		Closeables.close(computeService.getContext(), true);
	}

	/**
	 * Get node meta data by id
	 * 
	 * @param id the id
	 * 
	 * @return the node meta data
	 */
	public NodeMetadata getNodeMetadata(String id) {
		return computeService.getNodeMetadata(id);
	}

	/**
	 * Checks if the node is the group specified
	 * 
	 * @param node the node
	 * @param group the group
	 * 
	 * @return true if it is and false if not
	 */
	public boolean isInGroup(ComputeMetadata node, String group) {
		String nodeNameGroup = getGroup(node);
		return group.equalsIgnoreCase(nodeNameGroup);
	}
	
	/**
	 * Checks if the type is the type specified
	 * 
	 * @param node the node
	 * @param type the type
	 * 
	 * @return true if it is and false if not
	 */
	public boolean isType(ComputeMetadata node, String type) {
		String nodeTypeGroup = getType(node);
		return type.equalsIgnoreCase(nodeTypeGroup);
	}
	
	/**
	 * Gets a group from a node
	 * 
	 * @param node the node
	 * 
	 * @return the node group
	 */
	public String getGroup(ComputeMetadata node) {
		try{
			String nodeNameGroup = node.getName().split(NAME_REGEX)[0];
			return nodeNameGroup;
		}catch(IndexOutOfBoundsException ex){
			logger.warn(String.format("Node name [%s] doens't have a expected format.", node.getName()));
			return null;
		}
	}
	
	/**
	 * Gets a type from a node
	 * 
	 * @param node the node
	 * 
	 * @return the node type
	 */
	public String getType(ComputeMetadata node) {
		try{
			String nodeTypeGroup = node.getName().split(NAME_REGEX)[1];
			return nodeTypeGroup;
		}catch(IndexOutOfBoundsException ex){
			logger.warn(String.format("Node name [%s] doens't have a expected format.", node.getName()));
			return null;
		}
	}

	/**
	 * Build a group name based on a real group name and a type
	 * 
	 * @param group the real group name
	 * @param type the type
	 * 
	 * @return the built group name
	 */
	public String buildGroupName(String group, String type){
		return String.format("%s%s%s", group, NAME_REGEX, type);
	}
	
	/**
	 * Checks if a node is in a running type
	 * 
	 * @param id the node id
	 * 
	 * @return true if the node is running
	 */
	public boolean isRunning(String id){
		return computeService.getNodeMetadata(id).getStatus().equals(NodeMetadata.Status.RUNNING);
	}
	
}
