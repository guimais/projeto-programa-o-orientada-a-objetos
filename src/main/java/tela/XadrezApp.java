package tela;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class XadrezApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Xadrez");
        settings.setWidth(880);
        settings.setHeight(640);
    }

    public static void main(String[] args) {
        launch(args);
    }
}