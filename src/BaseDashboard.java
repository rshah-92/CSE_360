import javafx.stage.Stage;

public abstract class BaseDashboard {
    protected Stage window;

    public BaseDashboard(Stage window) {
        this.window = window;
    }

    public abstract void showDashboard();
}
