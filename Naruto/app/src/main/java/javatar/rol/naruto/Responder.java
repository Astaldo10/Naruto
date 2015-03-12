package javatar.rol.naruto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by astaldo on 24/12/14.
 */
public class Responder extends Activity implements View.OnClickListener {

    private Button responseButton;
    private EditText response;

    private String server;
    private int port;
    private String username;
    private String password;
    private String group;
    private String color;

    @Override
    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.response);

        responseButton = (Button) findViewById(R.id.responseButton);
        response = (EditText) findViewById(R.id.response);

        responseButton.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        server = extras.getString(Constantes.LOGIN_SERVER);
        port = extras.getInt(Constantes.LOGIN_PORT);
        username = extras.getString(Constantes.LOGIN_USER);
        password = extras.getString(Constantes.LOGIN_PASS);
        group = extras.getString(Constantes.GROUP_RESPONSE);
        color = extras.getString(Constantes.LOGIN_COLOR);

        ((TextView) findViewById(R.id.responseGroup)).setText(group);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.responseButton:
                post();
                break;

            default: break;

        }
    }

    private void post (){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Conexion con = Conexion.createConexion();
                    con.createLogin(username, password);
                    con.createPost(createInfo());
                    con.sendRequest(Constantes.POST);

                    con.flush();
                    con.closeConexion();
                    finish();

                } catch (IOException ioe){
                    ioe.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }).start();

    }

    private JSONObject createInfo (){

        JSONObject json = new JSONObject();

        try {

            json.put(Constantes.NOMBRE, username);
            json.put(Constantes.GRUPO, group.replace(" ", "_"));
            json.put(Constantes.COLOR, color);
            json.put(Constantes.TEXTO, response.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

    }
}
