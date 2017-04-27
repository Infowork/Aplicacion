package modeloProfesional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;


public class Profesional_envia {

	public static void main(String[] args) {
		try {
			Socket sc = new Socket("localhost", 8000);

			PrintStream salida = new PrintStream(sc.getOutputStream());
			salida.println("aaa");
			
			

			JSONObject jdatos=new JSONObject();
				jdatos.put("dni", "123456A");
				jdatos.put("empresa","Test2");
				jdatos.put("nombre", "Nombre2");
				jdatos.put("telefono", 222);
				jdatos.put("email", "nombre2@test2.com");
				jdatos.put("contrasena","test222");
				jdatos.put("urgente", false);
				jdatos.put("empleo","Limpieza");
				
			
			JSONObject jloc=new JSONObject();
				jloc.put("dni", "123456A");
				jloc.put("latitud", 22.423);
				jloc.put("longitud", 22.214);
				
			
			JSONObject jad=new JSONObject();
				jad.put("dni", "123456A");
				jad.put("titulo", "Titulo2");
				jad.put("descripcion", "Prueba 2");
			
				
			
			JSONArray jarray=new JSONArray();

			jarray.put(jdatos);
			jarray.put(jloc);
			jarray.put(jad);
			
			
			System.out.println(jarray.toString());
			salida.println(jarray.toString());
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
