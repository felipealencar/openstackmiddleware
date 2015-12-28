package distribution;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import utilsconf.UtilsConf;

@Component
public class CalculatorProxy extends ClientProxy implements ICalculator {

	private static final long serialVersionUID = 1L;

	// TODO objID
	public CalculatorProxy() throws UnknownHostException {
		this.host = InetAddress.getLocalHost().getHostName();
		this.port = UtilsConf.nextPortAvailable();
	}

	public CalculatorProxy(String h, int p) {
		this.host = h;
		this.port = p;
	}

	@Override
	public float add(float x, float y) throws Throwable {
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
		parameters.add(x);
		parameters.add(y);

		// information sent to Requestor
		inv.setClientProxy(this);
		inv.setOperationName(methodName);
		inv.setParameters(parameters);

		// invoke Requestor
		ter = requestor.invoke(inv);

		// @ Result sent back to Client
		return (Float) ter.getResult();
	}

	public float sub(float x, float y) throws Throwable {
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
		parameters.add(x);
		parameters.add(y);

		// information sent to Requestor
		inv.setClientProxy(this);
		inv.setOperationName(methodName);
		inv.setParameters(parameters);

		// invoke Requestor
		ter = requestor.invoke(inv);

		// @ Result sent back to Client
		return (Float) ter.getResult();
	}

	public float mul(float x, float y) throws Throwable {
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
		parameters.add(x);
		parameters.add(y);

		// information sent to Requestor
		inv.setClientProxy(this);
		inv.setOperationName(methodName);
		inv.setParameters(parameters);

		// invoke Requestor
		ter = requestor.invoke(inv);

		// @ Result sent back to Client
		return (Float) ter.getResult();
	}

	public float div(float x, float y) throws Throwable {
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
		parameters.add(x);
		parameters.add(y);

		// information sent to Requestor
		inv.setClientProxy(this);
		inv.setOperationName(methodName);
		inv.setParameters(parameters);

		// invoke Requestor
		ter = requestor.invoke(inv);

		// @ Result sent back to Client
		return (Float) ter.getResult();
	}
}
