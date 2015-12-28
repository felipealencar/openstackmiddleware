package distribution;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import distribution.services.jcloud.VMManager;
import distribution.services.jcloud.VmBuilder;
import distribution.services.model.RunningVM;
import utilsconf.UtilsConf;

public class VMManagerProxy extends ClientProxy implements VMManager {

	public VMManagerProxy() throws UnknownHostException {
		this.host = InetAddress.getLocalHost().getHostName();
		this.port = UtilsConf.nextPortAvailable();
	}
	
	public VMManagerProxy(String h, int p) {
		this.host = h;
		this.port = p;
	}
	
	@Override
	public int countRunningVms(String cluster, String type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean add(VmBuilder vmBuilder) throws Exception, Throwable {
		Invocation inv = new Invocation();
		Termination ter = new Termination();
		ArrayList<Object> parameters = new ArrayList<Object>();
		class Local {
		}
		;
		String methodName;
		Requestor requestor = new Requestor();

		// information received from Client
		methodName = Local.class.getEnclosingMethod().getName();
		parameters.add(vmBuilder);

		// information sent to Requestor
		inv.setClientProxy(this);
		inv.setOperationName(methodName);
		inv.setParameters(parameters);

		// invoke Requestor
		ter = requestor.invoke(inv);

		// @ Result sent back to Client
//		return (Float) ter.getResult();
		return true;
	}

	@Override
	public boolean remove(VmBuilder vmBuilder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> getRunningTypes(String cluster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RunningVM> getRunningVms(String cluster, String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
