package pe.com.itaminasor.mypizza;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Spinner pizzaSpinner;
    private RadioGroup masaRadioGroup;
    private CheckBox extra1CheckBox;
    private CheckBox extra2CheckBox;
    private EditText direccionEditText;
    private Button ordenarButton;

    //HOLA
    //INAMINADOR ESTUVO X AQUI.


    //private String[] pizzasDesc = {"Seleccione","Americana","Pepperoni","Hawaiana","Meat Lover"};
    private int[] pizzasPrice = {0,38,42,36,56};
    private int extra1Price=4;
    private int extra2Price=8;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pizzaSpinner = findViewById(R.id.pizzaSpinner);
        masaRadioGroup = findViewById(R.id.masaRadioGroup);
        extra1CheckBox = findViewById(R.id.extra1CheckBox);
        extra2CheckBox = findViewById(R.id.extra2CheckBox);
        direccionEditText = findViewById(R.id.direccionEditText);
        ordenarButton = findViewById(R.id.ordenarButton);

        //fillPizzaSpinner();
        setEventos();
    }

//    private void fillPizzaSpinner(){
//        List<String> pizzas= new ArrayList<String>();
//        for (String desc : pizzasDesc) pizzas.add(desc);
//        ArrayAdapter userAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, pizzas);
//        pizzaSpinner.setAdapter(userAdapter);
//    }



    private void setEventos(){
        ordenarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processOrdenar();
            }
        });
    }

    public void processOrdenar(){

        boolean isvalid= validateForm();

        if (isvalid) {
            String message =getMessage();
            showConfirm(message);
            sendNotification();
        }

    }

    private boolean validateForm(){

        int pos= pizzaSpinner.getSelectedItemPosition();
        if (pos==0){
            Toast.makeText(MainActivity.this,"Debe seleccionar el Tipo de Pizza.",Toast.LENGTH_SHORT).show();
            return false;
        }

        int masa_id = masaRadioGroup.getCheckedRadioButtonId();
        if (masa_id<0){
            Toast.makeText(MainActivity.this,"Debe seleccionar el Tipo de Masa ",Toast.LENGTH_SHORT).show();
            return false;
        }

        String direccion=direccionEditText.getText().toString();
        if (direccion.isEmpty()){
            Toast.makeText(MainActivity.this,"Debe ingresa Dirección ",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private String getMessage(){


        Calendar calendar =Calendar.getInstance();
        int dayofweek=calendar.get(Calendar.DAY_OF_WEEK);


        int pos= pizzaSpinner.getSelectedItemPosition();
        String pizza_desc = pizzaSpinner.getSelectedItem().toString();
        int pizza_price=pizzasPrice[pos];

        int masa_id = masaRadioGroup.getCheckedRadioButtonId();
        RadioButton radio = findViewById(masa_id);
        String masa_desc= radio.getText().toString();

        String message="Su pedido de ".concat(pizza_desc).concat(" con ").concat(masa_desc);
        double totalPrice=pizza_price;

        if (extra1CheckBox.isChecked()) {
            message=message.concat(" con ").concat(extra1CheckBox.getText().toString());
            totalPrice=totalPrice+extra1Price;
        }

        if (extra2CheckBox.isChecked()) {
            message=message.concat(" con ").concat(extra2CheckBox.getText().toString());
            totalPrice=totalPrice+extra2Price;
        }

        if (dayofweek==Calendar.TUESDAY) totalPrice = totalPrice * 0.7;
        message=message.concat(" a S./").concat(totalPrice+"").concat(" +  IGV esta en proceso de envío.");

        return message;

    }



    private void showConfirm(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Confirmación de pedido");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

       /* alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toasty.info(getApplicationContext(), "Seleccionó OK", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toasty.info(getApplicationContext(), "Seleccionó OK", Toast.LENGTH_LONG).show();
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NEUTRO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toasty.info(getApplicationContext(), "Seleccionó OK", Toast.LENGTH_LONG).show();
            }
        });*/


    }


    private void sendNotification(){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                showNotification();
            }
        }, 10000);
    }

    private void showNotification(){

        Notification notification = new NotificationCompat.Builder(this, "default")
                .setContentTitle("Pedido en camino")
                .setContentText("Su pedido llegará en 30 minutos...")
                .setSmallIcon(R.drawable.ic_pizza)
                .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            // Se define un objecto NotificationChannel en Android 8
            NotificationChannel channel = new NotificationChannel(
                    "default",
                    "Channel name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel description");
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0, notification);
        }else{
            NotificationManager notificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

    }


}
