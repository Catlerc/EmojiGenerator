import app.Context
import userInterface.MainForm

object Main {
  def main(args: Array[String]): Unit = {
    val context = Context(None)
    val form = MainForm(context)
    form.show()
  }
}
