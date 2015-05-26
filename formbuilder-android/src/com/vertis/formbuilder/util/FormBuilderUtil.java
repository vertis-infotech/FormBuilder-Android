package com.vertis.formbuilder.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;

import com.vertis.formbuilder.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by manish on 20/05/15.
 */
public class FormBuilderUtil {

    public Typeface getFontFromRes( Context context)
    {
        Typeface tf = null;
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(R.raw.roboto);
        } catch(Resources.NotFoundException e) { }
        String outPath = context.getCacheDir() + "/tmp" + System.currentTimeMillis()+".raw";
        try {
            byte[] buffer = new byte[is.available()];
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));
            int l = 0;
            while((l = is.read(buffer)) > 0)
                bos.write(buffer, 0, l);
            bos.close();
            tf = Typeface.createFromFile(outPath);
            new File(outPath).delete();
        } catch (IOException e) { }
        return tf;
    }
}
