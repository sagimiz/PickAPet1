package com.pet.att.pickapet.AuxiliaryClasses;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.pet.att.pickapet.AppActivities.AnimalDetailsActivity;
import com.pet.att.pickapet.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private static AnimalsPics[] mAnimalsPics;

    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private final ImageView imageView;
        private String imageJson=null ;

        public String getImageJson() {
            return imageJson;
        }

        public void setImageJson( String imageJson) {
            this.imageJson = imageJson;
        }

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            imageView.setOnClickListener(this);
        }

        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Recycle Click on item " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

            String animalJson=this.getImageJson();

            Intent intent = new Intent (v.getContext(), AnimalDetailsActivity.class);
            intent.putExtra("animal_data", animalJson);
            v.getContext().startActivity(intent);
            Log.d(TAG,"Recycle Click on item " + getAdapterPosition());
        }
    }

    public void refreshPics(AnimalsPics[] mNewAnimalsPics) {
        this.mAnimalsPics = mNewAnimalsPics;
        notifyDataSetChanged();
    }


    public CustomAdapter(AnimalsPics[] animalSet) {
        mAnimalsPics = animalSet;
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
        viewHolder.setImageJson(mAnimalsPics[position].getImageJson());
        viewHolder.getImageView().setImageBitmap(mAnimalsPics[position].getMyImage());
        Log.d(TAG, "Added view on position = " + position + " for animal " + mAnimalsPics[position].getAnimalId());
    }

    @Override
    public int getItemCount() {
        return mAnimalsPics.length;
    }

//    protected static String getAnimalsToString(){
//        String result="";
//        for (int i=0; i<1;i++){
//            String s= mAnimalsPics[i].toString();
//            result = result + "," + s;
//        }
//        return result;
//    }
}
