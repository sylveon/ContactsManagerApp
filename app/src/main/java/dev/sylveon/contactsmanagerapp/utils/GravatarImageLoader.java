package dev.sylveon.contactsmanagerapp.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import dev.sylveon.contactsmanagerapp.ContactsManagerApplication;

public class GravatarImageLoader {
    private static String getEmailHash(String email) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(email.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            builder.ensureCapacity(hash.length * 2);

            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot encode URL in MD5", e);
        }
    }

    public static ImageRequest loadAvatarForEmail(String email, int size, Response.Listener<Bitmap> successCallback, Response.ErrorListener errorCallback) {
        String avatarUrl = String.format(Locale.US, "https://www.gravatar.com/avatar/%s.png?s=%d&d=404", getEmailHash(email), size);
        ImageRequest request = new ImageRequest(
                avatarUrl,
                successCallback,
                size,
                size,
                ImageView.ScaleType.CENTER,
                Bitmap.Config.ARGB_8888,
                errorCallback
        );

        ContactsManagerApplication.getInstance().getRequestQueue().add(request);
        return request;
    }
}
