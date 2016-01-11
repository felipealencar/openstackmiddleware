package distribution;

import infrastructure.ClientRequestHandler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Requestor implements IVMManagerCallback, ICalculatorCallback {
	
	private List<IVMManagerCallback> callbacks = new ArrayList<IVMManagerCallback>();
	private IVMManagerCallback vmManagerCallback = null;
	private ICalculatorCallback calculatorCallback = null;
	
	public Termination invoke(Invocation inv, IVMManagerCallback vmManagerCallback, ICalculatorCallback calculatorCallback) throws UnknownHostException,
			IOException, Throwable {
		ClientRequestHandler clientRequestHandler = new ClientRequestHandler(inv
				.getClientProxy().getHost(), inv.getClientProxy().getPort());
		
		Marshaller marshaller = new Marshaller();
		Termination termination = new Termination();
		byte [] msgMarshalled = new byte [1000];
		byte [] msgToBeUnmarshalled = new byte [1000];
		Message msgUnmarshalled = new Message(); // TODO actual marshalling
		
		// map Invocation into a Message
		RequestHeader requestHeader = new RequestHeader("", 0, true, 0,
				inv.getOperationName());
		RequestBody requestBody = new RequestBody(inv.getParameters());
		MessageHeader messageHeader = new MessageHeader("MIOP", 0, false, 0, 0);
		MessageBody messageBody = new MessageBody(requestHeader, requestBody,
				null, null);
		Message msgToBeMarshalled = new Message(messageHeader, messageBody);

		// marshall request message
		msgMarshalled = marshaller.marshall(msgToBeMarshalled);

		// send marshalled message
		clientRequestHandler.send(msgMarshalled);
		
		this.vmManagerCallback = vmManagerCallback;
		this.calculatorCallback = calculatorCallback;
		// receive reply message
		if(this.vmManagerCallback != null)
			msgToBeUnmarshalled = clientRequestHandler.receive(this, null);
		else if(this.calculatorCallback != null)
			msgToBeUnmarshalled = clientRequestHandler.receive(null, this);
		else if(this.calculatorCallback == null && this.vmManagerCallback == null){
			msgToBeUnmarshalled = clientRequestHandler.receive();
			// unmarshall reply message and callback client
			msgUnmarshalled = (Message) marshaller.unmarshall(msgToBeUnmarshalled);
			
			// return result to Client Proxy
			termination.setResult(msgUnmarshalled.getBody().getReplyBody()
					.getOperationResult());

			return termination;
		}
		return null;
	}
	
	public void registerCallback(IVMManagerCallback callback){
		callbacks.add(callback);
	}

	@Override
	public Termination receiveMessage(byte[] msg) {
		Marshaller marshaller = new Marshaller();
		Termination termination = new Termination();
		byte [] msgMarshalled = new byte [1000];
		byte [] msgToBeUnmarshalled = new byte [1000];
		Message msgUnmarshalled = new Message(); // TODO actual marshalling
		System.out.println("Teste no Requestor (após execução da thread). ");
		msgToBeUnmarshalled = msg;
		msgUnmarshalled = (Message) marshaller.unmarshall(msgToBeUnmarshalled);
		
		// return result to Client Proxy
		termination.setResult(msgUnmarshalled.getBody().getReplyBody()
				.getOperationResult());
		if(this.vmManagerCallback != null)
			this.vmManagerCallback.sendVmSettings(termination);
		else if(this.calculatorCallback != null)
			this.calculatorCallback.printResult(termination);
		return termination;

	}

	@Override
	public float printResult(Termination termination) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float sendVmSettings(Termination termination) {
		// TODO Auto-generated method stub
		return 0;
	}
}
