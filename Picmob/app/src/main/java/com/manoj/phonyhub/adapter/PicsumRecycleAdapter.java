package com.manoj.phonyhub.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.manoj.phonyhub.HelperMethod;
import com.manoj.phonyhub.R;
import com.manoj.phonyhub.activities.FavoritesActivity;
import com.manoj.phonyhub.data.PicsumDataModel;
import com.manoj.phonyhub.fragments.FullscreenDialog;

import java.util.ArrayList;
import java.util.List;

public class PicsumRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<PicsumDataModel> picsumItemList;
    int VIEW_TYPE_LOADING = 0;
    int VIEW_TYPE_ITEM = 1;
    boolean isRotate = false;
    Dialog dialog;
    FloatingActionButton FabAdd, FabSave, FabSet, FabShare;

    public PicsumRecycleAdapter(Context context, List<PicsumDataModel> picsumItemList) {
        this.context = context;
        this.picsumItemList = picsumItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.picsum_image_item, parent, false);
            return new mViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.bottom_item_loading, parent, false);
            return new LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof mViewHolder) {
            populateItems((mViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //return picsumItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        if (picsumItemList.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return picsumItemList.size();
    }

    @SuppressLint({"DefaultLocale", "UseCompatLoadingForDrawables"})
    private void populateItems(mViewHolder holder, int position) {
        Context onBindViewContext = holder.itemView.getContext();
        PicsumDataModel dataModel = picsumItemList.get(position);

        String id = dataModel.getId();
        String author_name = dataModel.getAuthor();
        int width = dataModel.getWidth();
        int height = dataModel.getHeight();
        String image_size = String.format("%dx%d", width, height);
        String thumbnail_url_small = dataModel.getUrl() + "/download?force=true&w=640";
        String thumbnail_url_mini = "https://picsum.photos/id/" + id + "/120/220";
        String thumbnail_url = "https://picsum.photos/id/" + id + "/640/920";
        String download_url = dataModel.getDownload_url();

//        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()
//                .setDuration(2400L)
//                .setBaseAlpha(0.6F)
//                .setHighlightAlpha(0.6F)
//                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
//                .setAutoStart(true)
//                .build();
//        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
//        shimmerDrawable.setShimmer(shimmer);

//        PostProcessor postprocessor = new BlurPostProcessor(50, onBindViewContext);
//        ImageRequest lowResImageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(thumbnail_url_mini))
//                .setPostprocessor(new BlurPostProcessor(50, onBindViewContext))
//                .build();
        DraweeController controller = (DraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(ImageRequest.fromUri(thumbnail_url_mini))
                .setOldController(holder.picsumDraweeImage.getController())
                .build();
//                .setLowResImageRequest(ImageRequest.fromUri(thumbnail_url_mini))
//        Drawable icon = ContextCompat.getDrawable(onBindViewContext, R.drawable.ic_loading_dual_ring_200px);
//        if (icon != null) {
//            DrawableCompat.setTint(icon, ContextCompat.getColor(onBindViewContext, R.color.com_facebook_blue));
//            holder.picsumDraweeImage.getHierarchy().setProgressBarImage(new AutoRotateDrawable(icon, 1000));
//        }
        holder.picsumDraweeImage.getHierarchy().setProgress(20, true);
        holder.picsumDraweeImage.setController(controller);

        holder.picsumSize.setText(image_size);
        holder.picsumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new FullscreenDialog(dataModel);
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("ImageDialogBackStack")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                dialog.show(transaction, "ImageDialog");
            }
        });

        ArrayList<String> preSavedImagesItem = FavoritesActivity.getSavedImagesArrayList(onBindViewContext, "saved_image");
        if (preSavedImagesItem != null && preSavedImagesItem.contains(id)) {
            holder.addToFavoriteIcon.setImageResource(R.drawable.ic_favorite_round_filled_24);
            holder.addToFavoriteIcon.setColorFilter(ContextCompat.getColor(onBindViewContext, R.color.addToFavoriteIconColor));
        }

        holder.addToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(onBindViewContext);
                boolean isFirstSaved = prefs.getBoolean("isFirstSaved", true);
                if (isFirstSaved) {
                    prefs.edit().putBoolean("isFirstSaved", false).apply();
                    ArrayList<String> newSavedImageList = new ArrayList<>();
                    newSavedImageList.add(id);
                    FavoritesActivity.saveImagesArrayList(onBindViewContext, newSavedImageList, "saved_image");
                    holder.addToFavoriteIcon.setImageResource(R.drawable.ic_favorite_round_filled_24);
                    holder.addToFavoriteIcon.setColorFilter(ContextCompat.getColor(onBindViewContext, R.color.addToFavoriteIconColor));
                    HelperMethod.toastLong(id + " Cheer's! You will Added First Favorite Item");
                } else {
                    ArrayList<String> savedImageList = FavoritesActivity.getSavedImagesArrayList(onBindViewContext, "saved_image");
                    if (savedImageList.contains(id)) {
                        savedImageList.remove(id);
                        FavoritesActivity.saveImagesArrayList(onBindViewContext, savedImageList, "saved_image");
                        holder.addToFavoriteIcon.setImageResource(R.drawable.ic_favorite_round_24);
                        holder.addToFavoriteIcon.setColorFilter(ContextCompat.getColor(onBindViewContext, R.color.addToFavoriteIconColor));
                        HelperMethod.toastLong(id + " Removed from Favorites");
                    } else {
                        savedImageList.add(id);
                        FavoritesActivity.saveImagesArrayList(onBindViewContext, savedImageList, "saved_image");
                        holder.addToFavoriteIcon.setImageResource(R.drawable.ic_favorite_round_filled_24);
                        holder.addToFavoriteIcon.setColorFilter(ContextCompat.getColor(onBindViewContext, R.color.addToFavoriteIconColor));
                        HelperMethod.toastLong(id + " Added to Favorites");
                    }
                }
            }
        });

    }

    static class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class mViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        public TextView picsumSize;
        public SimpleDraweeView picsumDraweeImage;
        public ImageView addToFavoriteIcon;
        public RelativeLayout picsumLayout;
        public LinearLayout addToFavorite;

        public mViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            picsumDraweeImage = itemView.findViewById(R.id.picsum_image);
            picsumLayout = itemView.findViewById(R.id.picsum_layout);
            picsumSize = itemView.findViewById(R.id.picsum_size);
            addToFavorite = itemView.findViewById(R.id.add_to_favorite);
            addToFavoriteIcon = itemView.findViewById(R.id.add_to_favorite_icon);
        }

    }

