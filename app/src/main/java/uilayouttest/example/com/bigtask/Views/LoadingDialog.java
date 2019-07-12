package uilayouttest.example.com.bigtask.Views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import uilayouttest.example.com.bigtask.R;

/**
 * Created by 朱慧 on 18-10-9.
 */

public class LoadingDialog extends Dialog {

    private TextView tv;
    private String content;

    /**
     * style很关键
     */
    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        tv = (TextView) findViewById(R.id.tv_dialog_loading);
        tv.setText(content);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.linearLayout_loading_dialog);
        linearLayout.getBackground().setAlpha(210);
    }

    public void setTextContent(String content) {
        this.content = content;
    }


}
