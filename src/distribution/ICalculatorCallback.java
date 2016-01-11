package distribution;

import distribution.services.jcloud.VmBuilder;

public interface ICalculatorCallback {
	Termination receiveMessage(byte[] msg);
	float printResult(Termination termination);
}
