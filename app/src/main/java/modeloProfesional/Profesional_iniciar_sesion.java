package modeloProfesional;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;



public class Profesional_iniciar_sesion {

	public static void main(String[] args) {
		try {
			Socket sc = new Socket("localhost", 8000);

			PrintStream salida = new PrintStream(sc.getOutputStream());
			salida.println("ddd");
			salida.flush();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			JSONObject jdatos=new JSONObject();
			jdatos.put("dni", null);
			jdatos.put("empresa",null);
			jdatos.put("nombre", null);
			jdatos.put("telefono", null);
			jdatos.put("email", "nombre2@test2.com");
			jdatos.put("contrasena","test222");
			jdatos.put("urgente", false);
			jdatos.put("empleo",null);
			
			System.out.println(jdatos.toString());
			salida.println(jdatos.toString());
			salida.flush();
			
			BufferedReader bf = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			String s = bf.readLine();
			System.out.println("�Inicio de sesion correcto?: "+s);
			sc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
