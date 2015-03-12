package javatar.rol.naruto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Identificacion extends Activity {

    private EditText user;
    private EditText pass;
    private Button access;
    private TextView error;
    private CheckBox check;

    private String username;
    private String password;
    private String response;
    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.access);

        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        access = (Button) findViewById(R.id.accessButton);
        error = (TextView) findViewById(R.id.error);
        check = (CheckBox) findViewById(R.id.checkLogin);

        access.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (compIdent())
                    access();

            }
        });

        Bundle extras = getIntent().getExtras();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        if (extras == null) {

            if (compSavedState()) {

                user.setText(username);
                pass.setText(password);

                if (preferences.getBoolean(Constantes.SAVE, false))
                    check.setChecked(true);

            }

        } else {


            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constantes.LOGGED, extras.getBoolean(Constantes.LOGGED));
            editor.putBoolean(Constantes.SAVE, false);
            editor.putString(Constantes.SAVED_USER, Constantes.EMPTY);
            editor.putString(Constantes.SAVED_PASS, Constantes.EMPTY);
            editor.commit();

        }

    }

    private boolean compSavedState (){

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        if (preferences.getBoolean(Constantes.LOGGED, false)){

            username = preferences.getString(Constantes.SAVED_USER, Constantes.EMPTY);
            password = preferences.getString(Constantes.SAVED_PASS, Constantes.EMPTY);
            return true;

        }

        return false;

    }

    private boolean compIdent (){

        username = user.getText().toString();
        password = pass.getText().toString();

        if (username.equals("") || password.equals("")){

            response = getString(R.string.empty);
            error.setText(response);
            return false;

        }

        return true;

    }

    private void access () {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Conexion con = Conexion.createConexion();
                    con.createLogin(username, password);
                    con.sendRequest(Constantes.ACCESS);

                    JSONArray resp = con.getResponse();

                    if (resp.getJSONObject(0).getBoolean(Constantes.EXITO)) {

                        color = resp.getJSONObject(0).getString(Constantes.COLOR);
                        showForo();

                    }
                    else
                        response = getString(R.string.fail);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            error.setText(response);

                        }
                    });

                    con.flush();
                    con.closeConexion();

                } catch (IOException ioe){
                    ioe.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }).start();
    }

    private void showForo () throws IOException, JSONException {

        Intent intent = encapsulate(getApplicationContext(), Foro.class);
        savePreferences();
        startActivity(intent);
        finish();

    }

    private Intent encapsulate (Context context, Class c) throws  IOException {

        Intent intent = new Intent (context, c);

        intent.putExtra(Constantes.LOGIN_SERVER, Constantes.SERVER);
        intent.putExtra(Constantes.LOGIN_PORT, Constantes.PORT);
        intent.putExtra(Constantes.LOGIN_USER, username);
        intent.putExtra(Constantes.LOGIN_PASS, password);
        intent.putExtra(Constantes.LOGIN_COLOR, color);

        return intent;

    }

    private void savePreferences (){

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constantes.SAVED_USER, username);
        editor.putString(Constantes.SAVED_PASS, password);
        editor.putBoolean(Constantes.LOGGED, true);

        if (check.isChecked())
            editor.putBoolean(Constantes.SAVE, true);
        else
            editor.putBoolean(Constantes.SAVE, false);

        editor.commit();

    }
}
