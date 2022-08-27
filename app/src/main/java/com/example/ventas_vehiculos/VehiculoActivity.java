package com.example.ventas_vehiculos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class VehiculoActivity extends AppCompatActivity {
    EditText jetplaca,jetmarca,jetmodelo,jetvalor;
    Switch jetactivovhei;
    long resp;
    int sw;
    String placa,marca,modelo,valor;
    ClsOpenHelper admin=new ClsOpenHelper(this,"concesionario.db",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);
        //ocultar titulos por defecto y asociar objetos java con xml
        getSupportActionBar().hide();
        jetplaca=findViewById(R.id.etplaca1);
        jetmarca=findViewById(R.id.etmarca1);
        jetmodelo=findViewById(R.id.etmodelo1);
        jetvalor=findViewById(R.id.etvalor1);
        jetactivovhei=findViewById(R.id.swactivo1);
        sw=0;
    }
    public void Guardar(View view){

        placa=jetplaca.getText().toString();
        marca=jetmarca.getText().toString();
        modelo=jetmodelo.getText().toString();
        valor=jetvalor.getText().toString();
        if(placa.isEmpty() || marca.isEmpty()||
        modelo.isEmpty() || valor.isEmpty()){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
        else {
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
           registro.put("placa",placa);
            registro.put("marca",marca);
            registro.put("modelo",modelo);
            registro.put("valor",Integer.parseInt(valor));
            if (sw == 0) {
                resp = db.insert("TblVehiculo", null, registro);
            }
            else {
                resp = db.update("TblVehiculo", registro, "placa='" + placa + "'", null);
                sw=0;
            }


            if (resp > 0){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            db.close();


        }
    }

    public void Consultar(View view){
        placa=jetplaca.getText().toString();
        if (placa.isEmpty()){
            sw=1;
            Toast.makeText(this, "La placa es requerida", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblVehiculo where placa='" + placa + "'",null);
            if (fila.moveToNext()){
                sw=1;
                jetmarca.setText(fila.getString(1));
                jetmodelo.setText(fila.getString(2));
                jetvalor.setText(fila.getString(3));
                if(fila.getString(4).equals("si"))
                    jetactivovhei.setChecked(true);
                else
                    jetactivovhei.setChecked(false);
            }
            else
                Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

public void cancelar (View view){
        Limpiar_campos();
}
public void Anular(View view){
        if(sw==0){
            Toast.makeText(this, "primero debe consultar", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
        else {
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","no");
            resp=db.update("TblVehiculo",registro,"placa='" + placa + "'",null);
            if(resp > 0){
                Toast.makeText(this, "registro anulado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else {
                Toast.makeText(this, "error anulando registro", Toast.LENGTH_SHORT).show();
            }
        }
}

    private void Limpiar_campos(){
        jetplaca.setText("");
        jetvalor.setText("");
        jetmodelo.setText("");
        jetmarca.setText("");
        jetactivovhei.setChecked(false);
        jetplaca.requestFocus();
        sw=0;
    }



    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }
}