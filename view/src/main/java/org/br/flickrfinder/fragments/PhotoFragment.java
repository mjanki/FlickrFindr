package org.br.flickrfinder.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.br.flickrfinder.R;
import org.br.viewmodel.models.PhotoViewModelEntity;
import org.br.viewmodel.viewmodels.PhotoViewModel;

public class PhotoFragment extends Fragment {
    private PhotoViewModel photoVM;

    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoVM = ViewModelProviders.of(this).get(PhotoViewModel.class);

        Bundle args = getArguments();
        String photoId = "";
        if (args != null) {
            photoId = PhotoFragmentArgs.fromBundle(args).getPhotoId();
        }

        photoVM.init(photoId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivPhoto = view.findViewById(R.id.ivPhoto);

        photoVM.getPhoto().observe(
                getViewLifecycleOwner(),
                photoViewModelEntity -> Glide.with(PhotoFragment.this).load(photoViewModelEntity.getImgOriginal()).placeholder(R.drawable.ic_happy).into(ivPhoto)
        );
    }
}
