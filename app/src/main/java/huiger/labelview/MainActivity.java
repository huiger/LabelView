package huiger.labelview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import huiger.lib.LibelView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LibelView libelView1 = (LibelView) findViewById(R.id.libelView1);
        libelView1.setText("huiGer code");

        LibelView libelView2 = (LibelView) findViewById(R.id.libelView2);
        libelView2.setText("huiGer code", "哈哈哈哈");
        libelView2.setmCircleX(100f);

        LibelView libelView3 = (LibelView) findViewById(R.id.libelView3);
        libelView3.setText("huiGer code", "哈哈哈哈", "测试一下测试一下");

        LibelView libelView4 = (LibelView) findViewById(R.id.libelView4);
        libelView4.setText("huiGer code", "哈哈哈哈", "活动一下");

        LibelView libelView5 = (LibelView) findViewById(R.id.libelView5);
        libelView5.setText("huiGer code", "hello girl", "hahahahah");
    }
}
