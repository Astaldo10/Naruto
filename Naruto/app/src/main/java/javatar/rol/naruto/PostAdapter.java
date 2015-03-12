package javatar.rol.naruto;

import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by astaldo on 20/12/14.
 */
public class PostAdapter extends BaseAdapter {

    private final Activity activity;
    private final Vector<Post> posts;

    public PostAdapter (Activity activity, JSONArray posts) throws JSONException {

        super();
        this.activity = activity;
        this.posts = Post.getPostVector(posts);

    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem (int position) {
        return posts.elementAt(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View post = inflater.inflate(R.layout.element, null, true);
        Post currentPost = posts.elementAt(position);
        int color = Color.parseColor(currentPost.getColor());
        int white = Color.parseColor("#FFFFFF");
        float hsv [] = new float [3];
        Color.colorToHSV(color, hsv);

        LinearLayout background = (LinearLayout) post.findViewById(R.id.postBackground);
        TextView postId = (TextView) post.findViewById(R.id.postId);
        TextView autor = (TextView) post.findViewById(R.id.autor);
        TextView grupo = (TextView) post.findViewById(R.id.grupo);
        TextView fecha = (TextView) post.findViewById(R.id.fecha);
        TextView hora = (TextView) post.findViewById(R.id.hora);
        TextView texto = (TextView) post.findViewById(R.id.texto);

        background.setBackgroundColor(color);
        postId.setText(String.valueOf(currentPost.getId()));
        autor.setText(currentPost.getNombre());
        grupo.setText(currentPost.getGrupo());
        fecha.setText(currentPost.getFecha());
        hora.setText(currentPost.getHora());
        texto.setText(Html.fromHtml(currentPost.getTexto()));

        if (hsv[2] < 0.70){

            postId.setTextColor(white);
            autor.setTextColor(white);
            grupo.setTextColor(white);
            fecha.setTextColor(white);
            hora.setTextColor(white);

        }

        texto.setVisibility(View.GONE);

        return post;

    }

    public boolean isDeleted (int position){

        return posts.elementAt(position).getBorrado();

    }

    public String getColor (int position){

        return posts.elementAt(position).getColor();

    }
}
