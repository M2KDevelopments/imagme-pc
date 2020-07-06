import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXColorPicker
import com.jfoenix.controls.JFXTextField
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.stage.FileChooser
import mdu.msgbox.MsgBox
import java.net.URL
import java.util.*


class MainController : Initializable {

    @FXML
    private lateinit var mainScene: AnchorPane

    @FXML
    private lateinit var imageViewLogo: ImageView

    @FXML
    private lateinit var colorPickImage: JFXColorPicker

    @FXML
    private lateinit var chbxColor: JFXCheckBox

    @FXML
    private lateinit var chbxUseImage: JFXCheckBox

    @FXML
    private lateinit var colorPickText: JFXColorPicker

    @FXML
    private lateinit var txtCaption: JFXTextField

    @FXML
    private lateinit var canvas: Canvas


    override fun initialize(location: URL?, resources: ResourceBundle?) {

        //set up message box
        MsgBox.setup(mainScene)


    }

    @FXML
    fun changeImage() {
        val fc = FileChooser()
        fc.extensionFilters.add(
            FileChooser.ExtensionFilter(
                "Image files",
                "*.png", "*.jpg", "*.bmp", "*.tiff", "*.gif"
            )
        )
        fc.title = "Choose Main Image."
        val f = fc.showOpenDialog(null)
        if (f != null) {
            imageViewLogo.image = Image("file:" + f.absolutePath)
        }
    }

    @FXML
    fun colorChange() {
    }

    @FXML
    fun saveImages() {
    }

    @FXML
    fun useSpecificColor() {
    }

}