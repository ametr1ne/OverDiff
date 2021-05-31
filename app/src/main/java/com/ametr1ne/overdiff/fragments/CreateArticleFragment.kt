package com.ametr1ne.overdiff.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.encryption.JWT;
import com.ametr1ne.overdiff.models.User;
import com.ametr1ne.overdiff.utils.ArticleStatus;
import com.ametr1ne.overdiff.utils.CreateArticleTask;
import com.ametr1ne.overdiff.utils.ImageFilePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateArticleFragment extends Fragment {

    private MainActivity source;

    private String imagePath;
    private Bitmap bitmap;
    private File file;

    public CreateArticleFragment(MainActivity mainActivity) {
        this.source = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_article, container, false);

        User user = UserFactory.getInstance().getCurrentUser();
        if (user.getAccessToken() != null && !JWT.isAlive(user.getAccessToken())) {
            UserFactory.getInstance().refreshCurrentUser(user1 -> {
                if (!user1.isAuthorization()) {
                    source.openAuthorizationPage();
                }
            });
        }


        if (user.isAuthorization()) {
            imageView = (ImageView) view.findViewById(R.id.new_article_img);
            view.findViewById(R.id.new_article_find_img).setOnClickListener(v -> {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Pick_image);
            });

            view.findViewById(R.id.new_article_create).setOnClickListener(v -> {
                new CreateArticleTask(source,file, ((TextView) view.findViewById(R.id.new_article_name)).getText().toString(),
                        ((TextView) view.findViewById(R.id.new_article_text)).getText().toString(), status -> {
                    if (status == ArticleStatus.SUCCESSFULLY)
                        source.runOnUiThread(() -> {
                            source.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new TapesFragment(source)).commit();
                        });


                }).execute();

            });
        }

        return view;
    }

    private ImageView imageView;
    private final int Pick_image = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == Pick_image) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    final Uri imageUri = imageReturnedIntent.getData();
                    final InputStream imageStream = source.getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);

                    File file = new File(ImageFilePath.getPath(source, imageUri));
                    this.file = file;

                    String path = ImageFilePath.getPath(source, imageUri);


                    this.bitmap = selectedImage;
                    this.imagePath = path;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}