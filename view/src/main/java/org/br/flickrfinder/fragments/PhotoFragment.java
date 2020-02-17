package org.br.flickrfinder.fragments;


import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.br.flickrfinder.R;
import org.br.flickrfinder.mappers.PhotoViewViewModelMapper;
import org.br.flickrfinder.models.PhotoViewEntity;
import org.br.util.PermissionUtilsKt;
import org.br.viewmodel.models.PhotoViewModelEntity;
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
        String photoId = PhotoFragmentArgs.fromBundle(
                Objects.requireNonNull(getArguments())
        ).getPhotoId();

        photoVM = ViewModelProviders.of(this).get(PhotoViewModel.class);
        photoVM.init(photoId);

        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setTitle("Photo");
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

        Button bSave = view.findViewById(R.id.bSave);
        ImageView ivPhoto = view.findViewById(R.id.ivPhoto);

        setupPhotoObservers(ivPhoto, bSave);
        setupStatusObservers(bSave);
        setupSaveButton(bSave);

        setupView(bSave);
    }

    private void setupPhotoObservers(ImageView ivPhoto, Button bSave) {
        photoVM.getPhotoLiveData().observe(
                getViewLifecycleOwner(),
                photoViewModelEntity -> {

                    PhotoViewEntity photo = photoViewViewModelMapper.upstream(photoViewModelEntity);

                    // If photo is not saved get Bitmap from Url, enable save, and notify VM,
                    // otherwise just notify VM
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
                        photoVM.postPhoto(photo.getOriginalBitmap());
                    }
                }
        );

        // Observe photo, display and set Original Bitmap (to allow saving)
        photoVM.getPhotoBitmap().observe(
                getViewLifecycleOwner(),
                bitmap -> {
                    ivPhoto.setImageBitmap(bitmap);

                    if (photoVM.getPhotoLiveData().getValue() != null &&
                            photoVM.getPhotoLiveData().getValue().getOriginalBitmap() == null) {
                        photoVM.getPhotoLiveData().getValue().setOriginalBitmap(bitmap);
                    }
                }
        );
    }

    private void setupStatusObservers(Button bSave) {
        // If image is saved successfully disable Save button
        photoVM.getPhotoSavedLiveData().observe(
                getViewLifecycleOwner(),
                saved -> bSave.setEnabled(!saved)
        );
    }

    private void setupSaveButton(Button bSave) {
        // If Permissions don't exist request them before saving then save, otherwise save
        bSave.setOnClickListener(v -> {
            if (PermissionUtilsKt.hasWritePermissions(getContext())) {
                savePhoto();
            } else {
                PermissionUtilsKt.requestWritePermissions(this, 42);
            }
        });
    }

    private void setupView(Button bSave) {
        // Disable Save button until image is fetched and checked that it isn't saved already
        bSave.setEnabled(false);
    }

    private void savePhoto() {
        PhotoViewModelEntity photoViewModelEntity = photoVM.getPhotoLiveData().getValue();
        if (photoViewModelEntity == null) { return; }

        PhotoViewEntity photo = photoViewViewModelMapper.upstream(photoViewModelEntity);

        Glide.with(PhotoFragment.this)
                .asBitmap()
                .load(photo.getThumbUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        photoVM.savePhotoThumbnail(resource);
                        photoVM.savePhoto();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionUtilsKt.hasWritePermissions(getContext())) {
            savePhoto();
        } else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }
}
