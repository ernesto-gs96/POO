import java.io.*;
import java.net.*;

class Cliente 
{
	static final String HOST = "localhost";
	static final int PUERTO=5000;
	static String preguntas []= {"¿como te llamas?","¿cual es tu color favorito?", "¿en donde vives?", "¿cual es tu fruta favorita?", "¿cual es tu edad?", "¿donde estudias?","¿en donde vives?","¿Tienes hermanos?","¿tienes celular?","¿donde trabajas?"};
	private Socket cliente;  
	private ObjectInputStream ENTRADA;
	private ObjectOutputStream SALIDA;
	
	
	public Cliente(String msg) {

		int j;
		Object ci=null;

		try{
			cliente = new Socket( HOST , PUERTO );//SE CREA EL SOCKET CLIENTE
			
			try {
				
				//SE CREAN LOS FLUJOS DE OBJETOS
				SALIDA = getOOSNet(cliente.getOutputStream());
				ENTRADA = getOISNet(cliente.getInputStream());
			} 
			catch (IOException e) {

				e.printStackTrace();
				System.out.println("Cliente Error al crear los flujos de objeto "+e);
			}
			try {
				SALIDA.writeObject(msg);
			}
			catch (IOException ex) {
				
				ex.printStackTrace();
			}  
			
			j=0;

			try {
				ci = ENTRADA.readObject();	
			} 
			catch (IOException e) {

				System.out.println("IO ex"+e);
				j = 1;
			}
			catch (ClassNotFoundException ex) {

				System.out.println("Class no found"+ex);
				j = 1;
			}

			if (j==0) 
			{
				if(ci instanceof String){
					System.out.println(msg+": ");
					System.out.println((String)ci+"\n");
				} 
					
			}
			//cliente.close();
		} 
		catch( Exception e ) 
		{
			System.out.println( e.getMessage() );
		}
	}

	public static void main(String[] arg ){

		int i;
		String msg;

		for( i = 0; i < preguntas.length; i++) {
			msg=preguntas[i];
			new Cliente(msg);
		} 
		
	}
	
	ObjectOutputStream getOOSNet(OutputStream os) throws IOException
	{
		return new ObjectOutputStream(os);
	}
	
	ObjectInputStream getOISNet(InputStream is) throws IOException
	{
		return new ObjectInputStream(is);
	}
}
