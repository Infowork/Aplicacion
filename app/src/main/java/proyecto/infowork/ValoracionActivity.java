package proyecto.infowork;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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

public class ValoracionActivity extends AppCompatActivity {

    TextView nombre,empresa;
    RatingBar rb;
    EditText review;
    String dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoracion);
        Intent intent=this.getIntent();

        dni=intent.getStringExtra("dni");
        nombre=(TextView)this.findViewById(R.id.tv_nombre);
        nombre.setText(intent.getStringExtra("nombre"));
        empresa=(TextView)this.findViewById(R.id.tv_empresa);
        empresa.setText(intent.getStringExtra("empresa"));
        rb=(RatingBar)this.findViewById(R.id.rb_val);
        rb.setNumStars(5);
        review=(EditText)this.findViewById(R.id.edt_review);
    }

    public void enviarVal(View v){
        if(rb.getRating()>0 && review.getText().toString().length()>0){
            int puntuacion=(int)rb.getRating();
            EnviarValoracion enviar=new EnviarValoracion();
            enviar.execute(new Valoracion(dni,review.getText().toString(),puntuacion));
        }else{
            Toast.makeText(this,"Por favor, complete todos los campos",Toast.LENGTH_LONG).show();
        }
    }

    class EnviarValoracion extends AsyncTask<Valoracion,Void,Void> {
        @Override
        protected Void doInBackground(Valoracion... params) {
            try {

                Socket sc = new Socket("192.168.0.172", 9000);

                PrintStream salida = new PrintStream(sc.getOutputStream());
                salida.println("bbb");
                salida.flush();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JSONObject jval = new JSONObject();
                jval.put("dni", params[0].getDni());
                jval.put("texto", params[0].getTexto());
                jval.put("puntuacion", params[0].getPuntuacion());

                salida.println(jval.toString());
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ValoracionActivity.this.finish();
        }
    }
}
