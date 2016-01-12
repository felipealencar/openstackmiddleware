package distribution;

import java.util.List;

import distribution.services.jcloud.VmBuilder;

public class VMManagerCallback implements IVMManagerCallback {

	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isFineshed() {
		return fineshed;
	}

	public void setFineshed(boolean fineshed) {
		this.fineshed = fineshed;
	}

	private boolean fineshed;
	private List<VmBuilder> vmBuilders;

	@Override
	public float sendVmSettings(Termination termination) {
		System.out.println("Teste no callback: "+(Float) termination.getResult());
		return 0;
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
