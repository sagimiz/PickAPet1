package com.pet.att.pickapet.AuxiliaryClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.pet.att.pickapet.AppActivities.AnimalDetailsActivity;
import com.pet.att.pickapet.HTTP.ImageDetailsDataTask;
import com.pet.att.pickapet.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private AnimalsPics[] mAnimalsPics;
    protected Activity mActivity;
    protected Context mContext;

    public void refreshPics(AnimalsPics[] mNewAnimalsPics) {
        this.mAnimalsPics = mNewAnimalsPics;
        notifyDataSetChanged();
    }

    public CustomAdapter(AnimalsPics[] mAnimalsPics,Activity mActivity) {
        this.mAnimalsPics = mAnimalsPics;
        this.mActivity =mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pet_image_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.setViewActivity(mActivity);
        viewHolder.setAnimalInImageId(mAnimalsPics[position].getAnimalId());
        viewHolder.setImageJson(mAnimalsPics[position].getImageJson());
        viewHolder.getImageView().setImageBitmap(mAnimalsPics[position].getMyImage());
        Log.d(TAG, "Added view on position = " + position + " for animal " + mAnimalsPics[position].getAnimalId());
    }

    @Override
    public int getItemCount() {
        return mAnimalsPics.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private final ImageView imageView;
        private String imageJson=null ;
        private String mAnimalInImageId;
        private Activity mViewActivity=null;

        ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.imageView);
            imageView.setOnClickListener(this);
        }


        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View v) {
            final Context  mContext =v.getContext();
            new ImageDetailsDataTask((AppCompatActivity) mViewActivity, mContext, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(mContext.getString(R.string.dialog_error_text))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                @Override
                public void onTaskCompleted(String result) {
                    String mAnimalPicJson=getImageJson();
                    Intent intent = new Intent (mContext, AnimalDetailsActivity.class);
                    intent.putExtra(mContext.getString(R.string.animal_image_json), mAnimalPicJson);
                    intent.putExtra(mContext.getString(R.string.animals_image_all_data_json), result);
                    mContext.startActivity(intent);
                }

                @Override
                public void onTaskCompleted(Boolean result) {
                    if(result){
                        String mFullDetails = mViewActivity.getIntent().getStringExtra(mContext.getString(R.string.animals_image_all_data_json));
                        onTaskCompleted(mFullDetails);
                    }else{
                        onTaskCompleted();
                    }
                }
            }).execute(mContext.getString(R.string.animals_image_all_data_request),
                    this.getAnimalInImageId(),
                    mContext.getString(R.string.animals_image_all_data_json));

            Log.d(TAG,"Recycle Click on item " + getAdapterPosition());
        }
        private String getAnimalInImageId() {
            return mAnimalInImageId;
        }

        private void setAnimalInImageId(String mAnimalInImageId) {
            this.mAnimalInImageId = mAnimalInImageId;
        }

        private String getImageJson() {
            return imageJson;
        }

        private void setImageJson( String imageJson) {
            this.imageJson = imageJson;
        }

        private void setViewActivity(Activity mViewActivity) {
            this.mViewActivity = mViewActivity;
        }
    }

}
