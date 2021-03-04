package com.manoj.phonyhub.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.manoj.phonyhub.R;


public class MVPTestActivity extends AppCompatActivity implements ContractMVP.View {

    TextView heading, subheading, description;
    ProgressBar progressBar;
    Button button;
    ImageView imageView;
    ContractMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_mvp);

        heading = findViewById(R.id.test_mvp_text_heading);
        subheading = findViewById(R.id.test_mvp_text_subHeading);
        description = findViewById(R.id.test_mvp_text_description);
        progressBar = findViewById(R.id.test_mvp_progressBar);
        button = findViewById(R.id.test_mvp_button);
        imageView = findViewById(R.id.test_mvp_imageView);

        // instantiating object of Presenter Interface
        presenter = new PresenterMVP(this, new ModelMVP());

        // operations to be performed when user clicks the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onButtonClick();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    // method to display the Course Detail TextView
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        description.setVisibility(View.INVISIBLE);
    }

    @Override
    // method to hide the Course Detail TextView
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        description.setVisibility(View.VISIBLE);
    }

    @Override
    // method to set random string
    // in the Course Detail TextView
    public void setString(String string) {
        description.setText(string);
    }

}
