package br.com.pedroargentati.viagens_api.util.io;

import java.io.InputStream;

public class FileSecurityCheck {

    public static final FileSecurityCheck INSTANCE = new FileSecurityCheck();

    private FileSecurityCheck() {
        // Singleton
    }

    public final void check(String fullFileName, String mimeType, InputStream dataStream) {
        // To be implemented
    }

}
