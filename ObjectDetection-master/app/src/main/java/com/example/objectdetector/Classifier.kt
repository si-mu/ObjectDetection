import android.content.Context
import android.graphics.Bitmap
import java.io.BufferedReader
import java.io.InputStreamReader
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class Classifier(context: Context) {
    private var imageClassifier: ImageClassifier? = null
    var labels: List<String> = listOf()  // To store class names

    init {
        try {
            // Initialize the image classifier with the TFLite model
            imageClassifier = ImageClassifier.createFromFile(context, "model.tflite")
            // Load class names from label.txt
            labels = loadLabels(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Method to load labels from label.txt
    private fun loadLabels(context: Context): List<String> {
        val labelsList = mutableListOf<String>()
        val inputStream = context.assets.open("labels.txt") // Open the label.txt file
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.forEachLine { line ->
            labelsList.add(line.trim()) // Add each line to the list after trimming whitespace
        }
        reader.close() // Close the reader
        return labelsList
    }

    fun classifyImage(bitmap: Bitmap): List<Classifications>? {
        val image = TensorImage.fromBitmap(bitmap)
        return this.imageClassifier?.classify(image)
    }
}
