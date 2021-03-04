package com.manoj.phonyhub.fragments;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.manoj.phonyhub.Config;
import com.manoj.phonyhub.DownloadFileBackground;
import com.manoj.phonyhub.HelperMethod;
import com.manoj.phonyhub.R;
import com.manoj.phonyhub.data.PicsumDataModel;

public class FullscreenDialog extends DialogFragment implements View.OnClickListener {

    TextView picsum_full_author, picsum_full_preview_Btn;
    ImageButton picsum_full_back_Btn;
    ImageView picsum_full_image, picsum_full_image_background;
    ImageView SaveBtn, SetBtn, ShareBtn;
    PicsumDataModel dataModel;
    String id, author_name, image_size, thumbnail_url_small, thumbnail_url_mini, thumbnail_url, download_url;
    int width, height;

    public FullscreenDialog(PicsumDataModel dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_wallpaper_view, container, false);

        // intialize the view's
        picsum_full_author = view.findViewById(R.id.picsum_full_author);
        picsum_full_image = view.findViewById(R.id.picsum_full_image);
        picsum_full_image_background = view.findViewById(R.id.picsum_full_image_background);
        picsum_full_preview_Btn = view.findViewById(R.id.picsum_full_preview);
        picsum_full_back_Btn = view.findViewById(R.id.picsum_full_back);
        SaveBtn = view.findViewById(R.id.picsum_fab_save);
        SetBtn = view.findViewById(R.id.picsum_fab_set);
        ShareBtn = view.findViewById(R.id.picsum_fab_share);

        picsum_full_preview_Btn.setOnClickListener(this);
        picsum_full_back_Btn.setOnClickListener(this);
        SaveBtn.setOnClickListener(this);
        SetBtn.setOnClickListener(this);
        ShareBtn.setOnClickListener(this);

        id = dataModel.getId();
        author_name = dataModel.getAuthor();
        width = dataModel.getWidth();
        height = dataModel.getHeight();
        image_size = width + "x" + height;
        thumbnail_url_small = dataModel.getUrl() + "/download?force=true&w=640";
        thumbnail_url_mini = "https://picsum.photos/id/" + id + "/220/440";
        thumbnail_url = "https://picsum.photos/id/" + id + "/720/1280";
        download_url = dataModel.getDownload_url();

//        Postprocessor postprocessor = new BlurPostProcessor(80, getContext());
//        ImageRequest lowResImageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(thumbnail_url_mini))
//                .setPostprocessor(postprocessor)
//                .build();
//        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
//                .setLowResImageRequest(lowResImageRequest)
//                .setImageRequest(ImageRequest.fromUri(thumbnail_url))
//                .setOldController(picsum_full_image_background.getController())
//                .build();
//        Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_loading_reload_200px);
//        if (icon != null) {
//            DrawableCompat.setTint(icon, ContextCompat.getColor(getContext(), R.color.com_facebook_blue));
//            picsum_full_image_background.getHierarchy().setProgressBarImage(new AutoRotateDrawable(icon, 1000));
//        }
//        picsum_full_image_background.setController(controller);

        Glide.with(getActivity())
                .asBitmap()
                .load(thumbnail_url_mini)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap blurBitmap = HelperMethod.fastblur(bitmap, 2.6f, 56);
                        picsum_full_image_background.setImageBitmap(blurBitmap);
                        Glide.with(getActivity())
                                .load(thumbnail_url)
                                .placeholder(new BitmapDrawable(getContext().getResources(), blurBitmap))
                                .into(picsum_full_image);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        picsum_full_author.setText(author_name);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.picsum_fab_save) {
            new DownloadFileBackground(getContext(), download_url).execute();
        } else if (id == R.id.picsum_fab_set) {
            Config.setWallpaper(getContext(), download_url);
        } else if (id == R.id.picsum_fab_share) {
            Config.shareWallpaper(getContext(), download_url);
        } else if (id == R.id.picsum_full_preview) {
            HelperMethod.toast("Wallpaper Preview");
        } else if (id == R.id.picsum_full_back) {
            this.dismiss();
        }

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        //Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("ImageDialog");
        //FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.remove(fragment).addToBackStack(null).commit();
    }


}
