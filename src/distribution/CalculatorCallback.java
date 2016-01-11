package distribution;

public class CalculatorCallback implements ICalculatorCallback {

	@Override
	public Termination receiveMessage(byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float printResult(Termination termination) {
		System.out.println("Resultado no callback (após execução da thread): "+(float) termination.getResult());
		return 0;
	}

}
