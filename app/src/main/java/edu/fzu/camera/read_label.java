package edu.fzu.camera;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//import static android.support.v4.graphics.drawable.IconCompat.getResources;

public class read_label {

    /*   List<String> labels = new ArrayList<>();


       InputStream labelsInput = AssetManager.open('labels.txt');
       BufferedReader br = new BufferedReader(new InputStreamReader(labelsInput));
       String line;
       while((line= br.readline()) != null){
           labels.add(line);
       }
       br.close();*/
    private static final int MAX_RESULTS = 100;//3817;
    private static final int INPUT_SIZE = 96;
    private String labelFilename;
    private String modelFilename;
    private List<String> labels = new ArrayList<>();
    private AssetManager assetManager;
    private TensorFlowInferenceInterface inferenceInterface;

    public read_label(String labelFilename, String modelFilename, AssetManager assetManager) {
        this.labelFilename = labelFilename;
        this.modelFilename = modelFilename;
        this.assetManager = assetManager;
    }

    public void load() throws IOException {
        InputStream labelsInput = assetManager.open(labelFilename);
        BufferedReader br = new BufferedReader(new InputStreamReader(labelsInput));
        String line;
        while ((line = br.readLine()) != null) {
            labels.add(line);
        }
        br.close();
        if (inferenceInterface != null) {
            inferenceInterface.close();
        }
        inferenceInterface = new TensorFlowInferenceInterface(assetManager, modelFilename);
    }

    public static int getMaxindex(float[] arr) {
        int maxindex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[maxindex]) {
                maxindex = i;
            }
        }
        return maxindex;
    }

    public String detect(Bitmap bitmap) {
        int[] pixels = new int[INPUT_SIZE * INPUT_SIZE];
        bitmap.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE);
        //byte[] byteInput = new byte[pixels.length];
/*        for (int i = 0; i < pixels.length; ++i) {
            byteInput[i * 3 + 2] = (byte) (pixels[i] & 0xFF);
            byteInput[i * 3 + 1] = (byte) ((pixels[i] >> 8) & 0xFF);
            byteInput[i * 3 + 0] = (byte) ((pixels[i] >> 16) & 0xFF);
        }
        */
        inferenceInterface.feed("layer0-bn/bn0/Const:0", pixels, 1, INPUT_SIZE, INPUT_SIZE);
        inferenceInterface.run(new String[]{"layer14-output/add:0"}, false);
//        float[] boxes = new float[MAX_RESULTS * 4];
//        float[] scores = new float[MAX_RESULTS];
        float[] classes = new float[MAX_RESULTS];
//        inferenceInterface.fetch("detection_boxes", boxes);
//        inferenceInterface.fetch("detection_scores", scores);
        inferenceInterface.fetch("layer14-output/add:0", classes);
        int index = getMaxindex(classes);
        String result = labels.get(index);
    /*for (int i = 0; i < classes.length; i++) {
            if (scores[i] > minimum) {
                RectF box = new RectF(
                        boxes[4 * i + 1] * INPUT_SIZE,
                        boxes[4 * i] * INPUT_SIZE,
                        boxes[4 * i + 3] * INPUT_SIZE,
                        boxes[4 * i + 2] * INPUT_SIZE);
                results.add(new DetectionResult(labels.get((int) classes[i]), scores[i], box));
            }
        }*/
        return result;
    }
}
