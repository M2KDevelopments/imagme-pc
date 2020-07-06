import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXColorPicker
import com.jfoenix.controls.JFXSlider
import com.jfoenix.controls.JFXTextField
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import mdu.toasts.Toast
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*
import javax.imageio.ImageIO
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
    private lateinit var chbxShadow: JFXCheckBox

    @FXML
    private lateinit var colorPickText: JFXColorPicker

    @FXML
    private lateinit var lblOpacity: Label

    @FXML
    private lateinit var sliderOpacity: JFXSlider

    @FXML
    private lateinit var txtCaption: JFXTextField

    @FXML
    private lateinit var canvas: Canvas

    @FXML
    private lateinit var radLightColors: RadioButton

    @FXML
    private lateinit var radDarkColors: RadioButton

    companion object {
        const val SHADOW_PADDING = 15
        const val IMG_LOGO_SIZE = 500
        const val IMAGE_DEFAULT_SIZE = 1000
        var SAVE_FOLDER: String =
            System.getProperty("user.home") + File.separator + "Documents" + File.separator + "ImagMe Images" + File.separator
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        //create folder for saving
        File(SAVE_FOLDER).mkdirs()

        //listener for slider
        sliderOpacity.valueProperty().addListener { _, _, _ ->
            lblOpacity.text = "Opacity ${sliderOpacity.value * (100 / 255)}%"
            testing()
        }

        testing()

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
        testing()
    }

    @FXML
    fun saveImages() {

        //create folder for saving
        val userFolder = File(SAVE_FOLDER)
        userFolder.mkdirs()

        val fc = DirectoryChooser()
        fc.title = "Directory for saving images"
        fc.initialDirectory = userFolder
        val folder = fc.showDialog(null)
        if (folder != null) {
            val list = ArrayList<Image>()
            list.addAll(oneCirclePattern())
            list.addAll(fourCirclePattern())
            list.addAll(oneCirclePattern(open = true))
            list.addAll(fourCirclePattern(open = true))
            list.addAll(oneSquarePattern())
            list.addAll(fourSquarePattern())
            list.addAll(fourSquarePattern(open = true))
            list.addAll(randomPattern(number = 30, square = false))
            list.addAll(randomPattern(square = true))

            for ((index, image) in list.withIndex()) {
                val file = File(folder.absolutePath, "Image $index.png")
                writeImage(image, file.absolutePath)
            }

            Toast.makeToast("Done writing image", mainScene)

        }
    }

    @FXML
    fun testing() {

        //set radio buttons
        radDarkColors.isDisable = chbxColor.isSelected
        radLightColors.isDisable = chbxColor.isSelected

        //clear canvas
        canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)

        val hashMap = getGraphicsStuff()
        val g: Graphics2D = hashMap["graphics"] as Graphics2D
        val bmp: BufferedImage = hashMap["image"] as BufferedImage

        //start drawing
        drawCircle(g, 50, 50, 400)
        drawCircle(g, 50, 550, 400)
        drawCircle(g, 550, 50, 400)
        drawCircle(g, 550, 550, 400)

        //draw icon
        drawIcon(g)

        //convert to javafx image
        val img = SwingFXUtils.toFXImage(bmp, null)

        //draw graphics
        canvas.graphicsContext2D.drawImage(img, 0.0, 0.0, canvas.width, canvas.height)
    }

    private fun getGraphicsStuff(): HashMap<String, Any> {
        //get graphics context
        val bmp = BufferedImage(IMAGE_DEFAULT_SIZE, IMAGE_DEFAULT_SIZE, BufferedImage.TYPE_INT_ARGB)
        val g = bmp.createGraphics()
        g.font = Font("Arial", Font.ITALIC, 60)

        //get color for graphics
        setupGraphicsColor(g)

        val hashMap = HashMap<String, Any>()
        hashMap.putIfAbsent("graphics", g)
        hashMap.putIfAbsent("image", bmp)
        return hashMap
    }

    private fun setupGraphicsColor(g: Graphics2D) {

        if (chbxColor.isSelected) {

            //get colors
            val red = (colorPickImage.value.red * 255).toInt()
            val green = (colorPickImage.value.green * 255).toInt()
            val blue = (colorPickImage.value.blue * 255).toInt()
            val alpha = sliderOpacity.value.toInt()

            //set color
            g.color = Color(red, green, blue, alpha)
        } else {
            val rnd = Random()
            if (radDarkColors.isSelected) { //use dark colors
                g.color = Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))
            } else { //use light colors
                g.color = Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))
            }
        }

    }

    private fun drawIcon(g: Graphics2D) {

        if (chbxUseImage.isSelected) {
            if (imageViewLogo.image != null) {

                //create buffered image from image in image view
                val logo = SwingFXUtils.fromFXImage(imageViewLogo.image, null)

                val w: Int
                val h: Int

                //get/set width and height
                if (logo.width > logo.height) {
                    w = IMG_LOGO_SIZE
                    h = (IMG_LOGO_SIZE / (logo.width.toFloat() / logo.height.toFloat())).toInt()
                } else {
                    h = IMG_LOGO_SIZE
                    w = (IMG_LOGO_SIZE / (logo.height.toFloat() / logo.width.toFloat())).toInt()
                }

                //set x and y position
                val x = (IMAGE_DEFAULT_SIZE / 2) - (w / 2)
                val y = (IMAGE_DEFAULT_SIZE / 2) - (h / 2)
                g.drawImage(logo, x, y, w, h, null)
            }

            if (txtCaption.text.isNotBlank()) {
                val red = colorPickText.value.red.toInt()
                val green = colorPickText.value.green.toInt()
                val blue = colorPickText.value.blue.toInt()

                //set color
                g.color = Color(red, green, blue)
                //draw text on center
                g.drawString(txtCaption.text, 250, 600)
            }
        } else if (txtCaption.text.isNotBlank()) {

            val red = colorPickText.value.red.toInt()
            val green = colorPickText.value.green.toInt()
            val blue = colorPickText.value.blue.toInt()

            //set color
            g.color = Color(red, green, blue)

            //draw text on center
            g.drawString(txtCaption.text, 250, 440)
        }
    }

    private fun drawCircle(
        g: Graphics2D,
        x: Int,
        y: Int,
        size: Int,
        open: Boolean = false,
        shadow: Boolean = true,
        borderThickness: Int = 6
    ) {
        //add shadow if effect
        val originalColor = g.color
        if (chbxShadow.isSelected && shadow) {

            g.color = Color(10, 10, 10, 10) //gray shadow effect
            if (open) {
                for (i in 0 until borderThickness) {
                    g.drawArc(x + SHADOW_PADDING + i, y + SHADOW_PADDING + i, size - (i * 2), size - (i * 2), 0, 360)
                }

            } else {
                g.fillArc(x + SHADOW_PADDING, y + SHADOW_PADDING, size, size, 0, 360)
            }

        }

        g.color = originalColor

        if (open) {
            for (i in 0 until borderThickness) {
                g.drawArc(x + i, y + i, size - (i * 2), size - (i * 2), 0, 360)
            }
        } else {
            g.fillArc(x, y, size, size, 0, 360)
        }

    }

    private fun drawRect(
        g: Graphics2D,
        x: Int,
        y: Int,
        w: Int,
        h: Int = 0,
        open: Boolean = false,
        radius: Int = 10,
        shadow: Boolean = true,
        borderThickness: Int = 6
    ) {

        //add shadow if effect
        val originalColor = g.color
        if (chbxShadow.isSelected && shadow) {
            g.color = Color(10, 10, 10, 10) //gray shadow effect
            if (h == 0) {//square
                if (open) {
                    for (i in 0 until borderThickness) {
                        g.drawRoundRect(
                            x + SHADOW_PADDING + i,
                            y + SHADOW_PADDING + i,
                            w - (i * 2),
                            w - (i * 2),
                            radius,
                            radius
                        )
                    }

                } else {
                    g.fillRoundRect(x + SHADOW_PADDING, y + SHADOW_PADDING, w, w, radius, radius)
                }

            } else {
                if (open) {
                    for (i in 0 until borderThickness) {
                        g.drawRoundRect(x + i, y + i, w - (i * 2), h - (i * 2), radius, radius)
                    }
                } else {
                    g.fillRoundRect(x + SHADOW_PADDING, y + SHADOW_PADDING, w, h, radius, radius)
                }
            }

        }

        //draw rect
        g.color = originalColor
        if (h == 0) {//square
            if (open) {
                for (i in 0 until borderThickness) {
                    g.drawRoundRect(x + i, y + i, w - (i * 2), h - (i * 2), radius, radius)
                }
            } else {
                g.fillRoundRect(x, y, w, w, radius, radius)
            }
        } else {
            if (open) {
                for (i in 0 until borderThickness) {
                    g.drawRoundRect(x + i, y + i, w - (i * 2), h - (i * 2), radius, radius)
                }
            } else {
                g.fillRoundRect(x, y, w, h, radius, radius)
            }
        }
    }

    /**
     * Save Image from objects into the asset folder
     *
     * @param img  -  JavaFX Image
     * @param path - Path to save the image as a png.
     */
    private fun writeImage(img: Image, path: String) {
        val file = File(path)
        file.mkdir()
        file.mkdirs()
        try {
            val successful = ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file)
            if (successful) {
                println("Image written in: $path")
            } else {
                println("Image write failure.")
            }
        } catch (ex: IOException) {
            println("Image write failure.")
        }
    }

    /**
     * Graphics Patterns*
     */
    private fun oneCirclePattern(open: Boolean = false, number: Int = 10): ArrayList<Image> {

        val list = ArrayList<Image>()

        for (i in 0 until number) {
            val hashMap = getGraphicsStuff()
            val g: Graphics2D = hashMap["graphics"] as Graphics2D
            val bmp: BufferedImage = hashMap["image"] as BufferedImage

            //start drawing
            drawCircle(g, 50, 50, 900, open = open, shadow = false)

            //draw icon
            drawIcon(g)

            //convert to javafx image
            val img = SwingFXUtils.toFXImage(bmp, null)

            list.add(img)
        }

        return list
    }

    private fun fourCirclePattern(open: Boolean = false, number: Int = 10): ArrayList<Image> {

        val list = ArrayList<Image>()

        for (i in 0 until number) {
            val hashMap = getGraphicsStuff()
            val g: Graphics2D = hashMap["graphics"] as Graphics2D
            val bmp: BufferedImage = hashMap["image"] as BufferedImage

            //start drawing
            drawCircle(g, 50, 50, 400, open)
            drawCircle(g, 50, 550, 400, open)
            drawCircle(g, 550, 50, 400, open)
            drawCircle(g, 550, 550, 400, open)

            //draw icon
            drawIcon(g)

            //convert to javafx image
            val img = SwingFXUtils.toFXImage(bmp, null)

            list.add(img)
        }

        return list
    }

    private fun oneSquarePattern(open: Boolean = false, number: Int = 10): ArrayList<Image> {

        val list = ArrayList<Image>()

        for (i in 0 until number) {
            val hashMap = getGraphicsStuff()
            val g: Graphics2D = hashMap["graphics"] as Graphics2D
            val bmp: BufferedImage = hashMap["image"] as BufferedImage

            //start drawing
            drawRect(g, 50, 50, 900, open = open, shadow = false)

            //draw icon
            drawIcon(g)

            //convert to javafx image
            val img = SwingFXUtils.toFXImage(bmp, null)

            list.add(img)
        }

        return list
    }

    private fun fourSquarePattern(open: Boolean = false, number: Int = 10): ArrayList<Image> {

        val list = ArrayList<Image>()

        for (i in 0 until number) {
            val hashMap = getGraphicsStuff()
            val g: Graphics2D = hashMap["graphics"] as Graphics2D
            val bmp: BufferedImage = hashMap["image"] as BufferedImage

            //start drawing
            drawRect(g, 50, 50, 400, open = open)
            drawRect(g, 50, 550, 400, open = open)
            drawRect(g, 550, 50, 400, open = open)
            drawRect(g, 550, 550, 400, open = open)

            //draw icon
            drawIcon(g)

            //convert to javafx image
            val img = SwingFXUtils.toFXImage(bmp, null)

            list.add(img)
        }

        return list
    }

    private fun randomPattern(number: Int = 10, square: Boolean = false): ArrayList<Image> {

        val list = ArrayList<Image>()
        val rnd = Random()

        for (i in 0 until number) {
            rnd.setSeed(i.toLong())

            //get graphics
            val hashMap = getGraphicsStuff()
            val g: Graphics2D = hashMap["graphics"] as Graphics2D
            val bmp: BufferedImage = hashMap["image"] as BufferedImage


            val open = rnd.nextInt() % 4 == 0
            val amount = 7 + rnd.nextInt(15)

            for (j in 0 until amount) {
                //start drawing
                if (square) {
                    // random positioning and size
                    val x = rnd.nextInt(IMAGE_DEFAULT_SIZE)
                    val y = rnd.nextInt(IMAGE_DEFAULT_SIZE)
                    val size = 75 + rnd.nextInt(200)
                    drawRect(g, x, y, size, open = open)
                } else {
                    // random positioning and size
                    val x = rnd.nextInt(IMAGE_DEFAULT_SIZE)
                    val y = rnd.nextInt(IMAGE_DEFAULT_SIZE)
                    val size = 75 + rnd.nextInt(200)
                    drawCircle(g, x, y, size, open = open)
                }
            }

            //draw icon
            drawIcon(g)

            //convert to javafx image
            val img = SwingFXUtils.toFXImage(bmp, null)

            list.add(img)
        }

        return list
    }
}