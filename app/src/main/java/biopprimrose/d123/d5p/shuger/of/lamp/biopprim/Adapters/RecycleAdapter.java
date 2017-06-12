package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.OnRecycleListener;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amemiyaY on 2016/05/07.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<OtherUsersList> data;
    private Context context;
    private OnRecycleListener listener;


    public RecycleAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public RecycleAdapter(Context context, List<OtherUsersList> data){
        this(context);
        this.data = data;
    }

    public RecycleAdapter(Context context, List<OtherUsersList> data, OnRecycleListener listener) {
        this(context,data);
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item_title_recycle,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.v("recycleradapter",position+"");

        if (data != null && data.size() > position && data.get(position) != null ) {
            String img_path =  data.get(position).getFilepath();
            Bitmap bitmap = BitmapFactory.decodeFile(img_path);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            if (bitmap != null && bitmap.getHeight() < bitmap.getWidth()) {
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            holder.imageView.setImageBitmap(bitmap);

//            holder.imageView.setRotation(90);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecycleClicked(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public List<OtherUsersList> getOtherUsersList() {
        return this.data;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.list_item_recycler);
        }
    }
}
