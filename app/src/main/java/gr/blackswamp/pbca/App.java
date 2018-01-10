package gr.blackswamp.pbca;


import android.app.Application;

public class App extends Application {
    public static final String NAMESPACE = "gr.blackswamp.pbca.";
    static Settings _settings;

    public static Settings Settings() {
        return _settings;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _settings = new Settings(getApplicationContext());
    }
}
