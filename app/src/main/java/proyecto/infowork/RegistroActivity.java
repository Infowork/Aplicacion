package proyecto.infowork;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class RegistroActivity extends AppCompatActivity {
    EditText nombre,dni,tel,calle,num,ciu,email,contrasena,repite_contrasena;
    ImageView fotoPerfil;
    LinearLayout paso1,paso2;
    Button bot_verificar,bot_siguiente;


    Boolean dni_existe=true;
    Context ctx=RegistroActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //---Datos
        nombre=(EditText)this.findViewById(R.id.edt_nombre);
        dni=(EditText)this.findViewById(R.id.edt_dni);
        tel=(EditText)this.findViewById(R.id.edt_telefono);

        //---Direccion
        calle=(EditText)this.findViewById(R.id.edt_calle);
        num=(EditText)this.findViewById(R.id.edt_num);
        ciu=(EditText)this.findViewById(R.id.edt_ciudad);

        //----Resgistro(email-Contraseña)
        email=(EditText)this.findViewById(R.id.edt_email);
        contrasena=(EditText)this.findViewById(R.id.edt_contrasena);
        repite_contrasena=(EditText)this.findViewById(R.id.edt_rep_contrasena);

        fotoPerfil=(ImageView)this.findViewById(R.id.iv_perfil);
        //---Poner Foto en Funcion PROFESION! Recupera Intent!!!!



        //-----Botones
        bot_verificar=(Button)this.findViewById(R.id.boton_verificar);
        bot_siguiente=(Button)this.findViewById(R.id.boton_siguiente);



        //-----Lauouts---PAso1 y Paso2

        paso1 =(LinearLayout)this.findViewById(R.id.id_paso1);
        paso2 =(LinearLayout)this.findViewById(R.id.id_paso2);

        paso2.setVisibility(View.GONE);



    }
    public boolean datosRellenos(){
        if( isNoEmpty(nombre) && isNoEmpty(dni) && isNoEmpty(tel) && isNoEmpty(contrasena) && isNoEmpty(repite_contrasena) ){
            return true;
        }else{
        return false;
        }
    }
    private boolean isNoEmpty(EditText edt){
        if(edt.getText().toString().trim().length()>0){
            return true;
        }else{
            return false;
        }

    }
    public void verificar(View v){
        //boton para comprobar que los datos son correctos!!!
        if(datosRellenos()) { //comprobar todos los campos
            TareaCom t = new TareaCom();
            t.execute(dni.getText().toString());
            if(dni_existe){
                //break?
            }else{//noExisteDni
                //compruebo que las contraseñas son iguales
                if(contrasena.getText().toString().equals(repite_contrasena.getText().toString())){
                    bot_siguiente.setVisibility(View.VISIBLE);

                }else{ //las contraseñas no coinciden
                    Toast.makeText(this,"las contraseñas no coinciden",Toast.LENGTH_LONG).show();
                }
            }
        }else{
            //pedir que rellene todos los campos
            Toast.makeText(this,"Faltan campos por rellenar",Toast.LENGTH_LONG).show();
        }

        // 1. Ver que todos estan rellenos
        // 2. Comprobar dni --> Tarea Asincrona
        // 3. Comprobar que las contraseñas son iguales

       //............... HAce visible el boton siguiente
    }

    public void siguiente(View V){


        //poner en el tv_nombre el campo que he rellenado en el text_view y la foto perfi imv_perfil?
        paso1.setVisibility(View.GONE);
        paso2.setVisibility(View.VISIBLE);

    }

    public void enviar (View v){
        //transformar Calle/numero/Telf --> LatLong
        //----metodo en AsyncTask que haga esta tarea
    /*    TareaCom3 t3 = new TareaCom();
        t3.execute(dni.getText().toString());
     */

        //condicionar a que se acepten los terminos y condiciones de uso

        //condicionar el boton a que TODOS esten rellenos

        // mostrar un mensaje de registro exitoso

        //condicionar el enviar datos a que el numero de caracteres de la descripcion sean menores de 150


    }

    class TareaCom extends AsyncTask<String,Void,Boolean>{

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dni_existe=aBoolean;
            if(dni_existe){
                //DNI ya registrado
                Toast.makeText(ctx,"Ya existe otro usuario con ese DNI",Toast.LENGTH_LONG).show();

            }else{
                //puede continuar
                Toast.makeText(ctx,"Ya existe otro usuario con ese DNI",Toast.LENGTH_LONG).show();
            }

            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //GestionComunicacion gcom=new GestionComunicacion();
            //gcom.existeDni(params[0]);
            //return gcom.existeDni(params[0]);
            return null;
        }
    }

    class TareaCom2 extends AsyncTask<String,Void,Void>{


        @Override
        protected Void doInBackground(String... params) {
            //GestionComunicacion gcom=new GestionComunicacion();
            //gcom.enviarDatos(params[0]);
            return null;
        }
    }

    class TareaCom3 extends AsyncTask<String,Void,LatLng>{
        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
        }

        @Override
        protected LatLng doInBackground(String... params) {
            //Recibe
            String calle=params[0];
            String num=params[1];
            String ciu=params[2];
            //USa metodo?LocationManager----Se conecta con una web

            //Obtiene
            LatLng coord=null;

            return coord;
        }
    }


}
