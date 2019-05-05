package ntk.ambrose.landscapist.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import ntk.ambrose.landscapist.R;
import ntk.ambrose.landscapist.models.ImageElement;

import java.util.ArrayList;


public class ListImageAdapter extends ArrayAdapter<ImageElement> {

    private ArrayList<ImageElement> items;

    public ListImageAdapter(Context context, int resource, ArrayList<ImageElement> objects) {
        super(context, resource, objects);
        this.items = objects;
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = View.inflate(getContext(), R.layout.list_image,null);
        }

        ImageView imageView = view.findViewById(R.id.itemImage);
        CheckBox checkSelected = view.findViewById(R.id.checkSelected);
        checkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                items.get(position).setSelected(b);
            }
        });
        final ImageElement element = items.get(position);
        if(element!=null){
            imageView.setImageBitmap(element.getData());
            checkSelected.setChecked(element.isSelected());

        }

        return view;
    }

    public int countSelected(){
        int count = 0;
        for(int i=0;i<items.size();i++){
            if(items.get(i).isSelected()){
                count++;
            }
        }
        return count;
    }
}
