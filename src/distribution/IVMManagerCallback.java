package distribution;

import distribution.services.jcloud.VmBuilder;

public interface IVMManagerCallback {
	boolean vmCreated(boolean status);
}
