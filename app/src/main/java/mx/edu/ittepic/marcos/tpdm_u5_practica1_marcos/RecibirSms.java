package mx.edu.ittepic.marcos.tpdm_u5_practica1_marcos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class RecibirSms extends BroadcastReceiver {

    BaseDatos base;
    String mensajeMostrar;

    public void onReceive(Context context, Intent intent) {
        base = new BaseDatos(context, "Horoscopo1", null, 1);
        datos();
        Bundle extras = intent.getExtras();
        Object[] pdus = (Object[]) extras.get("pdus");
        SmsMessage mensaje = SmsMessage.createFromPdu((byte[])pdus[0]);
        String descripcion;
        if(mensaje.getMessageBody().startsWith("HOROSCOPO")){
            String[] estructuraMensaje =  mensaje.getMessageBody().split("-");
            if(estructuraMensaje.length==2){
                descripcion = estructuraMensaje[1];
                mensajeMostrar = buscarDetalleHoroscopo(descripcion);
            }
        }else{
            mensajeMostrar = "Su mensaje debe tener el formato 'HOROSCOPO-TAURO'";
        }
        Toast.makeText(context,mensajeMostrar,Toast.LENGTH_LONG).show();
        enviarSMS(mensaje.getOriginatingAddress(),mensajeMostrar,context);
    }

    public String buscarDetalleHoroscopo(String horoscopo){
        try{
            SQLiteDatabase selec = this.base.getReadableDatabase();
            String[] claves = {horoscopo};
            String descripcion;
            Cursor c =  selec.rawQuery("SELECT * FROM Horosco WHERE NOMBRE = ?",claves);

            if(c.moveToFirst()){
                descripcion = c.getString(1);
            }else {
                descripcion = "No se encontró signo zodiacal";
            }
            return descripcion;
        }catch(SQLiteException e){
            return (e.getMessage());
        }
    }

    private void enviarSMS(String t, String m, Context c) {
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(t,null,m,null,null);
        }catch (Exception e){
            Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void datos(){
        SQLiteDatabase db = this.base.getWritableDatabase();
        db.execSQL("INSERT INTO Horosco VALUES('ARIES','Aries, habrá gratas sorpresas si dejas de estar pendiente del reloj ')");
        db.execSQL("INSERT INTO Horosco VALUES('TAURO','Tauro, recibirás buenas noticias a nivel profesional. Buscar nuevos retos mas ambiciosos, te permitirá sentirte satisfecho con el trabajo realizado')");
        db.execSQL("INSERT INTO Horosco VALUES('GEMINIS','Geminis, aceptar como buenas y válidas las ideas que te dará tu compañero de trabajo, será la salida a un contratiempo venidero')");
        db.execSQL("INSERT INTO Horosco VALUES('CANCER','Cancer, disfrutarás más del presente si te planteas metas más asequibles en el trabajo')");
        db.execSQL("INSERT INTO Horosco VALUES('LEO','Leo, permanecer alerta a lo largo de toda la jornada laboral de hoy, será primordial para que organices y planifiques con éxito todas tus tareas pendientes ')");
        db.execSQL("INSERT INTO Horosco VALUES('VIRGO','Virgo, todo marchará estupendamente para ti durante esta semana')");
        db.execSQL("INSERT INTO Horosco VALUES('LIBRA','Libra, deberás tomar la iniciativa, para tener las riendas de tu vida')");
        db.execSQL("INSERT INTO Horosco VALUES('ESCORPIO','Escorpio, hoy sucederá un acontecimiento importante y con el, grandes oportunidades para ti')");
        db.execSQL("INSERT INTO Horosco VALUES('SAGITARIO','Sagitario, este mes trae grandes experiencias que te harán crecer como persona y tomar las riendas de tu vida')");
        db.execSQL("INSERT INTO Horosco VALUES('CAPRICORNIO','Capricornio, surgirá una situación clave, que traerá una oportunidad a nuevas ilusiones')");
        db.execSQL("INSERT INTO Horosco VALUES('ACUARIO','Acuario, la alineación de los astros augura cosas buenas y emocionantes, que disfrutarás junto a tu pareja')");
        db.execSQL("INSERT INTO Horosco VALUES('PISCIS','Piscis, se avecina un nuevo reto en el trabajo, este ampliará tus perspectivas profesionales')");
        db.close();
    }




}
