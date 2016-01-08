package distribution;

import infrastructure.ClientRequestHandler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Requestor {

	List<VMManagerCallback> callbacks = new ArrayList<VMManagerCallback>();
	
	public Termination invoke(Invocation inv) throws UnknownHostException,
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
		
		// receive reply message
		msgToBeUnmarshalled = clientRequestHandler.receive();

		// unmarshall reply message and callback client
		msgUnmarshalled = (Message) marshaller.unmarshall(msgToBeUnmarshalled);
		for (VMManagerCallback vmManagerCallback : callbacks) {
            vmManagerCallback.vmCreated(true);
        }
		
		// return result to Client Proxy
		termination.setResult(msgUnmarshalled.getBody().getReplyBody()
				.getOperationResult());

		return termination;
	}
	
	public void registerCallback(VMManagerCallback callback){
		callbacks.add(callback);
	}
}
