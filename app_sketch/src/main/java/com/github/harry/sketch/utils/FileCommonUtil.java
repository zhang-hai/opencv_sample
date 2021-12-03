package com.github.harry.sketch.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by zhanghai on 2019/1/25.
 * function：文件目录
 */
public class FileCommonUtil{


    /**
     * 发送广播，将拍照的图片、资源保存，使得在相册中可以查看
     * 保存到系统相册
     * @param context
     * @param filePath
     * @return
     */
    public static boolean sendInsertFileBroadcast(Context context, String filePath){
        File file = new File(filePath);
        try {
            insertImage(context.getContentResolver(),file.getAbsolutePath(),file.getName(),null);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));//通知图库刷新
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
     * 重写 {@link MediaStore.Images.Media#insertImage}，解决保存到相册出现两个相同图片的问题
     * @param cr
     * @param imagePath
     * @param name
     * @param description
     * @return
     * @throws FileNotFoundException
     */
    private static final String insertImage(ContentResolver cr, String imagePath,
                                            String name, String description) throws FileNotFoundException{
        FileInputStream stream = new FileInputStream(imagePath);
        try {
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            String ret = insertImage(cr, bm, name, description);
            bm.recycle();
            return ret;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }

    private static final String insertImage(ContentResolver cr, Bitmap source,
                                            String title, String description){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id,
                        MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                Bitmap microThumb = StoreThumbnail(cr, miniThumb, id, 50F, 50F,
                        MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    private static final Bitmap StoreThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width, float height,
            int kind) {
        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true);

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND,     kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int)id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT,   thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH,    thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);

            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        }
        catch (FileNotFoundException ex) {
            return null;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
