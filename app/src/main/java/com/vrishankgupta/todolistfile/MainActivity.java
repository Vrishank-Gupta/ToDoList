package com.vrishankgupta.todolistfile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.vrishankgupta.todolistfile.Model.listClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity{
    public static final String TAG = "Vrishank";

    ArrayList<listClass> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView lv = findViewById(R.id.lv);
        final Button addButn = findViewById(R.id.addButn);
        final EditText etnew = findViewById(R.id.etnew);

        int perm = ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE
                );

        if(perm== PackageManager.PERMISSION_GRANTED)
        {
            arr = (ArrayList<listClass>) readFile();
        }

        else
        {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    45
                    );
        }

        final CustomAdapter adapter = new CustomAdapter();
        lv.setAdapter(adapter);

        addButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etnew.getText() == null || etnew.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Some Data",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listClass task = new listClass(etnew.getText().toString(), false);
                    arr.add(task);
                    etnew.setText("");
                    lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }

    void writeFile(ArrayList<listClass> task)
    {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File taskFile = new File(sdCard, "taskFile.txt");
            FileOutputStream fout = new FileOutputStream(taskFile, false);
            ObjectOutputStream objOut = new ObjectOutputStream(fout);
            objOut.writeObject(task);
        } catch (IOException e) {
            Log.e(TAG, "writeFile: ");
        }
    }

    Object readFile()
    {
        ArrayList<listClass> tasks = new ArrayList<>();
        boolean cont = true;
          try {
              File sdCard = Environment.getExternalStorageDirectory();
              File taskFile = new File(sdCard, "taskFile.txt");
              FileInputStream fin = new FileInputStream(taskFile);
              ObjectInputStream objIn = new ObjectInputStream(fin);

              while (cont) {
                  Object t = objIn.readObject();

                  if (t != null) {
                      return (Object) t;
                  } else cont = false;
              }
          } catch (Exception e) {
              Log.e(TAG, "readFile");
          }
        return (Object) new ArrayList<listClass>();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 45) { //read request
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                arr = (ArrayList<listClass>) readFile();
            } else {
                Toast.makeText(this, "Reading file required this permission", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this).setMessage("Required Permission, Please allow").setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},44);
                    }
                }).setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"Sigh!I tried",Toast.LENGTH_SHORT).show();
                    }
                }).create().show();

            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        int perm = ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );

        if(perm == PackageManager.PERMISSION_GRANTED)
        {
             writeFile(arr);

        } else{
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    44
            );
        }

    }



    class CustomAdapter extends  BaseAdapter{
        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public listClass getItem(int position) {
            return arr.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
            {
                LayoutInflater li = getLayoutInflater();
                convertView = li.inflate(R.layout.detail,parent,false);

            }

            TextView listTv = convertView.findViewById(R.id.listtv);
            final CheckBox activeCb = convertView.findViewById(R.id.activeCb);
            final ImageButton delCb = convertView.findViewById(R.id.delCb);

            final listClass task = getItem(position);

            if(task != null) {
                listTv.setText(task.getTask());
                activeCb.setChecked(task.isActive());
            }

            activeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    task.setActive(activeCb.isChecked());
                    Toast.makeText(getApplicationContext(),task.getTask() + " is " +task.isActive(),Toast.LENGTH_SHORT).show();
                }
            });

            delCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arr.remove(task);
                    notifyDataSetChanged();
                    new CustomAdapter();
                    Toast.makeText(getApplicationContext(),arr.size()+" elements left!",Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;

        }
    }
}