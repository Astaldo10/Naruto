package javatar.rol.naruto;

/**
 * Created by astaldo on 17/12/14.
 */

public abstract class Constantes {

    // Códigos de operación con el servidor
    public static final String POST = "0";
    public static final String GET = "1";
    public static final String ACCESS = "2";
    public static final String GROUP = "3";

    // Datos de conexión con el servidor
    public static final String SERVER = "astaldo.noip.me";
    public static final int PORT = 12001;

    // Códigos de obtención de datos de la intercambio de JSON entre cliente y servidor
    public static final String USER = "user";
    public static final String PASS = "pass";
    public static final String EXITO = "exito";
    public static final String END = "END";

    // Códigos de datos de un post
    public static final String ID = "id";
    public static final String BORRADO = "borrado";
    public static final String NOMBRE = "nombre";
    public static final String GRUPO = "grupo";
    public static final String HORA = "hora";
    public static final String FECHA = "fecha";
    public static final String COLOR = "color";
    public static final String TEXTO = "texto";

    // Paso de mensajes entre Actividades
    public static final String LOGIN_SERVER = "server";
    public static final String LOGIN_PORT = "port";
    public static final String LOGIN_USER = "user";
    public static final String LOGIN_PASS = "pass";
    public static final String LOGIN_COLOR = "color";
    public static final String GET_JSON = "getJSON";
    public static final String GROUP_JSON = "groupJSON";
    public static final String GROUP_RESPONSE = "groupResponse";

    // Opciones de codificación
    public static final String UTF8 = "UTF-8";
    public static final String ISO = "ISO-8859-1";

    // Opciones de Preferencias
    public static final String LOGGED = "logged";
    public static final String SAVE = "save";
    public static final String SAVED_USER = "saved_user";
    public static final String SAVED_PASS = "saved_pass";
    public static final String EMPTY = "";

}
