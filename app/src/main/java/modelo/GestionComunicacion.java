package modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by casa on 02/05/2017.
 */

public class GestionComunicacion {

    public boolean existeDni(String dni){
        String s="";

        try {
            Socket sc = new Socket("192.168.0.172", 8000);

            PrintStream salida = new PrintStream(sc.getOutputStream());
            salida.println("eee");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("gcom.Envia el dni: "+dni);
            salida.println(dni);

            BufferedReader bf = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            s=bf.readLine();

            System.out.println("gcom. Lee la respuesta del servidor: "+s);

            sc.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return Boolean.parseBoolean(s);

    }
    public void modificarDatos(String jsonArray) {
        try {
            Socket sc = new Socket("192.168.0.172", 8000);

            PrintStream salida2 = new PrintStream(sc.getOutputStream());
            salida2.println("bbb");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(jsonArray);
            salida2.println(jsonArray);
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void enviarDatos(String jsonArray) {
        try {
            Socket sc = new Socket("192.168.0.172", 8000);

            PrintStream salida2 = new PrintStream(sc.getOutputStream());
            salida2.println("aaa");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(jsonArray);
            salida2.println(jsonArray);
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public String devuelveDatosMail(String email){
        String s="";

/*
        try {
            Socket sc = new Socket("192.168.1.131", _cliente_);

               PrintStream salida = new PrintStream(sc.getOutputStream());
            salida.println("___________");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("gcom.Envia el dni: "+ email);
            salida.println( email);

            BufferedReader bf = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            s=bf.readLine();

            System.out.println("gcom. Lee la respuesta del servidor: "+s);

            sc.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
*/
        return s;

    }
}
