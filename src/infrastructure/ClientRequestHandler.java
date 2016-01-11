package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import distribution.ICalculatorCallback;
import distribution.IVMManagerCallback;

public class ClientRequestHandler {
	private String host;
	private int port;
	private int sentMessageQueueSize;
	private int receiveMessageSize;

	private static Queue<byte[]> queue = new LinkedList<byte[]>();
	private IVMManagerCallback callback;
	private boolean responseArrived;

	private byte[] msg = null;
	
	private Socket clientSocket = null;
	private DataOutputStream outToServer = null;
	private DataInputStream inFromServer = null;
	

	public ClientRequestHandler(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void send(byte [] msg) throws IOException, InterruptedException {

		clientSocket = new Socket(this.host, this.port);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new DataInputStream(clientSocket.getInputStream());

		sentMessageQueueSize = msg.length;
		outToServer.writeInt(sentMessageQueueSize);
		outToServer.write(msg,0,sentMessageQueueSize);
		outToServer.flush();

		return;
	}

	public byte [] receive() throws IOException, InterruptedException,
	ClassNotFoundException {

		byte[] msg = null;

		receiveMessageSize = inFromServer.readInt();
		msg = new byte[receiveMessageSize];
		inFromServer.read(msg, 0, receiveMessageSize);

		clientSocket.close();
		outToServer.close();
		inFromServer.close();

		return msg;
	}

	public byte [] receive(IVMManagerCallback vmManagerCallback, ICalculatorCallback calculatorCallback) throws IOException, InterruptedException,
	ClassNotFoundException {
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					receiveMessageSize = inFromServer.readInt();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg = new byte[receiveMessageSize];
				try {
					inFromServer.read(msg, 0, receiveMessageSize);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(vmManagerCallback != null)
					vmManagerCallback.receiveMessage(msg);
				else if(calculatorCallback != null)
					calculatorCallback.receiveMessage(msg);
				try {
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outToServer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					inFromServer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});  
		t1.start();


		return msg;
	}

	public int getSentMessageSize() {
		return sentMessageQueueSize;
	}

	public void setSentMessageSize(int sentMessageSize) {
		this.sentMessageQueueSize = sentMessageSize;
	}

	public int getReceiveMessageSize() {
		return receiveMessageSize;
	}

	public void setReceiveMessageSize(int receiveMessageSize) {
		this.receiveMessageSize = receiveMessageSize;
	}

	public void enqueue(byte[] msgMarshalled) {
		queue.add(msgMarshalled);
	}

	public void sendMessageQueue() throws IOException {
		clientSocket = new Socket(this.host, this.port);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new DataInputStream(clientSocket.getInputStream());

		byte[] msg = queue.poll();
		sentMessageQueueSize = msg.length;
		outToServer.writeInt(sentMessageQueueSize);
		outToServer.write(msg,0,sentMessageQueueSize);
		outToServer.flush();

		return;

	}

}