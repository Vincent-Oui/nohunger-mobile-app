package com.example.nohunger.ui.donate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.nohunger.donate_paths;
import com.example.nohunger.R;
import com.example.nohunger.donate_video_view;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class DonateFragment extends Fragment  {
    private DonateViewModel donateViewModel;
    private TextView tvVideoView;
    private ImageSlider imageSlider;
    private Button btnDonate;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        donateViewModel = new ViewModelProvider(this).get(DonateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_donate, container, false);
        imageSlider = root.findViewById(R.id.image_slider);
        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel(R.drawable.recyc1, null));
        images.add(new SlideModel(R.drawable.recyc2, null));
        images.add(new SlideModel(R.drawable.recyc3, null));
        imageSlider.setImageList(images, ScaleTypes.FIT);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDonate = view.findViewById(R.id.btn_donate);
        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), donate_paths.class));
            }
        });

        tvVideoView = view.findViewById(R.id.tv_video_view);
        tvVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), donate_video_view.class));
            }
        });
    }
}