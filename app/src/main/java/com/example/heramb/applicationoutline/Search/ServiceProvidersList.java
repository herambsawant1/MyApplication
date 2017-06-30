package com.example.heramb.applicationoutline.Search;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.heramb.applicationoutline.R;

import java.util.List;

/**
 * Created by heram on 6/29/2017.
 */

public class ServiceProvidersList extends ArrayAdapter<SearchResultItems> {

    private Activity context;
    private List<SearchResultItems> resultList;

    public ServiceProvidersList(Activity context, List<SearchResultItems> ServiceProviderList){
        super(context,R.layout.row_layout_search, ServiceProviderList);
        this.context = context;
        this.resultList = ServiceProviderList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.row_layout_search, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.rowUserName);
        TextView textViewService = (TextView) listViewItem.findViewById(R.id.rowService);
        TextView textViewLocation = (TextView) listViewItem.findViewById(R.id.rowLocation);
        RatingBar ratingViewRating = (RatingBar) listViewItem.findViewById(R.id.rowReviewRating);

        SearchResultItems results = resultList.get(position);

        textViewName.setText(results.getUsername());
        textViewService.setText(results.getService());
        textViewLocation.setText(results.getLocation());
        ratingViewRating.setRating(results.getRating());

        return listViewItem;
    }
}
