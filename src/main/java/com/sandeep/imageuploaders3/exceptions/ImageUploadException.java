package com.sandeep.imageuploaders3.exceptions;

import java.io.IOException;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException(String msg) {
        super(msg);
    }
}
