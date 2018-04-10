package huiger.labelview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import huiger.lib.LabelView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LabelView labelView1 = (LabelView) findViewById(R.id.labelView1);
        labelView1.setText("huiGer code");

        LabelView labelView2 = (LabelView) findViewById(R.id.labelView2);
        labelView2.setText("huiGer code", "哈哈哈哈");
        labelView2.setmCircleX(100f);

        LabelView labelView3 = (LabelView) findViewById(R.id.labelView3);
        labelView3.setText("huiGer code", "哈哈哈哈", "测试一下测试一下");

        LabelView labelView4 = (LabelView) findViewById(R.id.labelView4);
        labelView4.setText("huiGer code", "哈哈哈哈", "活动一下");

        LabelView labelView5 = (LabelView) findViewById(R.id.labelView5);
        labelView5.setText("huiGer code", "hello girl", "hahahahah");
    }
}
