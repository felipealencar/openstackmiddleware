package distribution;

import java.util.List;

import distribution.services.jcloud.VmBuilder;

public class VMManagerCallback implements IVMManagerCallback {

	private List<VmBuilder> vmBuilders;

	@Override
	public float sendVmSettings(Termination termination) {
		System.out.println("Teste no callback: "+(Float) termination.getResult());
		return (float) 1.0;
	}

	@Override
	public Termination receiveMessage(byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setVmBuilders(List<VmBuilder> vmBuilders) {
		this.vmBuilders = vmBuilders;
		
	}

}
