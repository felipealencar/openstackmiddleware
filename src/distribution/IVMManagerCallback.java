package distribution;

import distribution.services.jcloud.VmBuilder;

public interface IVMManagerCallback {
	Termination receiveMessage(byte[] msg);
	float sendVmSettings(Termination termination);
}
