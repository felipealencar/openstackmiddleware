package distribution;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import distribution.services.jcloud.VMManagerImpl;
import distribution.services.jcloud.VmBuilder;
import infrastructure.ServerRequestHandler;

public class VMManagerInvoker {

	public void invoke(ClientProxy clientProxy) throws IOException, Throwable {
		ServerRequestHandler srh = new ServerRequestHandler(
				clientProxy.getPort());
		byte[] msgToBeUnmarshalled = null;
		byte[] msgMarshalled = null;
		Message msgUnmarshalled = new Message();
		Marshaller mrsh = new Marshaller();
		Termination ter = new Termination();

		// create remote object
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:utilsconf/applicationContext.xml");
		VMManagerImpl rObj = context.getBean(VMManagerImpl.class);

		// inversion loop
		while (true) {

			// @ Receive Message
			msgToBeUnmarshalled = srh.receive();

			// @ Unmarshall received message
			msgUnmarshalled = mrsh.unmarshall(msgToBeUnmarshalled);

			switch (msgUnmarshalled.getBody().getRequestHeader().getOperation()) {
			
				case "add":
					// @ Invokes the remote object
					VmBuilder _add_p1 = (VmBuilder) msgUnmarshalled.getBody()
							.getRequestBody().getParameters().get(0);
					ter.setResult(rObj.add(_add_p1));
	
					Message _add_msgToBeMarshalled = new Message(new MessageHeader(
							"protocolo", 0, false, 0, 0), new MessageBody(null,
							null, new ReplyHeader("", 0, 0), new ReplyBody(
									ter.getResult())));
	
					// @ Marshall the response
					msgMarshalled = mrsh.marshall(_add_msgToBeMarshalled);
	
					// @ Send response
					srh.send(msgMarshalled);
					break;
			}
		}
	}
}
