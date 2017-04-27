package gestion;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jorge on 27/04/2017.
 */

public class GestionPermisosIniciales {

    private int PermissionCode=10;
    private Context ctx;
    private Activity act;

    ArrayList<String> permisos=new ArrayList<>();

    ArrayList<String> permisosFallidos=new ArrayList<>();

    public GestionPermisosIniciales(Context ctx,Activity act){

        permisos.add("Manifest.permission.WRITE_EXTERNAL_STORAGE");
        permisos.add("Manifest.permission.INTERNET");
        permisos.add("Manifest.permission.ACCESS_NETWORK_STATE");
        permisos.add("Manifest.permission.ACCESS_COARSE_LOCATION");
        permisos.add("Manifest.permission.ACCESS_FINE_LOCATION");
        permisos.add("Manifest.permission.GET_ACCOUNTS");
        permisos.add("Manifest.permission.READ_CONTACTS");
        this.ctx=ctx;
        this.act=act;
        for(int i=0;i<permisos.size();i++){
            if(!comprobarPermiso(permisos.get(i))){
                permisosFallidos.add(permisos.get(i));
            }
        }
        if(permisosFallidos.size()>0){
            pedirPermiso(permisosFallidos);
        }

    }

    public boolean comprobarPermiso(String permiso){
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(ctx,permiso);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    public void pedirPermiso(ArrayList<String> permisos){
        String[] permiso=new String[permisos.size()];

        for(int i=0;i<permisos.size();i++){
            permiso[i]=permisos.get(i);
        }

        AlertDialog.Builder cuadro=new AlertDialog.Builder(act);
        cuadro.setTitle("Permisos");
        cuadro.setMessage("Los permisos siguientes son necesarios para el funcionamiento de la app.");
        cuadro.setPositiveButton(android.R.string.ok,null);
        cuadro.show();

        //And finally ask for the permission
        ActivityCompat.requestPermissions(act, permiso, PermissionCode);
    }

    /*//This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == PermissionCode){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Displaying a toast
                Toast.makeText(act,"Permiso aceptado",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(act,"Permiso denegado",Toast.LENGTH_LONG).show();
            }
        }
    }*/
}
