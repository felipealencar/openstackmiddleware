package distribution;

import java.io.IOException;

import commonservices.naming.NamingProxy;
import distribution.CalculatorInvoker;
import distribution.CalculatorProxy;

public class VMManagerServer {

	public static void main(String[] args) throws IOException, Throwable {
		VMManagerInvoker invoker = new VMManagerInvoker();

		// remote object
		VMManagerProxy manager = new VMManagerProxy();

		// obtain instance of Naming Service
		NamingProxy namingService = new NamingProxy("localhost", 1313);

		// register calculator in Naming service
		namingService.bind("VMManager1", manager);

		// register calculator in Naming service
		namingService.bind("VMManager2", manager);
		
		// invoke Invoker
		invoker.invoke(manager);
	}
}
