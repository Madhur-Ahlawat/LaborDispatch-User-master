package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;



/**
 * Created by user on 4/18/2018.
 */

public class Message {
    public static void message(Context context, String message)
    {
        Toast.makeText (context, message, Toast.LENGTH_SHORT).show ();
    }
}
