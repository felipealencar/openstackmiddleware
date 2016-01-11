package distribution;

import java.io.IOException;
import java.net.UnknownHostException;

import commonservices.naming.NamingProxy;

public class CalculatorClient {

	public static CalculatorCallback callback = new CalculatorCallback() ;
	
	public static void main(String[] args) throws UnknownHostException,
			IOException, Throwable {
		
		// create an instance of Naming Service
		NamingProxy namingService = new NamingProxy("localhost", 1313);

		// check registered services
		System.out.println(namingService.list());
		
		// look for Calculator in Naming service
		CalculatorProxy calculatorProxy = (CalculatorProxy) namingService
				.lookup("Calculator");

		// invoke calculator
		//calculatorProxy.add(1, 3);
		
		System.out.println("Resultado da operação: "+calculatorProxy.add(1, 3, callback));
		System.out.println("Teste no cliente (chamada após a thread (ainda em execução)).");
	}

}