package proyecto.infowork;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import gestion.GestionPermisosIniciales;
import javabean.Adicionales;
import javabean.Datos;
import javabean.Localizacion;
import javabean.Persona;
import javabean.Puntuacion;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,LocationListener,OnMapReadyCallback {


    Spinner spEmpleo;
    Spinner spRadio;
    GoogleMap gm;
    String empleo;
    String radioTexto;
    int radio;
    SupportMapFragment smf;
    CardView tarjeta;
    ArrayList<MarkerOptions> markers;
    LocationManager lm;
    Location usuario;
    LatLng pos=new LatLng(0,0);
    TextView nombre_tarjeta;
    TextView empresa_tarjeta;
    ArrayList<Persona> personas;
    Persona prof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new GestionPermisosIniciales(this,this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personas=new ArrayList<>();
        markers = new ArrayList<>();
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }

        //Referencias de la tarjeta inferior
        tarjeta=(CardView)this.findViewById(R.id.bottomLayout);
        tarjeta.setVisibility(View.GONE);
        nombre_tarjeta=(TextView)this.findViewById(R.id.nombre_prof);
        empresa_tarjeta=(TextView)this.findViewById(R.id.nombre_emp);

        //Referencias a los Spinner
        spEmpleo = (Spinner) this.findViewById(R.id.spn_empleo);
        spRadio = (Spinner) this.findViewById(R.id.spn_radio);

        //Adapters para ambos spinners de búsqueda.
        ArrayAdapter adapterEmp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new String[]{"Limpieza", "Carpintero", "Fontanero", "Electricista", "Pintor", "Albañilería"});
        spEmpleo.setAdapter(adapterEmp);
        ArrayAdapter adapterRadio = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new String[]{"1 km", "2 km", "5 km"});
        spRadio.setAdapter(adapterRadio);

        //Listeners de los spinners
        spEmpleo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                empleo = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spRadio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                radioTexto = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Referencias al mapa
        FragmentManager fm = this.getSupportFragmentManager();
        smf = (SupportMapFragment) fm.findFragmentById(R.id.id_mapa);
        smf.getMapAsync(this);

        //Creación del menú lateral
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setScrimColor(Color.TRANSPARENT);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void buscar(View v){
        switch(radioTexto){
            case "1 km":
                radio=1000;
                break;
            case "2 km":
                radio=2000;
                break;
            case "5 km":
                radio=5000;
                break;
        }
        personas.clear();
        markers.clear();
        Comunicaciones coms=new Comunicaciones();
        coms.execute(empleo);

    }

    public void llamar(View v){
        if(prof==null){

        }else{
            Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel://"+prof.getDatos().getTelefono()));
            this.startActivity(intent);
        }
    }

    public void mandarSMS(View v){
        if(prof==null){

        }else{
            Intent intent=new Intent(Intent.ACTION_SENDTO, Uri.parse("tel://"+prof.getDatos().getTelefono()));
            this.startActivity(intent);
        }

    }

    public void perfil(View v){
        if(prof!=null){
            Intent intent=new Intent(this, Perfil2Activity.class);
            intent.putExtra("persona",prof);
            this.startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gm = googleMap;

        //Definir el tipo de mapa
        gm.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Habilitar controles
        gm.getUiSettings().setZoomControlsEnabled(true);
        try{
            gm.setMyLocationEnabled(true);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }

        //Posicionar el mapa en la localización de la persona
        gm.clear();
        if (pos.latitude == 0 && pos.longitude == 0) {
            try {
                Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                gm.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l.getLatitude(), l.getLongitude()), 15));
                pos=new LatLng(l.getLatitude(),l.getLongitude());

                System.out.println("Localizacion antigua!!");
            }catch(SecurityException ex){
                ex.printStackTrace();
            }
        }else{
            gm.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,15));
            System.out.println("Localizacion nueva!!");
        }

        TimerTask tarea_repetir=new TimerTask() {
            @Override
            public void run() {
                System.out.println("Agregando marcadores...");
                System.out.println("Numero de marcadores disponibles: "+markers.size());
                if(personas.size()>0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gm.clear();
                        }
                    });
                    int total=0;
                    for(int i=0;i<markers.size();i++){
                        final int n=i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gm.addMarker(markers.get(n));
                            }
                        });
                        total++;
                    }

                    System.out.println("Markers añadidos: "+total);
                }
            }
        };

        Timer tm=new Timer();
        tm.schedule(tarea_repetir,0,5000);

        gm.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                tarjeta.setVisibility(View.GONE);
            }
        });

        gm.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                tarjeta.setVisibility(View.VISIBLE);
                String nombre=marker.getTitle();
                for(int i=0;i<personas.size();i++){
                    if(nombre.equals(personas.get(i).getDatos().getNombre())){
                        nombre_tarjeta.setText(personas.get(i).getDatos().getNombre());
                        empresa_tarjeta.setText(personas.get(i).getDatos().getEmpresa());
                        prof=new Persona(personas.get(i).getDatos(),personas.get(i).getLoc(),personas.get(i).getAd(),personas.get(i).getPunt());
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        pos=new LatLng(location.getLatitude(),location.getLongitude());
        System.out.println("" + location.getLatitude() + " " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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

    class Comunicaciones extends AsyncTask<String,Void,ArrayList<Persona>>{
        @Override
        protected void onPostExecute(final ArrayList<Persona> personas) {

            //Para cada profesional recibido se comprueba si cumple el requisito de distancia solicitado.
            //Si lo cumple se crea un marker para el mapa y se van guardando en un arrayList.
            for(int i=0;i<personas.size();i++){
                Location profesional=new Location("PROFESIONAL");
                profesional.setLatitude(personas.get(i).getLoc().getLatitud());
                profesional.setLongitude(personas.get(i).getLoc().getLongitud());
                usuario=new Location("CLIENTE");
                usuario.setLatitude(pos.latitude);
                usuario.setLongitude(pos.longitude);

                float distancia= usuario.distanceTo(profesional);
                if(distancia<radio){
                    MarkerOptions mk=new MarkerOptions();
                    LatLng pos=new LatLng(personas.get(i).getLoc().getLatitud(),personas.get(i).getLoc().getLongitud());
                    mk.position(pos);
                    mk.title(personas.get(i).getDatos().getNombre());
                    mk.snippet(personas.get(i).getDatos().getEmpresa());
                    markers.add(mk);
                }
            }
            System.out.println("Total de markers: "+markers.size());

        }

        @Override
        protected ArrayList<Persona> doInBackground(String... params) {
            String host = "192.168.0.172";
            int puerto = 9000;
            markers.clear();
            personas.clear();

            try {
                Socket sc = new Socket(host, puerto);
                PrintStream salida = new PrintStream(sc.getOutputStream());
                //Envía código de aplicacion y empleo requerido
                salida.println("aaa");
                Thread.sleep(500);
                System.out.println(params[0]);
                salida.println(params[0]);
                System.out.println("!!!!!!Envio correcto¡¡¡¡¡¡");
                //Recoge un JSONArray compuesto por los multiples JSONArrays de ese tipo de empleo
                BufferedReader bf = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                String json=bf.readLine();
                System.out.println(json);

                JSONArray jarray=new JSONArray(json);
                //Se divide cada JSONArray en sus partes individuales y se guardan en un objeto Persona.
                //Los objetos Persona se añaden a un ArrayList.
                for(int i=0;i<jarray.length();i++){
                    JSONArray jarraycito=jarray.getJSONArray(i);
                    JSONObject jdatos=jarraycito.getJSONObject(0);
                    JSONObject jloc=jarraycito.getJSONObject(1);
                    JSONObject jad=jarraycito.getJSONObject(2);
                    JSONObject jpunt=jarraycito.getJSONObject(3);
                    Datos datos=new Datos(jdatos.getString("dni"),jdatos.getString("empresa"),jdatos.getString("nombre"),
                            jdatos.getInt("telefono"),jdatos.getString("email"),null,jdatos.getBoolean("urgente"),null);
                    Localizacion loc=new Localizacion(jloc.getString("dni"),jloc.getDouble("latitud"),jloc.getDouble("longitud"));

                    Adicionales ad=new Adicionales(jad.getString("dni"),jad.getString("titulo"),jad.getString("descripcion"));
                    ArrayList<String> review=new ArrayList<>();
                    for (int j=0;j<jpunt.getInt("total");j++){
                        String texto=jpunt.getString("review"+j);
                        review.add(texto);
                    }
                    Puntuacion punt=new Puntuacion(jpunt.getString("dni"),review,jpunt.getDouble("puntuacion"));
                    Persona p=new Persona(datos,loc,ad,punt);
                    personas.add(p);
                }

                sc.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i=0;i<personas.size();i++){
                System.out.println(personas.get(i).toString());
            }
            return personas;
        }
    }


}
