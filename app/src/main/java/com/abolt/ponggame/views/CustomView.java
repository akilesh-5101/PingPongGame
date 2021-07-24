package com.abolt.ponggame.views;

import android.content.Context;/*
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;*/
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import androidx.annotation.Nullable;

import com.abolt.ponggame.GameActivity;
import com.abolt.ponggame.R;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Thread.sleep;


public class CustomView extends View {

    public RectF ceiling;
    public RectF slider;
    public Paint ceil_paint;
    public Paint slider_paint;
    public int t;
    public float slider_l;
    public int score;
    public float ball_x;
    public float ball_y;
    public float ball_rad;
    public float slope;
    public float time;
    public int x_dir;
    public int y_dir;
    public int change; // to ensure that the ball bounces
    public float vel_dist;
    public int vel_change;
    public static final String TAG = "Please!";
    public ballThread b_thread;
    public float vel;
    public float vel_ball;
    public float vel_ball_y;
    public int g_end;
    public int con;
    public int con_ball;

    public interface GameButtonClickListener{
         void OnGamePower_Finish(int score_end, int opt);
    }

    public  GameButtonClickListener buttonClickListener;

    public CustomView(Context context) {
        super(context);
        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set){

        ceiling = new RectF();
        slider = new RectF();
        ceil_paint = new Paint();
        slider_paint = new Paint();
        ceil_paint.setColor(Color.WHITE);
        slider_paint.setColor(Color.WHITE);
        ball_rad = 0;
        ball_y = 0;
        ball_x = 0;
        t = 0;
        time = 10;
        score = 0;
        change = 0; // direction up or down
        vel_dist = 0.2f;
        vel_change = 0;
        vel = 0;
        vel_ball_y = 0;
        vel_ball = 0;
        g_end = 0;
        con = 1;
        con_ball = 1;
    }

