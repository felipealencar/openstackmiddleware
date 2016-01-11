package distribution;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import commonservices.naming.NamingProxy;
import distribution.beans.VMsConfigurationBean;
import distribution.services.jcloud.VmBuilder;
import distribution.services.model.RunningVM;

public class VMManagerClient {
	
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		// create an instance of Naming Service
		NamingProxy namingService = new NamingProxy("localhost", 1313);
		
		// check registered services
		System.out.println(namingService.list());
				
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:utilsconf/applicationContext.xml");
		
		List<VmBuilder> vmBuilders = context.getBean(VMsConfigurationBean.class).getVmBuilders();
		
		// look for Calculator in Naming service
		VMManagerProxy managerProxy = (VMManagerProxy) namingService.lookup("VMManager");
		
		System.out.println("############ ADD VMS ##################");
		
		for (VmBuilder vmBuilder : vmBuilders) {
			try {
				managerProxy.add(vmBuilder);
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		
		System.out.println("############ LIST VMS (BEFORE REMOTION) ##################");
		
		String vmTypeToRemove = "main-public";
		String idToRemove = null;
		
		List<RunningVM> vms = managerProxy.getRunningVms("main");
		
		for (RunningVM runningVM : vms) {
			System.out.println(runningVM.getType() + " | " + runningVM);
			if (runningVM.getType().equalsIgnoreCase(vmTypeToRemove)) {
				idToRemove = runningVM.getId();
			}
		}
		
		managerProxy.remove(idToRemove);
		
		Thread.sleep(5000);
		
		System.out.println("############ LIST VMS (AFTER REMOTION) ##################");
		
		vms = managerProxy.getRunningVms("main");
		
		for (RunningVM runningVM : vms) {
			System.out.println(runningVM.getType() + " | " + runningVM);
			if (runningVM.getType().equalsIgnoreCase(vmTypeToRemove)) {
				idToRemove = runningVM.getId();
			}
		}
	}
}
