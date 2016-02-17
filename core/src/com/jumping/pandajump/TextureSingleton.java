package com.jumping.pandajump;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by ikomarov on 16.02.2016.
 */
public class TextureSingleton {


    private  static  String pathToTexture = null;
    private static  Texture instance = null;

    private TextureSingleton() {}

    public static  Texture getInstance(String path) {
        if (instance == null)   {

                    instance = new Texture(path);
                    pathToTexture = path;

            }
        else if (!pathToTexture.equalsIgnoreCase(path)) {
                instance = new Texture(path);
                pathToTexture = path;

        }
        return instance;
    }





}
