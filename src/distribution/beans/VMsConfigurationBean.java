package distribution.beans;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.jclouds.compute.RunNodesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import distribution.services.jcloud.VMManagerImpl;
import distribution.services.jcloud.VmBuilder;
import distribution.services.model.TypeConfiguration;
import utilsconf.TextFile;

@Component
public class VMsConfigurationBean {

	public static final String SAMPLE_FILE_NAME = "sample.json";	
	public static final String SAMPLE_FILE_FILE_INTO_PROJECT = "distribution/services/files/" + SAMPLE_FILE_NAME;
	
	private final Gson gson = new Gson();
	private List<VMsConfiguration> configurations = new ArrayList<VMsConfiguration>();
	
	private List<VmBuilder> vmBuilders = new ArrayList<VmBuilder>();
	
	private Logger logger;
	
	@PostConstruct
	public void init() {
		
		File file;
		try {
			file = new File(Thread.currentThread().getContextClassLoader().getResource(SAMPLE_FILE_FILE_INTO_PROJECT).toURI().getPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		
		if (!file.exists()) {
			return;
		}
		
		if (!file.getName().endsWith("json"))
			return;

		try {
			String content = new TextFile(file.getAbsolutePath()).getContent();
			VMsConfiguration clusterConfiguration = gson.fromJson(
					content, VMsConfiguration.class);
			configurations.add(clusterConfiguration);
			
			
			for (VMsConfiguration conf : configurations) {
				
				for (TypeConfiguration typeConf : conf.getTypes()) {
					
					VmBuilder vmBuilder = VmBuilder.newBuilder()
							.cluster(conf.getCluster())
							.type(typeConf.getType())
							.hardwareName(typeConf.getHardwareName())
							.imageName(typeConf.getImageName())
							.network(typeConf.getNetwork());
					
					vmBuilders.add(vmBuilder);
					
				}
			}
			
			
		} catch (IOException ex) {
			logger.error("Error reading json config file: " + file, ex);
		}
	}


	public List<VmBuilder> getVmBuilders() {
		return vmBuilders;
	}


	public void setVmBuilders(List<VmBuilder> vmBuilders) {
		this.vmBuilders = vmBuilders;
	}

}
