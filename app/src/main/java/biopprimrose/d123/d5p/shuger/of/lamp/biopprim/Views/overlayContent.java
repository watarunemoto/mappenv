package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Created by amemiyaY on 2016/07/01.
 */
public class overlayContent extends View {

    private int width;
    private int height;

    //constructor
    public overlayContent(Context context,int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int lefth = width / 6;
        int leftb = 5*width / 6;
        int righth =height / 6;
        int rightb = 5*height / 6;

        Log.v("list_of_point","moto" + width + height + "," +  leftb +lefth+rightb+righth +"");

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setARGB(150, 0, 0, 255);

        //描画：x1,y1,x2,y2,paint
        canvas.drawRect(lefth, righth, leftb, rightb, paint);
    }
}