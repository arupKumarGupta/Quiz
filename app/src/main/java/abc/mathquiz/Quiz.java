package abc.mathquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


public class Quiz extends AppCompatActivity {

    Toolbar t;
    TextView ques, res;
    double correctAns;
    Button[] but = new Button[4];
    double a,b;
    Random r;
    int incorrect=0;
    int correct=0;
    int quesCount = 1;
    String[] ops = {"+","-","/","*","%"};
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        t = (Toolbar)findViewById(R.id.toolbar);
        t.setTitle("Quiz");
        setSupportActionBar(t);
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        ques = (TextView) findViewById(R.id.ques);
        but[0] = (Button)findViewById(R.id.b1);
        but[1] = (Button)findViewById(R.id.b2);
        but[2] = (Button)findViewById(R.id.b3);
        but[3] = (Button)findViewById(R.id.b4);
        r = new Random();
        generate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void generate() {

        a = (double)r.nextInt(1000)+1;
        b = (double)r.nextInt(1000)+1;
        int index = r.nextInt(ops.length);
        switch (index){
            case 0:
                correctAns = a+b;
                break;
            case 1:
                correctAns = a-b;
                break;
            case 2:
                correctAns = a/b;
                break;
            case 3:
                correctAns = a*b;
                break;
            case 4:
                correctAns = a%b;
                break;


        }

        ques.setText(a +" "+ ops[index] +" "+ b);

        double[] options = generateUnique();
        Set<Integer> used = new HashSet<Integer>();
        for(int i = 0; i < options.length; i++){
            int ind = (r.nextInt(4)+i)%4;

            while(used.contains(new Integer(""+ind)))
            {
                ind = (r.nextInt(4)+i)%4;

            }
            but[i].setText(options[ind]+"");
            used.add(ind);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private double[] generateUnique() {

        double[] ops = new double[4];
        ops[0] = correctAns;
        for(int i = 1; i < 4; i++){
            double x = ThreadLocalRandom.current().nextDouble(1, 1000);
            while(!safe(ops,x))
                x = ThreadLocalRandom.current().nextDouble(1, 1000);;
                ops[i] = x;
        }
        return ops;

    }

    private boolean safe(double[] ops, double x) {

        for(double t: ops){
            if(String.valueOf(t) == String.valueOf(x))
                return false;
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void checkAns(View view) {
        double value=0.0;
        switch(view.getId()){
            case R.id.b1:
                value = Double.valueOf(but[0].getText().toString());
                break;
            case R.id.b2:
                value = Double.valueOf(but[1].getText().toString());
                break;
            case R.id.b3:
                value = Double.valueOf(but[2].getText().toString());
                break;
            case R.id.b4:
                value = Double.valueOf(but[3].getText().toString());
                break;
        }

        if(correct(value))
            correct++;
        else
            incorrect++;

        if (quesCount < 10)
            generate();
        else{
            new AlertDialog.Builder(this)
                    .setIcon(null).setTitle("Result")
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setPositiveButton("Replay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    quesCount=1;
                    correct = incorrect = 0;
                    generate();
                }
            }).setCancelable(false)
            .setMessage("Correct = "+correct+"\nIncorrect = "+incorrect+"\n--end--").show();
        }
        quesCount++;
    }


    private boolean correct(double val) {

        return correctAns == val;
    }
}
