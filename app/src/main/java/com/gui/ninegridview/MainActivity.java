package com.gui.ninegridview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gui.ninegrideview.LGNineGrideView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LGNineGrideView LGNineGrideView;
    private List<String> urls = new ArrayList<>();
    private int countIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 9; ++i){
            urls.add(TestData.urlsArray[i]);
        }

        LGNineGrideView = (LGNineGrideView)findViewById(R.id.grideView);
        LGNineGrideView.setImageDatas(urls);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urls.clear();
                ++countIndex;
                if(countIndex >= 10){
                    countIndex = 1;
                }
                for (int i = 0; i < countIndex; ++i){
                    urls.add(TestData.urlsArray[i]);
                }
                LGNineGrideView.setImageDatas(urls);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ListViewActivity.class));
            }
        });
    }
}
