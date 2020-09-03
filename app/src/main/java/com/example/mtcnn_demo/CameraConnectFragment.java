package com.example.mtcnn_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.Objects;
import java.util.Vector;

public class CameraConnectFragment extends Fragment implements TextureView.SurfaceTextureListener, Camera2Wrapper.OnCamera2FrameListener{

    private Context mContext = FaceDetectApp.sAppContext;
    private AutoFitTextureView mTextureView;
    private FaceDetectionView mFaceDetectionView;
    private MTCNN mtcnn;
    private Camera2Wrapper mCamera2Wrapper;
    private Handler mHandler = new Handler();

    public static CameraConnectFragment newInstance(){
        return new CameraConnectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_camera_connect_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTextureView =view.findViewById(R.id.textureView);
        mFaceDetectionView = view.findViewById(R.id.FaceDetectionView);
        mCamera2Wrapper = new Camera2Wrapper(getActivity(), mTextureView);
        mCamera2Wrapper.setOnCamera2FrameListener(this);
        View switchCameraButton = view.findViewById(R.id.camera_switch_View);
        switchCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera2Wrapper.switchCamera();
            }
        });
        init_MTCNN();
    }

    private void init_MTCNN() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String faceShapeModelPath = Constants.INSTANCE.getFaceShapeModelPath();
                if (!new File(faceShapeModelPath).exists()){
                    FileUtils.INSTANCE.copyFileFromAssetsToOthers(mContext, Constants.FACE_SHAPE_MODEL_PATH, faceShapeModelPath);
                }
                mtcnn =new MTCNN(Objects.requireNonNull(getActivity()).getAssets());
            }
        });
        thread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCamera2Wrapper.startBackgroundThread();
        if (mTextureView.isAvailable()) {
            mCamera2Wrapper.openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        }
        else {
            mTextureView.setSurfaceTextureListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera2Wrapper.closeCamera();
        mCamera2Wrapper.stopBackgroundThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera2Wrapper.release();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mCamera2Wrapper.openCamera(i,i1);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        mCamera2Wrapper.configureTransform(i, i1);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    public void onImageAvailable(final Bitmap bitmap) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    while (true){
                        final Vector<Box> results;
                        synchronized (this){
                            results = mtcnn.detectFaces(bitmap, 40);
                        }
                        if (results != null && results.size() >0){
                            mHandler.postAtFrontOfQueue(new Runnable() {
                                @Override
                                public void run() {
                                    mFaceDetectionView.setResults(results);
                                }
                            });
                        }
                        else bitmap.recycle();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}