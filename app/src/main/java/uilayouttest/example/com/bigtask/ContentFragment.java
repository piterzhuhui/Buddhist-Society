package uilayouttest.example.com.bigtask;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 朱慧 on 2019/1/26.
 */

public class ContentFragment extends Fragment {
    public static final String  SELECTED_ITEM = "selected_item" ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bd = getArguments( ) ;

        View view = inflater.inflate(R.layout.fragment_content, null) ;
        ( (TextView ) view ).setText(bd.getString(SELECTED_ITEM)) ;
        return view ;
    }
}
