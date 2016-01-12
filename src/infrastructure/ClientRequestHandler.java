package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import distribution.ICalculatorCallback;
import distribution.IVMManagerCallback;

public class ClientRequestHandler {
	private String host;
	private int port;
	private int sentMessageSize;
	private int receiveMessageSize;

	private static Queue<byte[]> queue = new LinkedList<byte[]>();
	private IVMManagerCallback callback;
	AtomicInteger cont = new AtomicInteger(1);
	private boolean responseArrived;

	private static byte[] msg = null;

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

		sentMessageSize = msg.length;
		
		//Flush adicionado antes também do envio de dados devido ao multithread.
		
		outToServer.flush();
		outToServer.writeInt(sentMessageSize);
		outToServer.flush();
		outToServer.write(msg,0,sentMessageSize);
		outToServer.flush();

		return;
	}

	public byte [] receive() throws IOException, InterruptedException,
	ClassNotFoundException {

		byte[] msg = null;
		outToServer.flush();

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
//					outToServer.flush();
					receiveMessageSize = inFromServer.readInt();
					msg = new byte[receiveMessageSize];
					inFromServer.read(msg, 0, receiveMessageSize);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(vmManagerCallback != null)
					vmManagerCallback.receiveMessage(msg);
				else if(calculatorCallback != null)
					calculatorCallback.receiveMessage(msg);

				cont.incrementAndGet();
			}
		});
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				
			}
		});

		if(cont.get()==1){
			t1.start();
			t2.join();
			t2.start();
		}

		if(cont.get()>1){
			t1.join();
			t1.start();
			t2.join();
			t2.start();
		}

		return msg;
	}

	public int getSentMessageSize() {
		return sentMessageSize;
	}

	public void setSentMessageSize(int sentMessageSize) {
		this.sentMessageSize = sentMessageSize;
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
		sentMessageSize = msg.length;
		outToServer.writeInt(sentMessageSize);
		outToServer.write(msg,0,sentMessageSize);
		outToServer.flush();

		return;

	}

}