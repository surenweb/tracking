package fastLibrary;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apflocation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 3/15/2016.
 */
public class AdapterSpinner extends ArrayAdapter<FastKeyValue> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (FastKeyValue)
    private List<FastKeyValue> values;

    public AdapterSpinner(Context context, int textViewResourceId,
                          List<FastKeyValue> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
        return values.size();
    }

    public FastKeyValue getItem(int position){
        return values.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    //## Custom views

     /*
        @Override
        public View getView(int position, View convertView, ViewGroup prnt) {

            if(convertView == null)
                convertView = View.inflate(getContext(),
                        R.layout.layout_spinner_item,
                        null);
            TextView tvText1 = (TextView)convertView.findViewById(R.id.spin_item_id);
            //TextView tvText2 = (TextView)convertView.findViewById(android.R.id.text2);
            tvText1.setText(values.get(position).value);
            //tvText2.setText(getItem(position).distance);
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup prnt) {
            if(convertView == null)
                convertView = View.inflate(getContext(),
                        R.layout.layout_spinner_dropdown_item,
                        null);
            TextView tvText1 = (TextView)convertView.findViewById(R.id.spin_dropdown_id);
            //TextView tvText2 = (TextView)convertView.findViewById(android.R.id.text2);
            tvText1.setText(values.get(position).value);
            //tvText2.setText(getItem(position).distance);
            return convertView;
        }
    */

}
