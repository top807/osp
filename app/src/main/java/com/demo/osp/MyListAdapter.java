package com.demo.osp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<DataModel> /*BaseAdapter*/ {

    //    String[] data;
    private static LayoutInflater inflater;
    Context context;
    private ArrayList<DataModel> dataSet;

    //    public MyListAdapter(Context context, String[] data) {
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
    private View.OnClickListener likeButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent();
            final int position = listView.getPositionForView(parentRow);
        }
    };

    public MyListAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.list_item, data);
        this.dataSet = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


//    @Override
//    public int getCount() {
//        return data.length;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return data[position];
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        final DataModel currentDataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final MyViewHolder viewHolder; // view lookup cache stored in tag
        final View result;

        if (convertView == null) {

            viewHolder = new MyViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            // 把拿到的 textView 設定進 view holder
            viewHolder.txt_dishname = convertView.findViewById(R.id.model_txt_dishname);
            viewHolder.txt_price = convertView.findViewById(R.id.model_txt_price);
            viewHolder.txt_stock = convertView.findViewById(R.id.model_txt_stock);
            viewHolder.btn_inc = convertView.findViewById(R.id.model_btn_inc);
            viewHolder.txt_order_number = convertView.findViewById(R.id.model_txt_order);
            viewHolder.btn_dsc = convertView.findViewById(R.id.model_btn_dsc);
            viewHolder.i = 0;
            result = convertView;
            convertView.setTag(viewHolder);

        } else {
//            viewHolder = convertView.setTag(viewHolder);
            viewHolder = (MyViewHolder) convertView.getTag();
            result = convertView;
        }

//        viewHolder.txt_dishname.setText(data[position]);
//        viewHolder.txt_price.setText(data[position]);
//        viewHolder.txt_order_number.setText(data[position]);

        viewHolder.txt_dishname.setText(currentDataModel.getDishname());
        viewHolder.txt_price.setText(currentDataModel.getPrice());
        viewHolder.txt_stock.setText(currentDataModel.getStock());

        viewHolder.txt_dishname.setTag(position + "dishname");
        viewHolder.txt_price.setTag(position + "price");
        viewHolder.txt_stock.setTag(position + "stock");
        viewHolder.txt_order_number.setTag(position + "order");
        viewHolder.btn_inc.setTag(position + "inc");
        viewHolder.btn_dsc.setTag(position + "dsc");

//        //TODO Click To Increase Order Number
//        viewHolder.btn_inc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int quantity = Integer.parseInt(currentDataModel.getStock());
//
//                Log.i("POSITION", String.valueOf(position));
//
//                if (viewHolder.i < quantity && viewHolder.i >= 0) {
//                    ++viewHolder.i;
//                    viewHolder.txt_order_number.setText("" + viewHolder.i);
//                } else {
//                    viewHolder.txt_order_number.setText(quantity + "");
//                }
//            }
//        });
//
//        //TODO Click To Decrease Order Number
//        viewHolder.btn_dsc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (viewHolder.i <= 0) {
//                    viewHolder.txt_order_number.setText(0 + "");
//                } else {
//                    --viewHolder.i;
//                    viewHolder.txt_order_number.setText("" + viewHolder.i);
//                }
//            }
//        });

//        //TODO Click To Show Picture
//        viewHolder.txt_dishname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("黑人問號", "喵喵喵？ " + position + " ");
//            }
//        });

        return convertView;
    }

    // View lookup cache
    static class MyViewHolder {
        TextView txt_dishname;
        TextView txt_price;
        TextView txt_order_number;
        TextView txt_stock;
        EditText user_key_in;
        Button btn_inc;
        Button btn_dsc;
        Integer i;
    }
}
