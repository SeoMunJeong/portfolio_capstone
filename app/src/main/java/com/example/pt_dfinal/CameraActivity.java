package com.example.pt_dfinal;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.pt_dfinal.util.ImageResizeUtils;
import com.gun0912.tedpermission.PermissionListener;
//import com.gun0912.tedpermission.TedPermission; xxxxxxxxxxxxxxxx
import com.gun0912.tedpermission.TedPermissionUtil;
import com.gun0912.tedpermission.normal.TedPermission;

import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "blackjin";

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    private Boolean isCamera = false;
    private File tempFile;
    public File cropFile;

    private PermissionListener permissionListener;
    private Boolean isPermission = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        SharedPreferences sharedPref = getSharedPreferences("PREF", Context.MODE_PRIVATE);

        final String ocrApiGwUrl = sharedPref.getString("ocr_api_gw_url", "https://1nmqgfe05o.apigw.ntruss.com/custom/v1/17593/1159858723a553520011325a7a8b07c5ed8e3c5e6e2131c93f34f717abe1e3eb/general");
        final String ocrSecretKey = sharedPref.getString("ocr_secret_key", "ZEJEaFdGQnZVVHFOSEllT2JTTXFzZmFyV3N4S092dXU=");


        // ????????? ?????? ??????/?????? ?????? ?????? ??? ??????
        Button btn_inbodyCheck = (Button) findViewById(R.id.inbodyCheck);
        btn_inbodyCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraCheckActivity.class);
                startActivity(intent);
            }
        });

        // ???????????? ?????? ?????? ??? ??????
        Button btn_cameraWrite = (Button) findViewById(R.id.cameraWrite);
        btn_cameraWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InBodyWriteActivity1.class);
                startActivity(intent);
            }
        });

