package proyecto.infowork;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

import javabean.Localizacion;
import modelo.GestionComunicacion;

public class RegistroActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText nombre,dni,tel,email,contrasena,repite_contrasena;
    Button bot_verificar,bot_siguiente;
    ImageView fotoProfesion1;
    Boolean dni_existe=false;
    String empleo;

    //----Paso2------
    EditText empresa,calle,num,ciu;
    TextView sobreescribeNom;
    TextView tv_lat, tv_long;

    //----Paso 3------
    EditText titulo_servicio,descripcion;
    CheckBox cb_urgente,cb_uso;
    TextView tv_nombre2;

    LinearLayout paso1,paso2,paso3;
    Context ctx=RegistroActivity.this;

    Localizacion loc=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Creacion del menu lateral
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.setScrimColor(Color.TRANSPARENT);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //---Datos (PASO 1)
        nombre=(EditText)this.findViewById(R.id.edt_nombre);
        dni=(EditText)this.findViewById(R.id.edt_dni);
        tel=(EditText)this.findViewById(R.id.edt_telefono);
        //----Resgistro(email-Contraseña)
        email=(EditText)this.findViewById(R.id.edt_mail);
        contrasena=(EditText)this.findViewById(R.id.edt_contrasena);
        repite_contrasena=(EditText)this.findViewById(R.id.edt_rep_contrasena);


        //---Poner Foto en Funcion PROFESION! Recupera Intent!!!!
        fotoProfesion1=(ImageView)this.findViewById(R.id.img_paso1);
        Intent recupera=this.getIntent();
        empleo=recupera.getStringExtra("empleo");
        int n=recupera.getIntExtra("num_empleo",0);
        System.out.println(empleo);
        switch (n){
            case 1://electricista
                fotoProfesion1.setImageResource(R.mipmap.electricista);
                //    fotoProfesion2.setImageResource(R.mipmap.electricista);
                break;
            case 2://carpintero
                fotoProfesion1.setImageResource(R.mipmap.obrero);
                break;
            case 3://limpieza
                fotoProfesion1.setImageResource(R.mipmap.limpieza2);
                break;
            case 4://fontanero
                fotoProfesion1.setImageResource(R.mipmap.fontaneria);
                break;
            case 5://pintor
                fotoProfesion1.setImageResource(R.mipmap.pintor);

                break;
            case 6://albañil
                fotoProfesion1.setImageResource(R.mipmap.obras2);
        }

        //-----Botones
        bot_verificar=(Button)this.findViewById(R.id.boton_verificar);
        bot_siguiente=(Button)this.findViewById(R.id.b_siguiente);

        bot_siguiente.setVisibility(View.GONE);

        //-----Layouts---Paso1 , Paso2 y Paso 3
        paso1 =(LinearLayout)this.findViewById(R.id.id_paso1);
        paso2 =(LinearLayout)this.findViewById(R.id.id_paso2);
        paso3 =(LinearLayout)this.findViewById(R.id.id_paso3);

        paso1.setVisibility(View.VISIBLE);
        paso2.setVisibility(View.INVISIBLE);
        paso3.setVisibility(View.INVISIBLE);

        //----------^ Paso 2 ^-----
        sobreescribeNom=(TextView)this.findViewById(R.id.tv_nombre);
        tv_lat=(TextView)this.findViewById(R.id.tv_lat);
        tv_long=(TextView)this.findViewById(R.id.tv_long);
        empresa= (EditText)this.findViewById(R.id.edt_empresa);
        //---Direccion
        calle=(EditText)this.findViewById(R.id.edt_calle);
        num=(EditText)this.findViewById(R.id.edt_num);
        ciu=(EditText)this.findViewById(R.id.edt_ciudad);

        //-----Datos (Paso 3)---Servicio
        titulo_servicio= (EditText)this.findViewById(R.id.edt_servicio);
        descripcion=(EditText)this.findViewById(R.id.edt_descripcion);
        cb_urgente=(CheckBox)this.findViewById(R.id.cb_urgente);
        cb_uso=(CheckBox)this.findViewById(R.id.cb_uso);
        tv_nombre2=(TextView)this.findViewById(R.id.tv_nombre2);
    }

    //----------^ Paso 1 ^----- comprobar DNI

    public boolean datosRellenosPaso1(){
        if( isNoEmpty(nombre) && isNoEmpty(dni) && isNoEmpty(tel)&& isNoEmpty(email) && isNoEmpty(contrasena) && isNoEmpty(repite_contrasena) ){
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
        System.out.println("Boton VERIFICAR pulsado");
        //boton para comprobar que los datos son correctos!!!

        if(datosRellenosPaso1()) { //comprobar todos los campos
            String ss=dni.getText().toString();
            System.out.println("1.Pone en ejecucion TareaCom a partir del DNI: "+ss);

            TareaCom t = new TareaCom();
            t.execute(ss);

            System.out.println(" TareaCom devuelve un boolean: "+dni_existe);

            if(dni_existe){ //true->existe  y false->NO existe

                System.out.println("El numero de DNI ya esta registrado");
                Toast.makeText(this,"numero de DNI ya esta registrado",Toast.LENGTH_LONG).show();

            }else{//noExisteDni-- Toast: DNI ok

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
    }//---FIN----Boton Verificar

    public void siguiente(View V){
        if(datosRellenosPaso1()) {
            // Intent intentAct = new Intent(ctx, MainActivity.class);
            // this.startActivity(intentAct);

            sobreescribeNom.setText(nombre.getText().toString());
            paso1.setVisibility(View.INVISIBLE);
            paso2.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(this,"Faltan campos por rellenar", Toast.LENGTH_LONG).show();
            bot_siguiente.setVisibility(View.INVISIBLE);
        }
        //poner en el tv_nombre el campo que he rellenado en el text_view y la foto perfi imv_perfil?
        //  paso1.setVisibility(View.GONE);
        //  sobreescribeNom.setText(nombre.getText().toString());
        //  paso2.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_alta) {
            Intent intentalta=new Intent(this,ProfesionActivity.class);
            this.startActivity(intentalta);
        } else if (id == R.id.nav_iniciosesion) {
            Intent intentinicio=new Intent(this,LoginActivity.class);
            this.startActivity(intentinicio);
        } else if (id == R.id.nav_busqueda) {
            Intent intentbusqueda=new Intent(this,MainActivity.class);
            this.startActivity(intentbusqueda);
        } else if (id == R.id.nav_presupuesto) {
            Intent intentpresupuesto=new Intent(this,MainActivity.class);
            this.startActivity(intentpresupuesto);
        } else if (id == R.id.nav_ajustes) {
            Intent intentajustes=new Intent(this,MainActivity.class);
            intentajustes.putExtra("email","nombre1@test.com");
            this.startActivity(intentajustes);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class TareaCom extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            GestionComunicacion gcom=new GestionComunicacion();
            // gcom.existeDni(params[0]);
            Boolean res=gcom.existeDni(params[0]);
            System.out.println("2. respuesta doInBackground: "+res);
            return res;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if(!aBoolean){
                //Si No hay otro DNI se queda como falso
                Toast.makeText(RegistroActivity.this," DNI correcto",Toast.LENGTH_LONG).show();
            }else{
                dni_existe=true;
                // si ya existe otro cambio la variable
                Toast.makeText(RegistroActivity.this,"El DNI ya esta registrado",Toast.LENGTH_LONG).show();
                dni.setText("");
            }

            System.out.println("Respuesta onPostExecute"+dni_existe);
        }
    }

    //----------^ Paso 2 ^----- obtener Long y LAt

    public boolean datosRellenosPaso2(){
        if( isNoEmpty(empresa) && isNoEmpty(calle) && isNoEmpty(num) && isNoEmpty(ciu) ){
            return true;
        }else{
            return false;
        }
    }

    public void siguiente2 (View v){
        System.out.println("Datos:"+calle.getText().toString()+num.getText().toString()+ ciu.getText().toString()+dni.getText().toString());

        if(datosRellenosPaso2()) {

            Geocoder geo=new Geocoder(this,new Locale("ES"));
            try{
                String c_calle=calle.getText().toString();
                String n_num=num.getText().toString();
                String c_ciu=ciu.getText().toString();
                List<Address> lista= geo.getFromLocationName(""+c_calle+","+n_num+","+c_ciu+"",1);
                tv_lat.setText(""+lista.get(0).getLatitude());
                tv_long.setText(""+lista.get(0).getLongitude());

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            Toast.makeText(this,"Existen campos sin rellenar",Toast.LENGTH_LONG).show();
        }
        paso2.setVisibility(View.INVISIBLE);
        //  tv_nombre2.setText(nombre.getText().toString());
        paso3.setVisibility(View.VISIBLE);

    }

   /* class TareaCom3 extends AsyncTask<String,Void,Localizacion>{

        private  Localizacion javabeanLoc=null;

        @Override
        protected Localizacion doInBackground(String... params) {
            //Recibe
            String calle=params[0];
            String num=params[1];
            String ciu=params[2];
            String dni=params[3];
            //Crea
            // Localizacion javabeanLoc=null;

            try {
                //establecimiento de conexión
                URL dir = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+num+"+"+calle+","+ciu+"&key=AIzaSyC3t3ae-qK7Vi7q6pYh3zuZypy2ExckNYI");
                URLConnection con = dir.openConnection();
                BufferedReader bf=new BufferedReader(new InputStreamReader(con.getInputStream()));
                //recuperar respuesta
                String cad="",s;
                while((s=bf.readLine())!=null){
                    cad+=s;
                }
                System.out.println("Lectura: "+cad);
                JSONObject jprincipal=new JSONObject(cad);
                //recupera el objeto que se encuentra en la propiedad RestResponse del
                //objeto principal
                System.out.println("jPrincipal: "+jprincipal);
                JSONArray jresponse=jprincipal.getJSONArray("results");
                //recupera el objeto result, que contiene las propiedades básicas
                JSONObject jresp=jresponse.getJSONObject(2);
                System.out.println("Jresp resultado: "+jresp.toString());
                JSONObject jgeometry=jresp.getJSONObject("geometry");
                System.out.println("Jgeometry resultado: "+jresp.toString());
                JSONObject jresult=jgeometry.getJSONObject("location");
                System.out.println(jresult.toString());
                System.out.println(jresult.getString("lat"));
                System.out.println(jresult.getString("lng"));
                javabeanLoc=new Localizacion(dni,Double.parseDouble(jresult.getString("lat")),Double.parseDouble(jresult.getString("lng")));
                System.out.println("JAvabean--->"+javabeanLoc.toString());

                bf.close();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return javabeanLoc;
        }
        @Override
        protected void onPostExecute(Localizacion jloc) {
            System.out.println("he entrado");
            Toast.makeText(ctx,"Se ha creado el javabean con los datos de localizacion"+jloc.getLatitud()+jloc.getLongitud(),Toast.LENGTH_SHORT).show();
            tv_lat.setText(""+jloc.getLatitud());
            tv_long.setText(""+jloc.getLongitud());
        }
    }*/

    //----------^ Paso 3 ^-----

    public boolean datosRellenosPaso3(){
        if(isNoEmpty(titulo_servicio) && isNoEmpty(descripcion) ){
            return true;
        }else{
            return false;
        }
    }

    public String formarJSONArray() {
        JSONArray jarray = null;
        try {
            JSONObject jdatos = new JSONObject();
            jdatos.put("dni", dni.getText().toString());
            jdatos.put("empresa", empresa.getText().toString());
            jdatos.put("nombre", nombre.getText().toString());
            jdatos.put("telefono", tel.getText().toString());
            jdatos.put("email", email.getText().toString());
            jdatos.put("contrasena", contrasena.getText().toString());
            jdatos.put("urgente", cb_urgente.isChecked());
            jdatos.put("empleo", empleo);

            System.out.println("empleo--->"+empleo);
            System.out.println("jdatos--->"+jdatos);

            loc=new Localizacion(dni.getText().toString(),
                    Double.parseDouble(tv_lat.getText().toString()),
                    Double.parseDouble(tv_long.getText().toString()));
            JSONObject jloc = new JSONObject();
            jloc.put("dni", loc.getDni());
            jloc.put("latitud", loc.getLatitud());
            jloc.put("longitud", loc.getLongitud());
            System.out.println("jloc--->"+jloc);

            JSONObject jad = new JSONObject();
            jad.put("dni", dni.getText().toString());
            jad.put("titulo", titulo_servicio.getText().toString());
            jad.put("descripcion",  descripcion.getText().toString());

            System.out.println("jad--->"+jad);

            jarray = new JSONArray();
            jarray.put(jdatos);
            jarray.put(jloc);
            jarray.put(jad);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("jArray--->"+jarray.toString());

        return jarray.toString();
    }//cierroMetodo Formar

    public void enviando (View v) {
        System.out.println("Entra en el boton Enviar");

        if (datosRellenosPaso3()) {
            //1.comprobar todos los campos y que se han aceptado terminos y condiciones de uso
            if(cb_uso.isChecked()) {



                //2.EnviarDatos
                //2.2. Monta la cadena de texto
                String s=formarJSONArray(); //Texto que voy a enviar
                System.out.print("Formar la cadena de texto"+s);

                //2.2. Envia el texto desde una tarea asincrona
                TareaCom2 t2=new TareaCom2();
                t2.execute(s);

                //3.Avisa de que todo_FUnciona
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 4. Redirige a la siguiente actividad ExitoActivity
                Intent intentAct=new Intent(this,ExitoActivity.class);
                intentAct.putExtra("email",email.getText().toString());
                this.startActivity(intentAct);

            }else {
                //falta aceptar condUSO
                Toast.makeText(this, "Por favor, acepta los términos y condiciones de uso", Toast.LENGTH_LONG).show();
            }


        } else {
            //pedir que rellene todos los campos
            Toast.makeText(this, "Faltan campos por rellenar", Toast.LENGTH_LONG).show();
        }

    }//FIn boton ENVIAR

    class TareaCom2 extends AsyncTask<String,Void,Void>{


        @Override
        protected Void doInBackground(String... params) {
            GestionComunicacion gcom2=new GestionComunicacion();
            gcom2.enviarDatos(params[0]);
            return null;
        }
    }

}//----FIN---- Registro