    public void setGameButtonListener(GameButtonClickListener listener){
        this.buttonClickListener = listener;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        float x = getWidth(), y = getHeight();

        ceiling.top = y/8;
        ceiling.left = 0;
        ceiling.right = x;
        ceiling.bottom = 7*ceiling.top/6;

        if(t == 0){

            slider.left = 5*x/13;
            slider.right = 8*x/13;
            slider.top = 43*y/45;
            slider.bottom = 44*y/45;
            slider_l = 3*x/13;
            vel = y/400;
            ballThread b_thread = new ballThread();
            b_thread.start();
        }

        canvas.drawRect(ceiling, ceil_paint);
        canvas.drawRect(slider, slider_paint);

        ceil_paint.setTextSize(getHeight()/16);
        canvas.drawText("Score: "+score, getHeight()/8, getHeight()/12, ceil_paint);
        canvas.drawCircle(ball_x, ball_y, ball_rad, ceil_paint);

        if(score >= 20 && con == 1){
            con = 0;
            buttonClickListener.OnGamePower_Finish(-1, 1);
        }
        if(g_end == 1){
            buttonClickListener.OnGamePower_Finish(score, 0);
         }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float x = event.getX();
                float y = event.getY();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                float x = event.getX();
                float y = event.getY();

                //touched
                if(ball_y < slider.top) {
                    if (x >= slider.left + slider_l/2) {
                        if (slider.right >= getWidth()) {
                            return true;
                        } else {
                            slider.left += getWidth() / 100;
                            slider.right = slider_l + slider.left;
                        }
                    }
                    if (x < slider.left + slider_l/2) {
                        if (slider.left <= 0) {
                            return true;
                        }
                        else {
                            slider.left -= getWidth() / 100;
                            slider.right = slider_l + slider.left;
                        }
                    }

                    postInvalidate();
                }
                return value;
            }
        }
        return value;
    }

    class ballThread extends Thread{
        public CustomView mcustview;

        @Override
        public void run() {
            while (ball_y < slider.top) {
                if (t == 0) {
                    Random Rand = new Random();
                    float[] num = {1.4f, 1.1f, 2, 1.4f, 2};
                    ball_x = getWidth() / 2;
                    ball_y = getHeight() / 4;
                    ball_rad = getHeight() / 96;
                    slope = num[Rand.nextInt(5)];
                    t = 1;
                    x_dir = (int)pow(-1, Rand.nextInt(2));
                    y_dir = 1;
                    vel_ball = getHeight()/(400*slope);
                    vel_ball_y = (float) pow(abs(pow((getHeight()/400.0f),2) - pow(vel_ball,2)),0.5f);
                }

                if(score >= 20 && con_ball == 1 ){
                    con_ball = 0;
                    try {
                        sleep(3000, 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(ball_y >= slider.top - (float)(getHeight()*(1/380 + 1.0f/50))){
                    if(vel_change == 0) {
                        vel_dist = slider.left;
                        vel_change = 1;
                       // Log.d(TAG, "oh"+(int)vel_dist);
                    }
                }
                if (ball_y <= ball_rad + ceiling.bottom) {
                    if(con == 0){
                        slider_paint.setColor(Color.WHITE);
                    }
                    if(change == 1) {
                        y_dir *= -1;
                        change = 0;
                        score++;
                        vel_change = 0;
                        vel = 0;
                    }
                }
                if (ball_x + ball_rad >= getWidth() || ball_x <= ball_rad) {
                    x_dir *= -1;
                }
                if (ball_x >= slider.left - ball_rad && ball_x <= slider.right + ball_rad) {
                    if (ball_y + ball_rad >= slider.top) {
                        if(change == 0) {
                            y_dir *= -1;
                            change = 1;
                            vel_dist = slider.left - vel_dist;
                            vel = vel_dist*5/getWidth();
                            vel_ball = abs(x_dir*vel_ball + vel);

                         //   Log.d(TAG, "" + vel_ball);
                        }
                    }
                }
                ball_x = ball_x + x_dir*vel_ball;
                ball_y = ball_y + y_dir * vel_ball_y;

                try {
                    sleep((long) (time), 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                t++;
                if (t % 2000 == 0) {
                    if(time >= 7) {
                        time -= 0.45;
                    }
                    else{
                        time -=0.1;
                    }
                }
                postInvalidate();
            }
            while(ball_y-ball_rad <= getHeight()) {
                ball_x = ball_x + x_dir * vel_ball;
                ball_y = ball_y + y_dir * vel_ball_y;

                try {
                    sleep((long)(time), 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
            //restart.setVisibility(View.VISIBLE);
                g_end = 1;
            postInvalidate();

        }
    }
}


/*
public class CustomView extends View {
    private static final int square_size = 150;
    private RectF mrect;
    private Paint mpaint;
    private Paint mpaintcir;
    private float mcircleX;
    private float mcircleY;
    private float mradius;
    private Bitmap mImage;

    // there are 4 constructors
    public CustomView(Context context) {
        super(context);

        init(null);    // bcos, there are no attribute Set
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs); // bcos, there is a  attribute Set
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs); // bcos, there is a  attribute Set
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs); // bcos, there is a  attribute Set
    }

    private void init(@Nullable AttributeSet set) { // function to initialize class member variables
        mrect = new RectF(); // rectf class can help in setting float coordinates
        mpaint = new Paint(Paint.ANTI_ALIAS_FLAG); // read about anti-aliasing
        mpaint.setColor(Color.GREEN);
        mradius = 100f;
        mpaintcir = new Paint();
        mpaintcir.setAntiAlias(true);
        mpaintcir.setColor(Color.parseColor("#00ccff"));

        mImage = BitmapFactory.decodeResource(getResources(), R.drawable.anime);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int padding = 50;
                mImage = getResizedBitmap(mImage, getWidth()-padding, getHeight()-padding); // user function
            }
        });
    }


    public void swap_color() {
        mpaint.setColor(mpaint.getColor() == Color.GREEN ? Color.RED : Color.GREEN);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawColor(Color.RED); try for yourself
        mrect.left = 10;
        mrect.top = 10;
        mrect.right = mrect.left + square_size;
        mrect.bottom = mrect.top + square_size;

        /*float cx,cy;
        float radius = 80f;
        cx = getWidth() - radius - 50f; //width of customview or in this case canvas itelf
        cy = mrect.top + (mrect.height())/2; */
/*
        if (mcircleX == 0f || mcircleY == 0f) {
            mcircleX = getWidth() / 2;
            mcircleY = getHeight() / 2;
        }

        int image_startx = (getWidth() - mImage.getWidth())/2; // starting point of image
        int image_starty = (getHeight() - mImage.getHeight())/2;

        canvas.drawBitmap(mImage, image_startx, image_starty, null);
        //canvas.drawCircle(mcircleX, mcircleY, mradius, mpaintcir);
        //canvas.drawRect(mrect, mpaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float x = event.getX();
                float y = event.getY();

                if(mrect.left < x && mrect.right > x) {
                     if (mrect.top < y && mrect.bottom > y) {
                         mradius += 5f;
                         postInvalidate();
                     }
                 }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                float x = event.getX();
                float y = event.getY();
                double dx = Math.pow(x - mcircleX, 2);
                double dy = Math.pow(y - mcircleY, 2);
                double dr = Math.pow(mradius, 2);

                if (dx + dy < dr) {
                    //touched
                    mcircleX = x;
                    mcircleY = y;

                    postInvalidate();
                    return true;
                }
                return value;
            }
        }
        return value;
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int reqw, int reqh)
    {
        Matrix matrix = new Matrix();
        RectF src = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF dest = new RectF(0, 0, reqw, reqh);

        matrix.setRectToRect(src,dest,Matrix.ScaleToFit.CENTER);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,true);
    }

}
*/



