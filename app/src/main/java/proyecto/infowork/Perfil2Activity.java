package proyecto.infowork;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import javabean.Adicionales;
import javabean.Datos;
import javabean.Localizacion;
import javabean.Persona;
import javabean.Puntuacion;
import javabean.Valoracion;

public class Perfil2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nombre;
    private TextView dni;
    private TextView emailp;
    private TextView telefono;
    private TextView descripcion;
    private TextView titulo;

    private RatingBar rbar;
    private ListView reviews;
    private SupportMapFragment smf;
    private Persona p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Perfil2Activity.this,ValoracionActivity.class);
                intent.putExtra("dni",dni.getText().toString());
                intent.putExtra("nombre",nombre.getText().toString());
                intent.putExtra("empresa","empresa??");
                Perfil2Activity.this.startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.setScrimColor(Color.TRANSPARENT);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Referencia a objetos
        nombre=(TextView)this.findViewById(R.id.tv_nombrePerfil);
        dni=(TextView)this.findViewById(R.id.tv_DNIPerfil);
        emailp=(TextView)this.findViewById(R.id.tv_EmailPerfil);
        telefono=(TextView)this.findViewById(R.id.tv_telefonoPerfil);
        titulo=(TextView)this.findViewById(R.id.tv_tituloPerfil);
        descripcion=(TextView)this.findViewById(R.id.tv_descripcionPerfil);

        rbar=(RatingBar)this.findViewById(R.id.rbar_puntuacion);
        reviews=(ListView)this.findViewById(R.id.lista_reviews);
        //Referencia al mapa
        FragmentManager fm = this.getSupportFragmentManager();
        smf = (SupportMapFragment) fm.findFragmentById(R.id.mapaPerfil);


        //Dependiendo de la información recogida en el intent se ejecuta la llamada al servidor o no.
        Intent intent=this.getIntent();
        if(intent.getSerializableExtra("persona")==null){
            String email=intent.getStringExtra("email");
            ObtenerPerfil perfil=new ObtenerPerfil();
            perfil.execute(email);
        }else{
            p=(Persona)intent.getSerializableExtra("persona");
            nombre.setText("Nombre: "+p.getDatos().getNombre());
            dni.setText("DNI: "+p.getDatos().getDni());
            emailp.setText("Email: "+p.getDatos().getEmail());
            telefono.setText("Telefono: "+p.getDatos().getTelefono());
            rbar.setIsIndicator(true);
            rbar.setNumStars(5);

            rbar.setRating(Float.parseFloat(String.valueOf(p.getPunt().getPuntuacion())));
            System.out.println("Puntuacion!!! "+Float.parseFloat(String.valueOf(p.getPunt().getPuntuacion())));
            ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,p.getPunt().getReview());
            reviews.setAdapter(adapter);
            titulo.setText(p.getAd().getTitulo());
            descripcion.setText(p.getAd().getDescripcion());
            smf.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    //Definir el tipo de mapa
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    //Posicionar el mapa en la localización de la persona
                    LatLng pos=new LatLng(p.getLoc().getLatitud(),p.getLoc().getLongitud());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));

                    //Marker
                    MarkerOptions mk=new MarkerOptions();
                    mk.position(pos);
                    googleMap.addMarker(mk);
                }
            });
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_alta) {
            Intent intentalta=new Intent(this,ProfesionActivity.class);
            this.startActivity(intentalta);
        } else if (id == R.id.nav_iniciosesion) {
            Intent intentinicio=new Intent(this,MainActivity.class);
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

    class ObtenerPerfil extends AsyncTask<String,Void,Persona>{
        @Override
        protected void onPostExecute(Persona p) {

            final Persona pers=p;
            nombre.setText("Nombre: "+p.getDatos().getNombre());
            dni.setText("DNI: "+p.getDatos().getDni());
            emailp.setText("Email: "+p.getDatos().getEmail());
            telefono.setText("Telefono: "+p.getDatos().getTelefono());
            rbar.setIsIndicator(true);
            rbar.setNumStars(5);
            if(p.getPunt()==null){
                rbar.setRating(0);
            }else{
                rbar.setRating(Float.parseFloat(String.valueOf(p.getPunt().getPuntuacion())));
                System.out.println("Puntuacion!!! "+Float.parseFloat(String.valueOf(p.getPunt().getPuntuacion())));
                ArrayAdapter<String> adapter=new ArrayAdapter<>(Perfil2Activity.this,android.R.layout.simple_list_item_1,p.getPunt().getReview());
                reviews.setAdapter(adapter);
            }

            titulo.setText(p.getAd().getTitulo());
            descripcion.setText(p.getAd().getDescripcion());
            smf.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    //Definir el tipo de mapa
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    //Posicionar el mapa en la localización de la persona
                    LatLng pos=new LatLng(pers.getLoc().getLatitud(),pers.getLoc().getLongitud());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));

                    //Marker
                    MarkerOptions mk=new MarkerOptions();
                    mk.position(pos);
                    googleMap.addMarker(mk);
                }
            });
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

}