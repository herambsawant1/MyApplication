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

import com.example.heramb.applicationoutline.Models.UserInformation;
import com.example.heramb.applicationoutline.R;

import java.util.List;

/**
 * Created by heram on 6/29/2017.
 */

public class ServiceProvidersList extends ArrayAdapter<UserInformation> {

    private Activity context;
    private List<UserInformation> resultList;

    public ServiceProvidersList(Activity context, List<UserInformation> userInformation){
        super(context,R.layout.row_layout_search, userInformation);
        this.context = context;
        this.resultList = userInformation;
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

        UserInformation results = resultList.get(position);

        textViewName.setText(results.getDisplayName());
        textViewService.setText(results.getService());
        textViewLocation.setText(results.getLocation());
        Long rating = ((results.getServiceRating()+results.getExperienceRating())/2);
        ratingViewRating.setRating(rating);

        return listViewItem;
    }
}
