package com.vrishankgupta.todolistfile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.vrishankgupta.todolistfile.Model.listClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import com.vrishankgupta.todolistfile.Model.CourseAdapter;

public class RecyclerActivity extends AppCompatActivity {
    public static final String TAG = "ERROR";

    ArrayList<listClass> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);
        final Button addButn = findViewById(R.id.addButn);
        final EditText etnew = findViewById(R.id.etnew);


        final RecyclerView rv = findViewById(R.id.recycle);

        int perm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(perm == PackageManager.PERMISSION_GRANTED)
        {
            arr = (ArrayList<listClass>) readFile();
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},45);
        }

        final CourseAdapter adapter = new CourseAdapter(this,arr);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        addButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etnew.getText() == null || etnew.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Some Data",Toast.LENGTH_SHORT).show();
                }
                else {
                    listClass task = new listClass(etnew.getText().toString(), false);
                    arr.add(task);
                    etnew.setText("");
                    rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });

    }



    void writeFile(ArrayList<listClass> arr)
    {
        File sdCard = Environment.getExternalStorageDirectory();
        File taskFile = new File(sdCard,"taskFile.txt");
        try {
            FileOutputStream fout = new FileOutputStream(taskFile,false);
            ObjectOutputStream objOut = new ObjectOutputStream(fout);
            objOut.writeObject(arr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Object readFile()
    {
        boolean cont = true;
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File task = new File(sdCard,"taskFile.txt");
            FileInputStream fin = new FileInputStream(task);
            ObjectInputStream objIn = new ObjectInputStream(fin);

            while (cont)
            {
                Object t = objIn.readObject();
                if(t!=null)
                    return t;
                else cont = false;
            }

        }catch (Exception e)
        {
            Log.e(TAG, "readFile: " );
        }

        return  (Object)new ArrayList<listClass>();
    }

    @Override
    protected void onStop() {
        super.onStop();

        int perm = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );

        if(perm == PackageManager.PERMISSION_GRANTED)
        {
            writeFile(arr);

        } else{
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    44
            );
        }

    }
}

