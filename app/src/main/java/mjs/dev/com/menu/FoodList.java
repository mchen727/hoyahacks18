package mjs.dev.com.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    private List<String> data;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        data = new ArrayList<String>();

        Intent intent = getIntent();
        String [] message = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);

        for(String s: message){
            data.add(s);
        }

        list = (ListView)findViewById(R.id.listview);

        createListView();
    }

    private void createListView(){


        list.setAdapter(new ArrayAdapter<String>(FoodList.this, android.R.layout.simple_list_item_1,data));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                //args2 is the listViews Selected index
            }
        });



    }

}
