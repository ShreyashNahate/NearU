package com.example.nearuseller;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class zoom extends AppCompatActivity {

    private ImageView imageView;
    private Matrix matrix = new Matrix();
    private float scale = 1f;
    private float prevScale = 1f;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        imageView = findViewById(R.id.zoomImageView);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            Glide.with(this).load(bundle.getString("Image")).into(imageView);
        }
        imageView.setScaleType(ImageView.ScaleType.MATRIX);

        imageView.setOnTouchListener((v, event) -> {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    start.set(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        midPoint(mid, event);
                        prevScale = scale;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 1) {
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                        start.set(event.getX(), event.getY());
                    } else if (event.getPointerCount() == 2) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            scale = (newDist / oldDist) * prevScale;
                            float maxScale = 3.0f;
                            float minScale = 0.1f;
                            scale = Math.max(minScale, Math.min(scale, maxScale));
                            matrix.setScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;
            }
            imageView.setImageMatrix(matrix);
            return true;
        });
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        point.set(x, y);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}

