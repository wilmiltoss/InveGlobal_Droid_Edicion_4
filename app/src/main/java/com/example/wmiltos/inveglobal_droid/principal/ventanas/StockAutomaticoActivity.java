package com.example.wmiltos.inveglobal_droid.principal.ventanas;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class StockAutomaticoActivity extends AppCompatActivity {

    TextView txScanning, txDescripcion, txDetalle, txSector, txCantidad, txIScanning,
            txIconteo, txInroLocacion,txIsoporte, txInivel,txImetro, txInroSoporte,
            txSumaCantidad,txIdLetraSoporte,txResulSuma, nroLecturas, txUsuario, txSectorDescripcion, campoCantidad, tvIdInventarioL, tvClaveS;;
    ConexionSQLiteHelper conn;

    Button btnGrabar;
    private Cursor fila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_automatico);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        variables();
        recepcionDatosLecturas();
        consultar();
        mostrarCantidad();
        sumaRegistrosSQL();
        muestraSectorDescripcion();
        btnAutomatico();
        verIdInventario();


    }
    //onClick automatico
    private void btnAutomatico (){
        int TIEMPO = 250; //como ejemplo 1 segundos = (1000 milisegundos)

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Ejecuta clic en boton, metodo o proceso.
                btnGrabar.performClick();
            }
        }, TIEMPO);
    }


    //recepcion de los mensajes de la ventana anterior
    private void recepcionDatosLecturas() {
        Bundle miBundle = this.getIntent().getExtras();
        if(miBundle!=null){
            //1
            String locacion =miBundle.getString("msjEnroLocacion");
            txInroLocacion.setText(locacion);
            //2
            String conteo =miBundle.getString("msjEconteo");
            txIconteo.setText(conteo);
            //3
            String soporte =miBundle.getString("msjEsoporte");
            txIsoporte.setText(soporte);
            //4
            String nroSoporte =miBundle.getString("msjEnroSoporte");
            txInroSoporte.setText(nroSoporte);
            //6
            String nivel =miBundle.getString("msjEnivel");
            txInivel.setText(nivel);
            //7
            String metro =miBundle.getString("msjEmetro");
            txImetro.setText(metro);
            //8
            String scanning =miBundle.getString("msjScanning");
            txScanning.setText(scanning);
            //9
            String usuario =miBundle.getString("msjUsu");
            txUsuario.setText(usuario);

            String claveInventario =miBundle.getString("msjClaveS");
            tvClaveS.setText(claveInventario);
        }
    }

    private void variables() {
        //TX que muestra en la pantalla
        btnGrabar = (Button) findViewById(R.id.btn_grabar);
        conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
        txScanning = (TextView) findViewById(R.id.tx_Scanning_Stock);
        txDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        txDetalle = (TextView) findViewById(R.id.txtDetalle);
        txSector = (TextView) findViewById(R.id.txtSector);
        txCantidad = (TextView) findViewById(R.id.txtCantidad);
        txDescripcion = (TextView) findViewById(R.id.etDescripcion);
        txSumaCantidad=(TextView)findViewById(R.id.tx_sumaCantidad);
        txResulSuma=(TextView)findViewById(R.id.txResulSuma);
        nroLecturas=(TextView) findViewById(R.id.tvNroLecturas);
        txSectorDescripcion=(TextView)findViewById(R.id.txtSectorDescrip);
        tvClaveS=(TextView)findViewById(R.id.tv_clave_s);


        //Impresion backup
        txInroLocacion = (TextView)findViewById(R.id.tvInroLocacion);    //1
        txIconteo = (TextView)findViewById(R.id.tvIconteo);              //2
        txIsoporte = (TextView)findViewById(R.id.tvIsoporte);            //3
        txInroSoporte = (TextView)findViewById(R.id.tvInroSoporte);      //4
        txIdLetraSoporte=(TextView) findViewById(R.id.tx_IdLetraSoporte);//5
        txInivel = (TextView)findViewById(R.id.tvInivel);                //6
        txImetro = (TextView)findViewById(R.id.tvImetro);                //7
        txIScanning = (TextView) findViewById(R.id.tx_Scanning_Stock);   //8
        txUsuario = (TextView)findViewById(R.id.tx_Usuario);             //9
        tvIdInventarioL = (TextView)findViewById(R.id.tv_IdInventarioL);

        //campo editable
        campoCantidad = (TextView) findViewById(R.id.etCantidad);         //9
        // txScanning=(EditText)findViewById(R.id.etActualizaScanning);

    }
    //muestra los datos traidos de LecturasActivity en la pantalla StockActivity
    private void consultar() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {txIScanning.getText().toString()};
        String[] campos = {Utilidades.CAMPO_DESCRIPCION_SCANNING, Utilidades.CAMPO_DETALLE, Utilidades.CAMPO_ID_SECTOR};

        try {
            Cursor cursor = db.query(Utilidades.TABLA_MAESTRO, campos, Utilidades.CAMPO_SCANNING + "=?", parametros, null, null, null);
            cursor.moveToFirst();
            txDescripcion.setText(cursor.getString(0));//muestra los campos
            txDetalle.setText(cursor.getString(1));
            txSector.setText(cursor.getString(2));
            cursor.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "El codigo no existe", Toast.LENGTH_LONG).show();
            limpiar();
        }
    }
    private void limpiar() {
        txInroLocacion.setText("");
        txIconteo.setText("");
        txIsoporte.setText("");
        txInroSoporte.setText("");
        txInivel.setText("");
        txImetro.setText("");
        txIScanning.setText("");
        txIdLetraSoporte.setText("");
        txCantidad.setText("");
        txScanning.setText("");
        txDescripcion.setText("");
        txSector.setText("");
        txDetalle.setText("");
        txCantidad.setText("");
        txSumaCantidad.setText("");
        campoCantidad.setText("");
    }

    //registro SQL
    private void registrarTablaLecturaSQL() {
        try{
            //CONEXION
            SQLiteDatabase db = conn.getWritableDatabase();
            //CON SENTENCIA SQL
            String insert="INSERT INTO "+ Utilidades.TABLA_LECTURAS +"("
                    +Utilidades.CAMPO_ID_LOCACION_L+","
                    +Utilidades.CAMPO_NRO_CONTEO+","
                    +Utilidades.CAMPO_ID_SOPORTE_L+","
                    +Utilidades.CAMPO_NRO_SOPORTE_L+","
                    +Utilidades.CAMPO_LETRA_SOPORTE_L+","
                    +Utilidades.CAMPO_NIVEL+","
                    +Utilidades.CAMPO_METRO+","
                    +Utilidades.CAMPO_SCANNING_L+","
                    +Utilidades.CAMPO_CANTIDAD+","
                    +Utilidades.CAMPO_ID_USUARIO_L+","
                    +Utilidades.CAMPO_ID_INVENTARIO_L+")"+
                    "VALUES ("+txInroLocacion.getText().toString()+", '"
                    +txIconteo.getText().toString()+"','"
                    +txIsoporte.getText().toString()+"','"
                    +txInroSoporte.getText().toString()+"','"
                    +txIdLetraSoporte.getText().toString()+"','"
                    +txInivel.getText().toString()+"','"
                    +txImetro.getText().toString()+"','"
                    +txScanning.getText().toString()+"','"
                    +campoCantidad.getText().toString()+"','"
                    +txUsuario.getText().toString()+ "','"
                    +tvIdInventarioL.getText().toString()+
                    "')";

            //"Id Registrado" es el mensaje que envia al insertar
            Toast.makeText(getApplicationContext(),"Lectura Registrada",Toast.LENGTH_SHORT).show();
            db.execSQL(insert);
            db.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al registrar lectura", Toast.LENGTH_LONG).show();
        }
    }

    //registro de un nuevo campo en la tabla Lecturas
    private void registrarDialogo() {
        registrarTablaLecturaSQL();
        limpiar();
        volverAmenu();//si esta correcto, retrocede a la ventana anterior
    }

    private void actualizarDialogo() {
        actualizarTablaLecturaSQL3();
        limpiar();
        volverAmenu();//si esta correcto, retrocede a la ventana anterior

    }

    //finaliza la pantalla StockActivity p/ desplegar LeturasActivity
    private void volverAmenu() {
        StockAutomaticoActivity.this.finish();
    }

    //mostrar la cantidad de existencia del codigo cargado
    public void mostrarCantidad(){
        try{
            SQLiteDatabase db = conn.getReadableDatabase();
            String[] parametros =  {txInroLocacion.getText().toString(),
                    txIconteo.getText().toString(),
                    txIsoporte.getText().toString(),
                    txInroSoporte.getText().toString(),
                    txInivel.getText().toString(),
                    txImetro.getText().toString(),
                    txScanning.getText().toString()};
            //Si la tabla tiene registros muestra la cantidad sino no lo muestra y pasa al sgte ventana
            try{
                Cursor cursor = db.rawQuery("SELECT "+Utilidades.CAMPO_CANTIDAD+
                                " FROM "+Utilidades.TABLA_LECTURAS+" WHERE "+Utilidades.CAMPO_ID_LOCACION_L+"=? AND "
                                +Utilidades.CAMPO_NRO_CONTEO+"=? AND "
                                +Utilidades.CAMPO_ID_SOPORTE_L+"=? AND "
                                +Utilidades.CAMPO_NRO_SOPORTE_L+"=? AND "
                                +Utilidades.CAMPO_NIVEL+"=? AND "
                                +Utilidades.CAMPO_METRO+"=? AND "
                                +Utilidades.CAMPO_SCANNING_L+"=?"
                        ,parametros);
                cursor.moveToFirst();
                //muestra los campos
                txSumaCantidad.setText(cursor.getString(0));
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sin registros", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al mostrar la cantidad", Toast.LENGTH_LONG).show();
        }
    }


    public void sumaStock(){
        int aux1 = Integer.valueOf(txSumaCantidad.getText().toString());
        int aux2 = Integer.valueOf(campoCantidad.getText().toString());

        int resultado = aux1 + aux2;
        txResulSuma.setText(""+resultado);
    }

    private void actualizarTablaLecturaSQL3() {
        try{
            sumaStock();
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = { txInroLocacion.getText().toString(),
                    txIconteo.getText().toString(),
                    txIsoporte.getText().toString(),
                    txInroSoporte.getText().toString(),
                    txInivel.getText().toString(),
                    txImetro.getText().toString(),
                    txScanning.getText().toString()};

            ContentValues valores = new ContentValues();
            valores.put(Utilidades.CAMPO_CANTIDAD,txResulSuma.getText().toString());
            db.update(Utilidades.TABLA_LECTURAS,valores, Utilidades.CAMPO_ID_LOCACION_L+"=? AND "
                            +Utilidades.CAMPO_NRO_CONTEO+"=? AND "
                            +Utilidades.CAMPO_ID_SOPORTE_L+"=? AND "
                            +Utilidades.CAMPO_NRO_SOPORTE_L+"=? AND "
                            +Utilidades.CAMPO_NIVEL+"=? AND "
                            +Utilidades.CAMPO_METRO+"=? AND "
                            +Utilidades.CAMPO_SCANNING_L+"=?"
                    ,parametros);
            Toast.makeText(getApplicationContext(),"Se agrego la cantidad",Toast.LENGTH_SHORT).show();
            db.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al actualizar los datos", Toast.LENGTH_LONG).show();
        }

    }

    public void dialogo(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(StockAutomaticoActivity.this);
        dialogo.setMessage("Desea salir del Stock?").setTitle("InveGlobal")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Intent intent = new Intent(StockActivity.this, LoginActivity.class);
                //startActivity(intent);
                StockAutomaticoActivity.this.finish();//finaliza la ventana
            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    //suma la cantidad agregada al codigo
    public void sumaRegistrosSQL(){
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ Utilidades.TABLA_LECTURAS,null);
        nroLecturas.setText("Nro.Lecturas: "+c.getCount());
        db.close();
    }

    public void muestraSectorDescripcion (){

        SQLiteDatabase db = conn.getWritableDatabase();

        String[] parametros = {txSector.getText().toString()};
        String[] campos = {Utilidades.CAMPO_ID_SECTOR, Utilidades.CAMPO_DESCRIPCION_SECTOR};

        try {
            Cursor cursor = db.query(Utilidades.TABLA_SECTORES, campos, Utilidades.CAMPO_ID_SECTOR + "=?", parametros, null, null, null);
            cursor.moveToFirst();
            txSectorDescripcion.setText(cursor.getString(1));//muestra los campos
            cursor.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en mostrar sector", Toast.LENGTH_LONG).show();
            limpiar();
        }

    }

    public void btnGrabar(View view) {
     try {
         //convertimos el campo a int p/ enviarlo a seleccionarConteo
         int campo = Integer.parseInt(txIconteo.getText().toString());
         conteoEnProceso(campo);
         if (campoCantidad.getText().length() != 0) {//si el campo esta vacio, mostrar mensaje

             SQLiteDatabase db = conn.getWritableDatabase();
             String[] parametros = {txInroLocacion.getText().toString(),
                     txIconteo.getText().toString(),
                     txIsoporte.getText().toString(),
                     txInroSoporte.getText().toString(),
                     txInivel.getText().toString(),
                     txImetro.getText().toString(),
                     txScanning.getText().toString(),
                     tvIdInventarioL.getText().toString()};
             fila = db.rawQuery("SELECT " + Utilidades.CAMPO_CANTIDAD +

                             " FROM " + Utilidades.TABLA_LECTURAS + " WHERE "
                             + Utilidades.CAMPO_ID_LOCACION_L + "=? AND "
                             + Utilidades.CAMPO_NRO_CONTEO + "=? AND "
                             + Utilidades.CAMPO_ID_SOPORTE_L + "=? AND "
                             + Utilidades.CAMPO_NRO_SOPORTE_L + "=? AND "
                             + Utilidades.CAMPO_NIVEL + "=? AND "
                             + Utilidades.CAMPO_METRO + "=? AND "
                             + Utilidades.CAMPO_SCANNING_L + "=? AND "
                             + Utilidades.CAMPO_ID_INVENTARIO_L + "=?"
                     , parametros, null);
             //si el select tira cantidad va actualizar, sino va registrar
             if (fila.moveToFirst()) {
                 if (fila != null) {
                     actualizarDialogo();
                 }
             } else {
                 registrarDialogo();
             }

         } else {
             Toast.makeText(getApplicationContext(), "El campo esta vacio", Toast.LENGTH_LONG).show();
         }
     }catch (Exception e){
         Toast.makeText(getApplicationContext(), "Error al grabar", Toast.LENGTH_LONG).show();
     }

    }
    //CONSULTA DEL ID INVENTARIO
    public void verIdInventario (){
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            String[] campos = new String[] {"ID_INVENTARIO"};
            String[] args = new String[] {"1"};
            Cursor c = db.query(Utilidades.TABLA_CONFIGURACIONES, campos, "ID_INVENTARIO>?", args, null, null, null);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String ID_INVENTARIO = c.getString(0);
                    tvIdInventarioL.setText(ID_INVENTARIO);
                    Log.i("id_inventario", ID_INVENTARIO);
                } while(c.moveToNext());
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al consultar IdInventario", Toast.LENGTH_SHORT).show();
        }
    }

    //Seleccionar conteo
    public void conteoEnProceso(int conteo) {
        //Actualiza los campos a 1 de acuerdo al nro de conteo
        try{
            if (conteo == 1){
                actualizarConteoEnProceso("CONTEO1");
            }else if (conteo == 2){
                actualizarConteoEnProceso("CONTEO2");
            }else if (conteo == 3){
                actualizarConteoEnProceso("CONTEO3");
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al actualizar nro de conteo", Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarConteoEnProceso(String CONTEO){
        SQLiteDatabase db = conn.getWritableDatabase();
        String claveSoporte = tvClaveS.getText().toString();//ID_CLAVE
        try {//actualiza el campo conteo solo si es igual a 0
            String update = "UPDATE " + Utilidades.TABLA_INVENTARIO_SOPORTE + " SET " + CONTEO + " = 1 " +
                    "WHERE " + Utilidades.CAMPO_ID_CLAVE + "= " +claveSoporte+ " AND " + CONTEO + " = 0 ";
            db.execSQL(update);
            db.close();
            //"Id Registrado" es el mensaje que envia al insertar
            //Toast.makeText(getApplicationContext(), "Se inició el " + CONTEO, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Conteo ingresado sin clave inventario" , Toast.LENGTH_SHORT).show();
        }
    }


}
