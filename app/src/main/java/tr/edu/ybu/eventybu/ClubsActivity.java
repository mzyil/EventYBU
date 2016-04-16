package tr.edu.ybu.eventybu;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ClubsActivity extends ActionBarActivity implements HandleJson.ParseMethod {

    private JSONArray clubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs);
        Log.i("tr.edu.ybu.eventybu", "Parsing Start");
        HandleJson clubsJson = new HandleJson("http://eventapi.biltek.club/kulup", this);
        clubsJson.fetchJSON();
        while (clubsJson.parsingComplete) {
            try {
                Thread.sleep(10);
                Log.i("tr.edu.ybu.eventybu", "Parsing check");
            } catch (InterruptedException e) {
            }
        }
        Log.i("tr.edu.ybu.eventybu", "Parsing complete");
        ListView clubsList = (ListView) findViewById(R.id.clubsList);
        ListAdapter clubsListAdapter = new ClubsListAdapter(getApplicationContext(), R.layout.clubs_row_view, clubs);
        clubsList.setAdapter(clubsListAdapter);


        //clubsList.setAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clubs, menu);
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

    @Override
    public void readAndParseJSON(String in) throws JSONException {
        JSONArray clubs = new JSONArray(in);
        this.clubs = clubs;
    }
    static class viewHolder{
        TextView ClubName;
        TextView ClubDesc;
    }
    public class ClubsListAdapter extends ArrayAdapter<JSONObject> {
        Context context;
        JSONArray values;

        public ClubsListAdapter(Context context, int resource, JSONArray values) {
            super(context, resource);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolder holder;
            if(convertView==null) {
               convertView = inflater.inflate(R.layout.clubs_row_view, parent, false);
                holder = new viewHolder();
                holder.ClubName = (TextView) convertView.findViewById(R.id.clubName);
                holder.ClubDesc = (TextView) convertView.findViewById(R.id.clubDesc);
                convertView.setTag(holder);
            }
            else{
                holder=(viewHolder)convertView.getTag();
            }
            try {
                holder.ClubName.setText(values.getJSONObject(position).getString("kulup_ad"));
                holder.ClubDesc.setText(values.getJSONObject(position).getString("kulup_aciklama"));
                Log.i("tr.edu.ybu.eventybu", values.getJSONObject(position).getString("kulup_ad") + ": " + values.getJSONObject(position).getString("kulup_aciklama"));
                holder.ClubDesc.setMovementMethod(new ScrollingMovementMethod());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return values.length();
        }
    }
}
