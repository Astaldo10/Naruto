package javatar.rol.naruto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by astaldo on 20/12/14.
 */
public class Foro extends ListActivity implements AdapterView.OnItemSelectedListener {

    private Conexion con;
    private String server;
    private int port;
    private String username;
    private String password;
    private String color;

    private Spinner spin;
    private String group;
    private boolean ready;

    private ArrayAdapter<String> arrayAdapter;
    private PostAdapter postAdapter [];
    private String groups [];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);

        Bundle extras = getIntent().getExtras();
        server = extras.getString(Constantes.LOGIN_SERVER);
        port = extras.getInt(Constantes.LOGIN_PORT);
        username = extras.getString(Constantes.LOGIN_USER);
        password = extras.getString(Constantes.LOGIN_PASS);
        color = extras.getString(Constantes.LOGIN_COLOR);

        spin = (Spinner) findViewById(R.id.groups);
        spin.setOnItemSelectedListener(this);

        ready = false;

        if (!ready)
            init();
        else {

            spin.setAdapter(arrayAdapter);
            showGroup(spin.getSelectedItemPosition());
        }

    }

    @Override
    protected void onListItemClick (ListView listView, View view, int position, long id){

        TextView texto = (TextView) view.findViewById(R.id.texto);

        if (texto.getVisibility() == View.GONE)
            texto.setVisibility(View.VISIBLE);
        else
            texto.setVisibility(View.GONE);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (ready)
            showGroup(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foro_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){

        switch (item.getItemId()){

            case R.id.reply:
                reply();
                break;

            case R.id.refresh:
                refresh();
                break;

            case R.id.closeSession:
                closeSession();
                break;

            case R.id.exit:
                finish();
                break;

            default: break;

        }

        return true;

    }

    private void init(){

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    con = Conexion.createConexion(server, port);
                    con.createLogin(username, password);
                    con.sendRequest(Constantes.GROUP);

                    groups = groupsToString(con.getResponse());

                    con.flush();
                    con.closeConexion();

                    arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, groups);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            spin.setAdapter(arrayAdapter);

                        }
                    });

                    refresh();

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void loadListView (String groups [], JSONArray jsonPosts) throws JSONException {

        postAdapter = new PostAdapter[groups.length];

        for (int i = 0; i < postAdapter.length; i++)
            postAdapter[i] = new PostAdapter (this, filterByGroup(jsonPosts, groups[i]));

        showGroup(spin.getSelectedItemPosition());

    }

    private JSONArray filterByGroup (JSONArray groups, String group){

        JSONArray array = new JSONArray();
        JSONObject json;

        try {

            for (int i = 0; i < groups.length(); i++) {

                json = groups.getJSONObject(i);

                if (json.getString(Constantes.GRUPO).replace("_", " ").equals(group))
                    array.put(json);

            }

        } catch (JSONException je){
            je.printStackTrace();
        }

        return array;

    }

    private String [] groupsToString (JSONArray groups) throws JSONException{

        String groupArray [] = new String [groups.length()];

        for (int i = 0; i < groups.length(); i++)
            groupArray[i] = groups.getJSONObject(i).getString(Constantes.GRUPO).replace("_", " ");

        return groupArray;

    }

    private void showGroup (int position){

        group = groups[position];
        setListAdapter(postAdapter[position]);
        postAdapter[position].notifyDataSetChanged();

    }

    private void closeSession (){

        Intent intent = new Intent(this, Identificacion.class);
        intent.putExtra(Constantes.LOGGED, false);
        finish();
        startActivity(intent);

    }

    private void reply (){

        try {

            Intent intent = encapsulate(this, Responder.class);
            startActivity(intent);

        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void refresh(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    con = Conexion.createConexion(server, port);
                    con.createLogin(username, password);
                    con.sendRequest(Constantes.GET);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try{

                                loadListView(groups, con.getResponse());
                                ready = true;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    con.flush();
                    con.closeConexion();

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

    private Intent encapsulate (Context context, Class c) throws  IOException {

        Intent intent = new Intent (context, c);

        intent.putExtra(Constantes.LOGIN_SERVER, Constantes.SERVER);
        intent.putExtra(Constantes.LOGIN_PORT, Constantes.PORT);
        intent.putExtra(Constantes.LOGIN_USER, username);
        intent.putExtra(Constantes.LOGIN_PASS, password);
        intent.putExtra(Constantes.LOGIN_COLOR, color);
        intent.putExtra(Constantes.GROUP_RESPONSE, spin.getSelectedItem().toString());

        return intent;

    }
}
