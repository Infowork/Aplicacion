package proyecto.infowork;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

import gestion.GestionPermisosIniciales;
import javabean.Datos;
import javabean.Localizacion;
import javabean.Persona;
import javabean.Puntuacion;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Spinner spEmpleo;
    Spinner spRadio;
    LinearLayout resultado;
    GoogleMap gm;
    String empleo;
    String radioTexto;
    int radio;
    SupportMapFragment smf;
    Location usuario;
    ArrayList<MarkerOptions> markers;
    LocationManager lm;
    LatLng pos=new LatLng(0,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GestionPermisosIniciales(this,this);

        markers = new ArrayList<>();
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new LocationListener() {
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
            });
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
        //Referencia al Layout de resultado
        resultado = (LinearLayout) this.findViewById(R.id.Layout_tarjeta);
        resultado.setVisibility(View.GONE);

        //Referencias a los Spinner
        spEmpleo = (Spinner) this.findViewById(R.id.spn_empleo);
        spRadio = (Spinner) this.findViewById(R.id.spn_radio);

        ArrayAdapter adapterEmp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new String[]{"Limpieza", "Canguro", "Fontanero", "Electricista", "Pintor", "Albañilería"});
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

        smf.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gm = googleMap;

                //Definir el tipo de mapa
                gm.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                //Habilitar controles
                gm.getUiSettings().setZoomControlsEnabled(true);
                gm.getUiSettings().setMapToolbarEnabled(true);
                gm.getUiSettings().setMyLocationButtonEnabled(true);

                //Posicionar el mapa en la localización de la persona
                gm.clear();
                if (pos.latitude == 0 && pos.longitude == 0) {
                    try {
                        Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        gm.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l.getLatitude(), l.getLongitude()), 15));
                        System.out.println("Localizacion antigua!!");
                    }catch(SecurityException ex){
                        ex.printStackTrace();
                    }
                }else{
                    gm.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,15));
                    System.out.println("Localizacion nueva!!");
                }

                for(int i=0;i<markers.size();i++){
                    MarkerOptions mk=markers.get(i);
                    gm.addMarker(mk);
                }
            }

        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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
        new Comunicaciones().execute(empleo);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_alta) {
            Intent intentalta=new Intent(this,RegistroActivity.class);
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
            this.startActivity(intentajustes);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class Comunicaciones extends AsyncTask<String,Void,ArrayList<Persona>>{
        @Override
        protected void onPostExecute(ArrayList<Persona> personas) {
            for(int i=0;i<personas.size();i++){
                Location profesional=new Location("Profesional");
                profesional.setLatitude(personas.get(i).getLoc().getLatitud());
                profesional.setLongitude(personas.get(i).getLoc().getLongitud());
                float distancia=usuario.distanceTo(profesional);
                if(distancia<radio){
                    MarkerOptions mk=new MarkerOptions();
                    LatLng pos=new LatLng(personas.get(i).getLoc().getLatitud(),personas.get(i).getLoc().getLongitud());
                    mk.position(pos);
                    mk.title(personas.get(i).getDatos().getNombre());
                    mk.snippet(personas.get(i).getDatos().getEmpresa());
                    markers.add(mk);
                }
            }
        }

        @Override
        protected ArrayList<Persona> doInBackground(String... params) {
            String host = "192.168.1.16";
            int puerto = 9000;
            ArrayList<Persona> personas=new ArrayList<>();
            try {
                Socket sc = new Socket(host, puerto);
                PrintStream salida = new PrintStream(sc.getOutputStream());
                //Envía código de aplicacion
                salida.println("aaa");
                Thread.sleep(500);
                System.out.println(params[0]);
                salida.println(params[0]);
                System.out.println("!!!!!!Envio correcto¡¡¡¡¡¡");

                BufferedReader bf = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                String json=bf.readLine();
                System.out.println(json);

                JSONArray jarray=new JSONArray(json);

                for(int i=0;i<jarray.length();i++){
                    JSONArray jarraycito=jarray.getJSONArray(i);
                    JSONObject jdatos=jarraycito.getJSONObject(0);
                    JSONObject jloc=jarraycito.getJSONObject(1);
                    JSONObject jpunt=jarraycito.getJSONObject(3);
                    Datos datos=new Datos(jdatos.getString("dni"),jdatos.getString("empresa"),jdatos.getString("nombre"),
                            jdatos.getInt("telefono"),jdatos.getString("email"),null,jdatos.getBoolean("urgente"),null);
                    Localizacion loc=new Localizacion(jloc.getString("dni"),jloc.getDouble("latitud"),jloc.getDouble("longitud"));
                    ArrayList<String> review=new ArrayList<>();
                    for (int j=0;j<jpunt.getInt("total");j++){
                        String texto=jpunt.getString("review"+j);
                        review.add(texto);
                    }
                    Puntuacion punt=new Puntuacion(jpunt.getString("dni"),review,jpunt.getDouble("puntuacion"));
                    Persona p=new Persona(datos,loc,null,punt);
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

            return personas;
        }
    }
}
