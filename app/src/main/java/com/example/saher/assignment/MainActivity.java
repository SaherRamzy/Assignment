package com.example.saher.assignment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int SELECTED_PICTURE = 1;
    Button bt_choose;
    ImageView iv_showimage;
    Button dialog_ok;
    Button dialog_cancel;
    EditText dialogcomment;
    Uri uri;
    float x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_choose = (Button) findViewById(R.id.btn_choose);
        iv_showimage = (ImageView) findViewById(R.id.iv_showimage);
        bt_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);
            }
        });
        iv_showimage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //x = v.getX();
                x = event.getY();
                Log.e("X", "VALUE" + x);
                y = event.getY();
                Log.e("y", "VALUE" + y);
                return false;
            }
        });
//        ViewTreeObserver vto = iv_showimage.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                //this.layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                int width = iv_showimage.getMeasuredWidth();
//                Log.e("width","value" + width);
//                int height = iv_showimage.getMeasuredHeight();
//                Log.e("height","value" + height);
//
//            }
//        });
        iv_showimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Your Comment Please !!!");
                dialog.setContentView(R.layout.comment_dialog);
                dialog_ok = (Button) dialog.findViewById(R.id.dialog_ok);
                dialog_cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
                dialogcomment = (EditText) dialog.findViewById(R.id.comment);
                dialog.show();

                dialog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProcessingBitmap();
                        dialog.dismiss();
                    }
                });
                dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECTED_PICTURE:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnindex = cursor.getColumnIndex(projection[0]);
                    String filepath = cursor.getString(columnindex);
                    cursor.close();

                    Bitmap yourselected = BitmapFactory.decodeFile(filepath);
                    Drawable d = new BitmapDrawable(yourselected);

                    iv_showimage.setBackground(d);
                    break;
                }
        }
    }

    public void ProcessingBitmap() {
        Bitmap bm1 ;
        Bitmap newBitmap = null;
        try {
            bm1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

            newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas newCanvas = new Canvas(newBitmap);

            String theComment = dialogcomment.getText().toString();

            Paint paintText = new Paint();
            paintText.setColor(Color.BLUE);
            paintText.setTextSize(50);
            //
            paintText.setFlags(Paint.ANTI_ALIAS_FLAG);

            newCanvas.drawText(theComment,0,theComment.length(), x, y, paintText);
            iv_showimage.setImageBitmap(newBitmap);

            Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();

        } catch (Exception ignored) {}
        //return newBitmap;
    }
}
