package distribution;

import distribution.services.jcloud.VmBuilder;

public class VMManagerCallback implements IVMManagerCallback {
	
	public boolean status = false;
	
	@Override
	public boolean vmCreated(boolean status) {
		this.status = status;
		return this.status;
	}
}
