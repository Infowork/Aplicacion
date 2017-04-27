package modeloProfesional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Profesional_recibe {

	public static void main(String[] args) {
		try {
			Socket sc = new Socket("localhost", 8000);

			PrintStream salida = new PrintStream(sc.getOutputStream());
			salida.println("ccc");
			salida.flush();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//PrintStream salida2 = new PrintStream(sc.getOutputStream());
			salida.println("25698321A");
			salida.flush();
			System.out.println("DNI enviado");
			/*PrintStream salida3 = new PrintStream(sc.getOutputStream());
			salida3.println("25698321A");*/

			BufferedReader bf = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			String s = bf.readLine();
			System.out.println(s);
			sc.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}
