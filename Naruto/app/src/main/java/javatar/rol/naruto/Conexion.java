package javatar.rol.naruto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by astaldo on 20/12/14.
 */
public class Conexion {

    private String server;
    private int port;

    private Socket socket;
    private BufferedReader inred;
    private PrintStream outred;

    private JSONArray response;
    private JSONObject login;
    private JSONObject post;

    private Conexion (String server, int port) throws IOException {

        this.server = server;
        this.port = port;
        connect();

    }

    public void connect () throws IOException {

        socket = new Socket(server, port);
        outred = new PrintStream(socket.getOutputStream());
        inred = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    public static Conexion createConexion () throws IOException {

        return new Conexion(Constantes.SERVER, Constantes.PORT);

    }

    public static Conexion createConexion (String server, int port) throws IOException {

        return new Conexion(server, port);

    }

    public void createLogin (String username, String password) throws JSONException {

        login = new JSONObject();
        login.put(Constantes.USER, username);
        login.put(Constantes.PASS, password);

    }

    public void createPost (JSONObject info) throws JSONException {

        Calendar calendar = new GregorianCalendar();

        String hora = ((calendar.get(Calendar.HOUR) < 10) ? "0" + calendar.get(Calendar.HOUR) : calendar.get(Calendar.HOUR)) + ":" + ((calendar.get(Calendar.MINUTE) < 10) ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE));
        String fecha = ((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH)) + "/" + ((calendar.get(Calendar.MONTH) < 10) ? "0" + calendar.get(Calendar.MONTH) : calendar.get(Calendar.MONTH)) + "/" + calendar.get(Calendar.YEAR);

        info.put(Constantes.HORA, hora);
        info.put(Constantes.FECHA, fecha);

        post = info;

    }

    public void sendRequest (String request) throws IOException, JSONException {

        switch (request){

            case Constantes.POST:
                sendPost();
                break;

            case Constantes.GET:
                sendGet();
                break;

            case Constantes.ACCESS:
                sendLogin();
                break;

            case Constantes.GROUP:
                sendGroups();
                break;

            default:
                break;

        }

    }

    public JSONArray getResponse (){

        return response;

    }

    public void flush (){

        outred.flush();

    }

    public void closeConexion () throws IOException {

        outred.close();
        socket.close();

    }

    private void sendGet () throws IOException, JSONException {

        outred.println(Constantes.GET);
        outred.println(login.getString(Constantes.USER));
        response = new JSONArray(inred.readLine());

    }

    private void sendLogin () throws IOException, JSONException {

        outred.println(Constantes.ACCESS);
        outred.println(login.toString());
        response = new JSONArray(inred.readLine());

    }

    private void sendGroups () throws IOException, JSONException {

        outred.println(Constantes.GROUP);
        outred.println(login.getString(Constantes.USER));
        response = new JSONArray(inred.readLine());

    }

    private void sendPost(){

        outred.println(Constantes.POST);
        outred.println(post.toString());

    }

}
