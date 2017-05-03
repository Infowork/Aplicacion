package proyecto.infowork;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfesionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String a;
    int n;
    ImageView aa,bb,cc,dd,ee,ff;
    TextView profesion;
    Button sigue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesion);

        //Referencia a objetos
        aa=(ImageView)this.findViewById(R.id.img_empleo1);
        bb=(ImageView)this.findViewById(R.id.img_empleo2);
        cc=(ImageView)this.findViewById(R.id.img_empleo3);
        dd=(ImageView)this.findViewById(R.id.img_empleo4);
        ee=(ImageView)this.findViewById(R.id.img_empleo5);
        ff=(ImageView)this.findViewById(R.id.img_empleo6);

        profesion=(TextView) this.findViewById(R.id.tv_empleo);

        sigue=(Button) this.findViewById(R.id.b_sigue);
        sigue.setVisibility(View.GONE);

        //Creacion del menu lateral.
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

    public void empleo1(View v){
        a="Electicista";
        n=1;
        profesion.setText(a);
        sigue.setVisibility(View.VISIBLE);
    }
    public void empleo2(View v){
        a="Carpintero";
        n=2;
        profesion.setText(a);
        sigue.setVisibility(View.VISIBLE);
    }
    public void empleo3(View v){
        a="Limpieza";
        n=3;
        profesion.setText(a);
        sigue.setVisibility(View.VISIBLE);
    }
    public void empleo4(View v){
        a="Fontanero";
        n=4;
        profesion.setText(a);
        sigue.setVisibility(View.VISIBLE);
    }
    public void empleo5(View v){
        a="Pintor";
        n=5;
        profesion.setText(a);
        sigue.setVisibility(View.VISIBLE);
    }
    public void empleo6(View v){
        a="Albañil";
        n=6;
        profesion.setText(a);
        sigue.setVisibility(View.VISIBLE);
    }
    public void Sigue (View v){
        //pasa a la siguiente pantalla
        Toast.makeText(this,"Profesion: "+a+" num: "+n,Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,RegistroActivity.class);
        intent.putExtra("empleo",a);
        intent.putExtra("num_empleo",n);

        this.startActivity(intent);
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
