package com.example.tt.connectingwifia;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    EditText edit;
    TextView textView;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("ddddddddddddddddddddddddddddddddddddddddddddddddd");
        edit = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickme(v);
            }
        });
    }
    public void clickme(View v){
      ConnectivityManager connMng =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo networkInfo = connMng.getActiveNetworkInfo();
        String urlText = edit.getText().toString();
        if (networkInfo!=null && networkInfo.isConnected()) {
           new CallWebPage().execute(urlText);
            Log.d("TAG", "ket noi thanh cong");
        }else{
            textView.setText("Ko cos mang dc ket noi");
        }
    }
    private class CallWebPage  extends AsyncTask<String, Void,String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                return downloadUrl(params[0]);
            }catch(IOException e ){
                return "Url ko dung";
            }
           
        }

        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
        }
        
        public String downloadUrl(String s) throws IOException{
            InputStream is = null;
            int len = 50;
            try{
                URL mUrl = new URL(s);

                HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();

                conn.setConnectTimeout(10000);
                conn.setReadTimeout(15000);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.connect();

                int rep = conn.getResponseCode();
                is = conn.getInputStream();
                System.out.println(rep);
                Log.d("TAG","The response is: "+ rep);
                String convertString = readIt(is,len);
                return convertString;

            }finally {
                if (is!=null) {
                    is.close();
                }
            }
            
        }
        public String readIt(InputStream is, int len)throws  IOException{
            Reader reader = new InputStreamReader(is,"UTF-8");
            char[] bytess = new char[len];
            reader.read(bytess);
            return new String (bytess);

        }
    }
}
