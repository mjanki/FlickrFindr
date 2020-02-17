package org.br.flickrfinder.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.br.flickrfinder.R;
import org.br.flickrfinder.mappers.PhotoViewViewModelMapper;
import org.br.flickrfinder.models.PhotoViewEntity;
import org.br.util.PermissionUtilsKt;
import org.br.viewmodel.viewmodels.PhotoViewModel;

import java.util.Objects;

public class PhotoFragment extends Fragment {
    private PhotoViewModel photoVM;

    private PhotoViewViewModelMapper photoViewViewModelMapper = new PhotoViewViewModelMapper();

    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NOTE: if this fragment is pushed without arguments it'll throw
        // an IllegalArgumentException anyway
        PhotoViewEntity photo = PhotoFragmentArgs.fromBundle(
                Objects.requireNonNull(getArguments())
        ).getPhoto();

        photoVM = ViewModelProviders.of(this).get(PhotoViewModel.class);
        photoVM.init(photoViewViewModelMapper.downstream(photo));

        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setTitle(photo.getTitle());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupPhotoObservers(
                view.findViewById(R.id.ivPhoto),
                view.findViewById(R.id.bSave));

        setupStatusObservers();
        setupSaveButton(view.findViewById(R.id.bSave));
    }

    private void setupPhotoObservers(ImageView ivPhoto, Button bSave) {
        bSave.setEnabled(false);

        PhotoViewEntity photo = photoViewViewModelMapper.upstream(photoVM.getPhoto());

        if (photo.getOriginalBitmap() == null) {
            Glide.with(PhotoFragment.this)
                    .asBitmap()
                    .load(photo.getOriginalUrl())
                    .placeholder(R.drawable.ic_happy)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            bSave.setEnabled(true);

                            photoVM.postPhoto(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        } else {
            bSave.setEnabled(false);

            photoVM.postPhoto(photo.getOriginalBitmap());
        }

        photoVM.getPhotoBitmap().observe(
                getViewLifecycleOwner(),
                bitmap -> {
                    ivPhoto.setImageBitmap(bitmap);

                    if (photoVM.getPhoto().getOriginalBitmap() == null) {
                        photoVM.getPhoto().setOriginalBitmap(bitmap);
                    }
                }
        );

        photoVM.getPhotoSavedLiveData().observe(
                getViewLifecycleOwner(),
                saved -> bSave.setEnabled(!saved)
        );
    }

    private void savePhoto() {
        PhotoViewEntity photo = photoViewViewModelMapper.upstream(photoVM.getPhoto());

        Glide.with(PhotoFragment.this)
                .asBitmap()
                .load(photo.getThumbUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        photoVM.getPhoto().setThumbBitmap(resource);
                        photoVM.savePhoto();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });
    }

    private void setupStatusObservers() {

    }

    private void setupSaveButton(Button bSave) {
        // If Permissions don't exist request them before saving, otherwise save
        bSave.setOnClickListener(v -> {
            if (PermissionUtilsKt.hasWritePermissions(getContext())) {
                savePhoto();
            } else {
                PermissionUtilsKt.requestWritePermissions(this, 42);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 42) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePhoto();
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
