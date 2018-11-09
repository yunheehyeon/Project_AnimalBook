package edu.android.teamproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GmapListAdapter extends BaseAdapter {

    public ArrayList<GmapListItem> listViewItemList = new ArrayList<GmapListItem>();

    public GmapListAdapter(ArrayList<GmapListItem> itemList){
        if (itemList == null){
            listViewItemList = new ArrayList<GmapListItem>();
        }else {
            listViewItemList = itemList;
        }
    }


    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.gmap_list_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView addr = (TextView) convertView.findViewById(R.id.addr);
        TextView lsind_Type = (TextView) convertView.findViewById(R.id.lsind_Type);
        TextView tel = (TextView) convertView.findViewById(R.id.tel);
        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        GmapListItem listViewItem = listViewItemList.get(position);

        name.setText(listViewItem.getName());
        addr.setText(listViewItem.getAddr());
        lsind_Type.setText(listViewItem.getLsind_Type());
        tel.setText(listViewItem.getTel());

        return convertView;
    }

    public void addItem(String name, String addr_old, String addr, String lsind_Type, String tel, double mapx, double mapy){
        Log.i("edu.android.teamproject", name + " " + addr);
        GmapListItem item = new GmapListItem();
        item.setName(name);
        item.setAddr(addr);
        item.setAddr_old(addr_old);
        item.setLsind_Type(lsind_Type);
        item.setTel(tel);
        item.setMapx(mapx);
        item.setMapy(mapy);
        listViewItemList.add(item);
    }


}
