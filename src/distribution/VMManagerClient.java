package distribution;

import java.util.List;

import org.jclouds.compute.RunNodesException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import commonservices.naming.NamingProxy;
import distribution.beans.VMsConfigurationBean;
import distribution.services.jcloud.VMManagerImpl;
import distribution.services.jcloud.VmBuilder;

public class VMManagerClient implements IVMManagerCallback {
	
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		VMManagerCallback vmManagerCallback = new VMManagerCallback();
		// create an instance of Naming Service
		NamingProxy namingService = new NamingProxy("localhost", 1313);
		
		// check registered services
		System.out.println(namingService.list());
				
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:utilsconf/applicationContext.xml");
		
		List<VmBuilder> vmBuilders = context.getBean(VMsConfigurationBean.class).getVmBuilders();
		
		// look for Calculator in Naming service
		VMManagerProxy managerProxy = (VMManagerProxy) namingService.lookup("VMManager");
		
		for (VmBuilder vmBuilder : vmBuilders) {
			try {
				managerProxy.registerCallback(vmManagerCallback);
				managerProxy.add(vmBuilder);
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean vmCreated(boolean status) {
		return status;
	}

}
