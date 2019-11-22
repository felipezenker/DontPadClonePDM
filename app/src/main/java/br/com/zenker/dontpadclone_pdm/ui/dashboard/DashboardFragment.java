package br.com.zenker.dontpadclone_pdm.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import br.com.zenker.dontpadclone_pdm.R;

import static br.com.zenker.dontpadclone_pdm.R.string.cant_take_pic;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private static final int REQ_CODE_CAMERA = 1001;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    public void tirarFoto(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // da pra tirar foto?
        //this, getString(R.string.cant_take_pic), Toast.LENGTH_SHORT).show();
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQ_CODE_CAMERA);
        } else {
            //Toast.makeText(this, getString(cant_take_pic), Toast.LENGTH_SHORT).show();
        }

    }
}