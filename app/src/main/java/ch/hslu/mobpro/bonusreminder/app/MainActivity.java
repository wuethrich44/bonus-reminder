package ch.hslu.mobpro.bonusreminder.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends Activity {
    Set<String> boni = new HashSet<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDefaultValues();

        Button newBonusButton = (Button) findViewById(R.id.new_bonus_button);
        newBonusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewBonusActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        boni = getBoni();

        List<String> list = new ArrayList<String>(boni);
        ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };

        ListView listView = (ListView)findViewById(R.id.boni_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedBonus = new ArrayList<String>(boni).get(i);
                Intent intent = new Intent(MainActivity.this, EditBonusActivity.class);
                intent.putExtra("selected_bonus",selectedBonus);
                startActivity(intent);
            }
        });
    }

    private void setDefaultValues(){
        boni.add("STUcard");
        boni.add("McDonalds Gutschein");
        saveBoni(boni);
    }

    private void saveBoni(Set<String> boni){
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("boni", boni);
        editor.apply();
    }

    private Set<String> getBoni(){
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        Set<String> boni = new HashSet<String>();
        boni = prefs.getStringSet("boni", boni);
        return boni;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
