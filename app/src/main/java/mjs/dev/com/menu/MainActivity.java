package mjs.dev.com.menu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.FOODLIST";

    private Vision vision;
    private String parsedData;
    private Button button;

    private String [] foodlist;
    public String [] temp_data = {"apple","banana", "cherry", "dates","apple","banana", "cherry", "dates","apple","banana", "cherry", "dates"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(),new AndroidJsonFactory(),null);
        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer("AIzaSyDiuvPzrL4c0Xip3JIhafbd4bSA255y0XU"));

        vision = visionBuilder.build();

        //listen to the camera button click to read menu and then transfer to foodlist
        //readMenu();

        button = (Button) findViewById(R.id.temp_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                startFoodList(arg0);
            }
        });
    }

    private void readMenu() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = getResources().openRawResource(R.raw.menu);
                    byte[] menuData = IOUtils.toByteArray(inputStream);

                    Image inputMenu = new Image();
                    inputMenu.encodeContent(menuData);

                    Feature desiredFeature = new Feature();
                    desiredFeature.setType("DOCUMENT_TEXT_DETECTION");

                    AnnotateImageRequest request = new AnnotateImageRequest();
                    request.setImage(inputMenu);
                    request.setFeatures(Arrays.asList(desiredFeature));

                    BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
                    batchRequest.setRequests(Arrays.asList(request));

                    BatchAnnotateImagesResponse batchResponse =
                            vision.images().annotate(batchRequest).execute();

                    final TextAnnotation text = batchResponse.getResponses()
                            .get(0).getFullTextAnnotation();

                    parsedData = text.getText();

                    TextView helloTextView = (TextView) findViewById(R.id.data);
                    helloTextView.setText(parsedData);


                } catch (Exception e) {
                    Log.d("ERROR", e.getMessage());
                }
            }
        });
    }

    public void startFoodList(View view){

        Intent intent = new Intent(this, FoodList.class);
        foodlist = temp_data;
        intent.putExtra(EXTRA_MESSAGE, foodlist);

        startActivity(intent);
    }
}
