package ntk.ambrose.landscapist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import ntk.ambrose.landscapist.adapters.ListImageAdapter;
import ntk.ambrose.landscapist.models.ImageElement;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    ListImageAdapter adapter;
    ListView listImage;
    Spinner spinnerTag;
    ArrayList<String> tagList;
    Button btSubmit;
    TextView tvInfo;
    ProgressBar progressUpload;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();
    int success = 0;
    int total = 0;
    String[] tagID = {
            "CHN",
            "CLN",
            "TSN",
            "ODT",
            "SKK",
            "DGT",
            "SON",
            "PNK",
            "BCS"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        final ArrayList<ImageElement> imageList = Storage.getInstance().getData("imageList");
        adapter = new ListImageAdapter(getBaseContext(),R.layout.list_image,imageList);
        listImage = findViewById(R.id.listImage);
        spinnerTag = findViewById(R.id.spinnerTag);
        btSubmit = findViewById(R.id.btSubmit);
        tvInfo = findViewById(R.id.tvInfo);
        progressUpload = findViewById(R.id.progressUpload);
        listImage.setAdapter(adapter);
        tagList = new ArrayList<>();
        tagList.add("Cây hằng năm - CHN");
        tagList.add("Cây lâu năm - CLN");
        tagList.add("Thủy hải sản - TSN");
        tagList.add("Đất đô thị - ODT");
        tagList.add("Khu sản xuất, kinh doanh - SKK");
        tagList.add("Đất mục đích công cộng - DGT");
        tagList.add("Sông ngòi, kênh rạch - SON");
        tagList.add("Phi nông nghiệp - PNK");
        tagList.add("Chưa sử dụng - BCS");
        spinnerTag.setAdapter(new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,tagList));
        spinnerTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for(int k =0;k<adapter.getCount();k++) {
                    adapter.getItem(k).setTag(tagID[(int)spinnerTag.getSelectedItemId()]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvInfo.setText("Submitting...");
                btSubmit.setVisibility(View.GONE);
                success = 0;
                total = adapter.countSelected();
                progressUpload.setProgress(0);
                progressUpload.setMax(total);
                listImage.setEnabled(false);
                for(int k =0;k<adapter.getCount();k++) {
                    ImageElement element = adapter.getItem(k);
                    if(element.isSelected()) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        Bitmap bitmap = element.getData();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageRef.child("capture/" + element.getLocation() + ";" + element.getTag() +";"+UUID.randomUUID().toString()+ ".jpg").putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                tvInfo.setText("Cannot submit these images");
                                btSubmit.setVisibility(View.VISIBLE);
                                success = 0;
                                listImage.setEnabled(true);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                success++;
                                progressUpload.setProgress(success);
                                if(success == total){
                                    Toast.makeText(getBaseContext(),"Submit successfully",Toast.LENGTH_LONG).show();
                                    Storage.getInstance().removeAll();
                                    startActivity(new Intent(UploadActivity.this,MainActivity.class));
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