//        Glide.with(onBindViewContext)
//                .load(thumbnail_url_small)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .thumbnail(Glide.with(onBindViewContext).load(thumbnail_url_mini))
//                .placeholder(shimmerDrawable)
//                .into(holder.picsumDraweeImage);
//
//        Glide.with(dialogContext)
//                .asBitmap()
//                .load(thumbnail_url)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
//                        Bitmap blurr = HelperMethod.fastblur(bitmap, 0.3f, 3);
//                        //Drawable draw = new BitmapDrawable(dialogContext.getResources(), blurr);
//                        picsum_full_image_background.setImageBitmap(blurr);
//                        Glide.with(dialogContext)
//                                .load(download_url)
//                                .thumbnail(Glide.with(dialogContext).load(thumbnail_url))
//                                .into(picsum_full_image);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                    }
//                });
//
//        Glide.with(dialogContext).asBitmap().load(thumbnail_url)
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        Log.d("imageblurness", "onResourceReady: Image Failed");
//                        return false;
//                    }
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        Bitmap blurr = HelperMethod.fastblur(resource, 0.3f, 2);
//                        Drawable d = new BitmapDrawable(getResources(), blurr);
//                        Glide.with(AboutActivity.this).load(original_image_url)
//                                .placeholder(d)
//                                .into(aboutImage2);
//                        mShimmerContainer2.stopShimmer();
//                        mShimmerContainer2.setVisibility(View.GONE);
//                        return true;
//                    }
//                }).submit();
//    public void setWallpaper(Context dialogContext) {
//        WallpaperManager wallpaper = WallpaperManager.getInstance(dialogContext.getApplicationContext());
//        if (HelperMethod.checkImageResource(dialogContext, picsum_full_image, R.id.picsum_full_image)) {
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) picsum_full_image.getDrawable();
//            Bitmap bitmap = bitmapDrawable.getBitmap();
//            if (bitmap == null) {
//                HelperMethod.toast("wallpaper set failed");
//            } else {
//                try {
//                    //wallpaper.setResource(R.raw.);
//                    wallpaper.setBitmap(bitmap);
//                    //wallpaper.setBitmap(HelperMethod.drawableToBitmap(dialogContext.getDrawable(R.id.picsum_full_image)));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}
