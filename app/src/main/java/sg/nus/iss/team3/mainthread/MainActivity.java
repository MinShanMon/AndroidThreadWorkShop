package sg.nus.iss.team3.mainthread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Thread backgroundthread;
    Button btn;
    TextView result;
    int Mid;
    int Tid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        result = findViewById(R.id.result);
        btn.setOnClickListener(this);

        //each thread has its own unique id
        Mid = android.os.Process.myTid();


        //call myrunnable
        new Thread(new MyRunnable(10, "Add")).start();
    }



    //runnable class [we can subclass Runnable to pass in arguments to the run method]
    class MyRunnable implements Runnable{
        protected int k;
        protected String s;

        MyRunnable(int k, String s){
            this.k = k;
            this.s = s;
        }

        @Override
        public void run(){
            System.out.println("k: " + k+ ", s:" + s);
        }
    }

    @Override
    public void onClick(View v){
        if(backgroundthread != null){
            backgroundthread.interrupt();

        }
        int id = v.getId();
        if(id == R.id.btn){
            //background threads create
            new Thread(new Runnable(){
                long sum = 0;

               @Override
               public void run(){
                   //every thread has its own unique id
                   Tid = android.os.Process.myTid();
                   for(int i = 0; i<Integer.MAX_VALUE; i++){
                       sum+=i;
                       //handle interruption
                       if(Thread.interrupted()){
                           backgroundthread = null;
                           return;
                       }
                   }
                   backgroundthread = null;

                   //set to main thread value
                   runOnUiThread(new Runnable(){
                       @Override
                       public void run(){

                           //post delay 3 second
                           try{
//                               Thread.sleep(10000);
                           }
                           catch (Exception e){

                           }
                            result.setText(String.valueOf(sum));
                            btn.setText("Start");
                       }
                   });




               }
            }).start();//start the thread


//            post delay 3 seconds
            result.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            result.setText(Integer.toString(Mid));
                            btn.setText("Stop");
                        }
                    }, 1000
            );

            //

            //main thread
//            result.setText("Computing");
//            btn.setText("Stop");
        }
    }

}