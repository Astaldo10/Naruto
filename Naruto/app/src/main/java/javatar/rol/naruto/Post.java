package javatar.rol.naruto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * Created by astaldo on 20/12/14.
 */
public class Post {

    private int id;
    private boolean borrado;
    private String nombre;
    private String grupo;
    private String fecha;
    private String hora;
    private String color;
    private String texto;

    public Post (int id, boolean borrado, String nombre, String grupo, String fecha, String hora, String color, String texto){

        this.id = id;
        this.borrado = borrado;
        this.nombre = nombre;
        this.grupo = grupo;
        this.fecha = fecha;
        this.hora = hora;
        this.color = color;

        try {

            this.texto = new String(texto.getBytes(Constantes.ISO), Constantes.UTF8);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            this.texto = texto;

        }

    }

    public static Vector<Post> getPostVector (JSONArray array) throws JSONException {

        Vector<Post> vector = new Vector<Post>();

        for (int i = 0; i < array.length(); i++)
            vector.add(decode(array.getJSONObject(i)));

        return vector;

    }

    private static Post decode (JSONObject json) throws JSONException {

        return new Post (json.getInt(Constantes.ID), json.getBoolean(Constantes.BORRADO), json.getString(Constantes.NOMBRE), json.getString(Constantes.GRUPO).replace("_", " "), json.getString(Constantes.FECHA), json.getString(Constantes.HORA), json.getString(Constantes.COLOR), json.getString(Constantes.TEXTO));

    }

    public int getId (){

        return id;

    }

    public boolean getBorrado (){

        return borrado;

    }

    public String getNombre (){

        return nombre;

    }

    public String getGrupo (){

        return grupo;

    }

    public String getFecha (){

        return fecha;

    }

    public String getHora (){

        return hora;

    }

    public String getColor (){

        return color;

    }

    public String getTexto (){

        return texto;

    }

}