//        Button ocrTranslateBtn;
//        ocrTranslateBtn = (Button) findViewById(R.id.btn_ocr_translate);
//        ocrTranslateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OcrTask ocrTask = new OcrTask();
//                ocrTask.execute(ocrApiGwUrl, ocrSecretKey);
//            }
//        });



        tedPermission();
        //boolean isGranted = TedPermissionUtil.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION);
        boolean isGranted = TedPermissionUtil.isGranted(Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("ted", "isGranted: " + isGranted);
        List<String> deniedPermissions = TedPermissionUtil.getDeniedPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("ted", "deniedPermissions: " + deniedPermissions);



        findViewById(R.id.btnGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbum();
            }
        });

        findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });


    }





    //protected void tedPermission() { xxxxxxxxxx
    private void tedPermission() {
        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(CameraActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(CameraActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleTitle(R.string.rationale_title)
                .setRationaleMessage(R.string.rationale_message)
                .setDeniedTitle("Permission denied")
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("SETTING")
                //Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION
                .setPermissions(Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "?????? ???????????????.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {

                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " ?????? ??????");
                        tempFile = null;
                    } else {
                        Log.e(TAG, "tempFile ?????? ??????");
                    }

                } else {
                    Log.e(TAG, "tempFile ???????????? ??????");
                }
            } else {
                Log.e(TAG, "tempFile is null");
            }

            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: {

                Uri photoUri = data.getData();
                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

                cropImage(photoUri);

                break;
            }
            case PICK_FROM_CAMERA: {

                // ????????? ????????? ????????? ????????? data.getData()??? ??????
                Uri photoUri = Uri.fromFile(tempFile);
                Log.d(TAG, "takePhoto photoUri : " + photoUri);

                cropImage(photoUri);

                break;
            }
            case Crop.REQUEST_CROP: {
                setImage();
//                File cropFile = new File(Crop.getOutput(data).getPath()); //Crop.REQUEST_CROP ??????  ????????? ????????? ???????????? ??????
//                SharedPreferences sharedPref = getSharedPreferences("PREF", Context.MODE_PRIVATE);
//
//                final String ocrApiGwUrl = sharedPref.getString("ocr_api_gw_url", "https://1nmqgfe05o.apigw.ntruss.com/custom/v1/17593/1159858723a553520011325a7a8b07c5ed8e3c5e6e2131c93f34f717abe1e3eb/general");
//                final String ocrSecretKey = sharedPref.getString("ocr_secret_key", "ZEJEaFdGQnZVVHFOSEllT2JTTXFzZmFyV3N4S092dXU=");
//                OcrTask ocrTask = new OcrTask();
//                ocrTask.cropFile1 = cropFile;
//                ocrTask.execute(ocrApiGwUrl, ocrSecretKey);
            }
        }
    }

    /**
     * ???????????? ????????? ????????????
     */
    private void goToAlbum() {
        isCamera = false;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * ??????????????? ????????? ????????????
     */
    private void takePhoto() {
        isCamera = true;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);



        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "111????????? ?????? ??????! ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            /**
             *  ??????????????? OS ?????? ?????? ??????????????? file:// URI ??? ????????? ????????? FileUriExposedException ??????
             *  Uri ??? FileProvider ??? ?????? ????????? ?????????.
             *
             *  ?????? ?????? http://programmar.tistory.com/4 , http://programmar.tistory.com/5
             */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.pt_dfinal.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {

                Uri photoUri = Uri.fromFile(tempFile);
                Log.d(TAG, "takePhoto photoUri : " + photoUri);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        }
    }

    /**
     * Crop ??????
     */
    private void cropImage(Uri photoUri) {

        Log.d(TAG, "tempFile : " + tempFile);

        /**
         *  ??????????????? ????????? ???????????? tempFile??? ???????????? ?????? ??????????????????.
         */
        if (tempFile == null) {
            try {
                tempFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "222????????? ?????? ??????! ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        }

        //????????? ??? ????????? Uri
        Uri savingUri = Uri.fromFile(tempFile); //?????? ??? ????????? ?????? ?????? tempFile ??? uri??? ???????????? ????????? ??? tempFile??? ???????????? ????????? ?????????

        Crop.of(photoUri, savingUri).start(this);
    }

    /**
     * ?????? ??? ?????? ?????????
     */
    private File createImageFile() throws IOException {

        // ????????? ?????? ?????? ( blackJin_{??????}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "blackJin_" + timeStamp + "_";
        System.out.println(imageFileName);


        // ???????????? ????????? ?????? ?????? ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/blackJin/");
        if (!storageDir.exists()) storageDir.mkdirs();
        System.out.println(storageDir.getAbsolutePath());

        // ??? ?????? ??????
        //File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        File image = File.createTempFile(imageFileName, ".jpg"); //********************
        System.out.println(image.getAbsolutePath());
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }

    /**
     * tempFile ??? bitmap ?????? ?????? ??? ImageView ??? ????????????.
     */

    public void setImage() {
        String ocrMessage = "";
//        OcrTask ocrTask = new OcrTask();
        String ocrApiURL = "https://1nmqgfe05o.apigw.ntruss.com/custom/v1/17593/1159858723a553520011325a7a8b07c5ed8e3c5e6e2131c93f34f717abe1e3eb/general";
        String ocrsecretKey = "ZEJEaFdGQnZVVHFOSEllT2JTTXFzZmFyV3N4S092dXU=";
        String imageFile = tempFile.getAbsolutePath();

//        File cropFile = new File(Crop.getOutput(data).getPath());

//        ImageView imageView = findViewById(R.id.imageVeiew);

        ImageResizeUtils.resizeFile(tempFile, tempFile,1280,isCamera);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());
        tempFile = null;
        new Thread() {
            public void run() {
//                String result = "";
                try {
                    URL url = new URL(ocrApiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setUseCaches(false);
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setReadTimeout(30000);
                    con.setRequestMethod("POST");
                    String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
                    con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    con.setRequestProperty("X-OCR-SECRET", ocrsecretKey);

                    JSONObject json = new JSONObject();
                    json.put("version", "V2");
                    json.put("requestId", UUID.randomUUID().toString());
                    json.put("timestamp", System.currentTimeMillis());
                    JSONObject image = new JSONObject();
                    image.put("format", "jpg");
                    image.put("name", "demo");
                    JSONArray images = new JSONArray();
                    images.put(image);
                    json.put("images", images);
                    String postParams = json.toString();

                    con.connect();
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    long start = System.currentTimeMillis();
                    File file = new File(imageFile);
                    writeMultiPart(wr, postParams, file, boundary);
                    wr.close();


                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if (responseCode == 200) {
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();

                    System.out.println(response);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String result = "";
                            try {
                                result = jsonToString(response.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println(result);
                        }
                    });

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();




//        imageView.setImageBitmap(originalBm);

        /**
         *  tempFile ?????? ??? null ????????? ????????? ?????????.
         *  (resultCode != RESULT_OK) ??? ??? (tempFile != null)?????? ?????? ????????? ???????????? ?????????
         *  ????????? ???????????? ?????? ?????? ?????? ?????? ?????? ????????? ???????????????.
         */


    }

    private static void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws
            IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (file != null && file.isFile()) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString
                    .append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + file.getName() + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }

    public String jsonToString(String jsonResultStr) throws JSONException {
        String resultText = "";
        // API ?????? ?????? ?????? JSON ?????? ??????????????? ????????? ??????
        // JSONParser  ???????????? ??????
        // images / 0 / fields / inferText ??????
        JSONObject jsonObj = new JSONObject(jsonResultStr);
        JSONArray imageArray = (JSONArray) jsonObj.get("images");
        if(imageArray != null) {
            JSONObject tempObj = (JSONObject) imageArray.get(0);
            JSONArray fieldArray = (JSONArray) tempObj.get("fields");
            if(fieldArray != null) {
                for(int i=0; i<fieldArray.length(); i++) {
                    tempObj = (JSONObject) fieldArray.get(i);
                    resultText += (String) tempObj.get("inferText") + " ";
                }
            }
        } else {
            System.out.println("??????");
        }
        String[] array = resultText.split(" ");
        List<String> inbodyresult = new ArrayList<>();
        //??????
        for(int s=0; s<array.length; s++) {
            if (array[s].contains(".")) {
                inbodyresult.add(array[s]);
            }
        }
        System.out.println("??????:"+ inbodyresult.get(0));
        System.out.println("????????????:"+ inbodyresult.get(1));
        System.out.println("????????????:"+ inbodyresult.get(2));
        TextView txtweight = (TextView) findViewById(R.id.updateWeight);
        txtweight.setText(inbodyresult.get(0));
        TextView txtmuscle = (TextView) findViewById(R.id.muscle);
        txtmuscle.setText(inbodyresult.get(1));
        TextView txtfat = (TextView) findViewById(R.id.fat);
        txtfat.setText(inbodyresult.get(2));

        // ??? ??????
        // ????????? ????????? -> ????????????????????????
        Intent intent = new Intent(getBaseContext(),CameraCheckActivity.class);
        intent.putExtra("weight",inbodyresult.get(0));
        intent.putExtra("muscle",inbodyresult.get(1));
        intent.putExtra("fat",inbodyresult.get(2));
        startActivity(intent);

        return resultText;
    }
}
