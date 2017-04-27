package modeloCliente;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class Cliente_busca_uno {

	public static void main(String[] args) {
		try {
			Socket sc = new Socket("localhost", 9000);

			PrintStream salida = new PrintStream(sc.getOutputStream());
			salida.println("ccc");
			salida.flush();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject jdatos=new JSONObject();
			jdatos.put("dni", "null");
			jdatos.put("empresa","null");
			jdatos.put("nombre", "null");
			jdatos.put("telefono", 222);
			jdatos.put("email", "null");
			jdatos.put("contrasena","null");
			jdatos.put("urgente", "null");
			jdatos.put("empleo","null");
			
			salida.println(jdatos.toString());

			BufferedReader bf = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			String s = bf.readLine();
			System.out.println(s);
			sc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
