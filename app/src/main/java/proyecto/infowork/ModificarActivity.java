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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javabean.Adicionales;
import javabean.Datos;
import javabean.Localizacion;
import javabean.Persona;
import javabean.Puntuacion;
import modelo.GestionComunicacion;

public class ModificarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView nombre, dni,email;
    ImageView profesion;

    EditText tel,empresa,calle,num,ciu,titulo_servicio,descripcion;
    CheckBox cb_urgente;
    Button b_modificar;

    Context ctx=ModificarActivity.this;
    Localizacion loc=null;

    TextView tv_lat,tv_lng;

    String contrasena_M,empleo_M;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        //Recuperar intent
        Intent recupera=this.getIntent();
        String mail=recupera.getStringExtra("email");
        email=(TextView)this.findViewById(R.id.tvm_email);
        email.setText(mail);

        ObtenerPerfil perfil=new ObtenerPerfil();
        perfil.execute(mail);

        nombre=(TextView)this.findViewById(R.id.tvm_nombre);
        dni=(TextView)this.findViewById(R.id.tvm_dni);

        tel=(EditText) this.findViewById(R.id.edtm_telf);
        empresa=(EditText) this.findViewById(R.id.edtm_empresa);
        calle=(EditText) this.findViewById(R.id.edtm_calle);
        num=(EditText) this.findViewById(R.id.edtm_num);
        ciu =(EditText) this.findViewById(R.id.edtm_ciudad);
        titulo_servicio=(EditText) this.findViewById(R.id.edtm_titulo);
        descripcion=(EditText) this.findViewById(R.id.edtm_descripcion);
        profesion=(ImageView)this.findViewById(R.id.imageView2);
        cb_urgente=(CheckBox)this.findViewById(R.id.cbm_urge);
        b_modificar=(Button)this.findViewById(R.id.b_modificar);

        b_modificar.setVisibility(View.INVISIBLE);
        tv_lat=(TextView)this.findViewById(R.id.tvm_lat);
        tv_lng=(TextView)this.findViewById(R.id.tvm_lng);

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
    }

    public void m_verificar (View v){

        if(datosRellenosPaso4()) {
            Geocoder geo=new Geocoder(this,new Locale("ES"));
            try{
                String c_calle=calle.getText().toString();
                String n_num=num.getText().toString();
                String c_ciu=ciu.getText().toString();
                List<Address> lista= geo.getFromLocationName(""+c_calle+","+n_num+","+c_ciu+"",1);
                tv_lat.setText(""+lista.get(0).getLatitude());
                tv_lng.setText(""+lista.get(0).getLongitude());

            }catch (Exception ex){
                ex.printStackTrace();
            }
            b_modificar.setVisibility(View.VISIBLE);

        }else{
            Toast.makeText(this,"Existen campos sin rellenar",Toast.LENGTH_LONG).show();
        }


    }

    /*class TareaCom3 extends AsyncTask<String,Void,Localizacion>{

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
            tv_lng.setText(""+jloc.getLongitud());
        }
    }*/
    private boolean isNoEmpty(EditText edt){
        if(edt.getText().toString().trim().length()>0){
            return true;
        }else{
            return false;
        }

    }
    public boolean datosRellenosPaso4(){
        if( isNoEmpty(tel) &&isNoEmpty(empresa) && isNoEmpty(calle) && isNoEmpty(num) && isNoEmpty(ciu) && isNoEmpty(titulo_servicio) && isNoEmpty(descripcion) ){
            return true;
        }else{
            return false;
        }
    }

    public void modificar(View v){
        //componer JsonModific con los datos cambiados
        String s=formarJSONArray(); //Texto que voy a enviar
        System.out.print("Formar la cadena de texto"+s);

        //2.2. Envia el texto desde una tarea asincrona
        TareaCom5 t5=new TareaCom5();
        t5.execute(s);

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
        //Enviar


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
            jdatos.put("contrasena", contrasena_M);
            jdatos.put("urgente", cb_urgente.isChecked());
            jdatos.put("empleo", empleo_M);

            System.out.println("empleo--->"+empleo_M);
            System.out.println("jdatos--->"+jdatos);

            loc=new Localizacion(dni.getText().toString(),
                    Double.parseDouble(tv_lat.getText().toString()),
                    Double.parseDouble(tv_lng.getText().toString()));
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

    class TareaCom5 extends AsyncTask<String,Void,Void>{


        @Override
        protected Void doInBackground(String... params) {
            GestionComunicacion gcom2=new GestionComunicacion();
            gcom2.modificarDatos(params[0]);
            return null;
        }
    }

    class ObtenerPerfil extends AsyncTask<String,Void, Persona>{
        @Override
        protected void onPostExecute(Persona p) {

            final Persona pers=p;
            nombre.setText("Nombre: "+p.getDatos().getNombre());
            dni.setText("DNI: "+p.getDatos().getDni());
            tel.setText("Telefono: "+p.getDatos().getTelefono());

            titulo_servicio.setText(p.getAd().getTitulo());
            descripcion.setText(p.getAd().getDescripcion());

            contrasena_M=p.getDatos().getContrasena();
            empleo_M=p.getDatos().getEmpleo();
            String n=p.getDatos().getEmpleo();

            switch (n){
                case "Electricista"://electricista
                    profesion.setImageResource(R.mipmap.electricista);
                    //    fotoProfesion2.setImageResource(R.mipmap.electricista);
                    break;
                case "Carpintero"://carpintero - canguro
                    profesion.setImageResource(R.mipmap.obrero);
                    break;
                case "Limpieza"://limpieza
                    profesion.setImageResource(R.mipmap.limpieza2);
                    break;
                case "Fontanero"://fontanero
                    profesion.setImageResource(R.mipmap.fontaneria);
                    break;
                case "Pintor"://pintor
                    profesion.setImageResource(R.mipmap.pintor);

                    break;
                case "Albannil"://albañil
                    profesion.setImageResource(R.mipmap.obras2);
            }
            if(p.getDatos().isUrgente()){
                cb_urgente.setChecked(true);
            }

        }

        @Override
        protected Persona doInBackground(String... params) {

            Persona p=null;
            try{

                Socket sc = new Socket("192.168.0.172", 9000);

                PrintStream salida = new PrintStream(sc.getOutputStream());
                salida.println("ccc");
                salida.flush();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JSONObject jdatos=new JSONObject();
                jdatos.put("dni", "null");
                jdatos.put("empresa","null");
                jdatos.put("nombre", "null");
                jdatos.put("telefono", 0);
                jdatos.put("email", params[0]);
                jdatos.put("contrasena","null");
                jdatos.put("urgente", "null");
                jdatos.put("empleo","null");

                salida.println(jdatos.toString());

                BufferedReader bf = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                String s = bf.readLine();
                System.out.println(s);
                sc.close();

                JSONArray jarray=new JSONArray(s);
                JSONObject jobdatos=(JSONObject) jarray.get(0);
                JSONObject joblocalizacion=(JSONObject) jarray.get(1);
                JSONObject jobadicionales=(JSONObject) jarray.get(2);
                JSONObject jobpuntuacion=(JSONObject) jarray.get(3);

                Datos datos= new Datos(jobdatos.get("dni").toString(),
                        jobdatos.get("empresa").toString(),
                        jobdatos.get("nombre").toString(),
                        Integer.parseInt(jobdatos.get("telefono").toString()),
                        jobdatos.get("email").toString(),
                        jobdatos.get("contrasena").toString(),
                        Boolean.parseBoolean(jobdatos.get("urgente").toString()),
                        null);

                Localizacion loc=new Localizacion( joblocalizacion.get("dni").toString(),
                        Double.parseDouble(joblocalizacion.get("latitud").toString()),
                        Double.parseDouble(joblocalizacion.get("longitud").toString()));
                Adicionales ad=new Adicionales(jobadicionales.get("dni").toString(),
                        jobadicionales.get("titulo").toString(),
                        jobadicionales.get("descripcion").toString());
                ArrayList<String> reviews=new ArrayList<>();
                for(int i=0;i<jobpuntuacion.getInt("total");i++){
                    reviews.add(jobpuntuacion.getString("review"+i));
                }
                Puntuacion punt;
                if(jobpuntuacion.get("puntuacion").toString().equals(null)){
                    punt=null;
                }else {
                    punt = new Puntuacion(jobpuntuacion.get("dni").toString(), reviews, Double.parseDouble(jobpuntuacion.get("puntuacion").toString()));
                }

                p=new Persona(datos,loc,ad,punt);

            } catch (Exception ex) {
                ex.printStackTrace();
            }return p;
        }
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


}
