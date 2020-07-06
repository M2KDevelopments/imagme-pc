import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.util.*

class Main : Application() {

    override fun start(stage: Stage) {

        val root = FXMLLoader.load<Parent>(javaClass.classLoader.getResource("fxmls/FXMLMain.fxml"))
        val scene = Scene(root)

        scene.fill = Color.TRANSPARENT
        stage.scene = scene
        stage.icons.add(Image(Objects.requireNonNull(javaClass.classLoader.getResourceAsStream("icons/logo.png"))))
        stage.title = "ImagMe"
        stage.isResizable = false
        stage.centerOnScreen()
        stage.show()
    }

}

fun main(args: Array<String>) {

    //launch app
    Application.launch(Main::class.java, *args)
}