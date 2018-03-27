package com.monisha.samples.sloshed.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vazra on 3/25/2018.
 */

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> {

    private List<Contact> values;
    private List<Contact> checkedList = new ArrayList<>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactsRecyclerAdapter(List<Contact> myDataset) {
        values = myDataset;
    }

    public void add(int position, Contact item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = values.get(position).getName();
        holder.txtHeader.setText(name);
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove(position);
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && !checkedList.contains(values.get(position))) {
                    checkedList.add(values.get(position));
                } else if (!b && checkedList.contains(values.get(position))) {
                    checkedList.remove(values.get(position));
                }
            }
        });
        Contact curr = values.get(position);
        if (checkedList.contains(curr)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
       // holder.txtFooter.setText("Footer: " + name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

    public List<Contact> getCheckedList() {
        return checkedList;
    }

    public void setCheckedList(List<Contact> list) {
        this.checkedList = list;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public AppCompatCheckBox checkBox;
        //   public Tex txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = v.findViewById(R.id.firstLine);
            checkBox = v.findViewById(R.id.checkbox);
            // txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }
}
