package userInterface.components

import javax.swing.{JSpinner, SpinnerNumberModel}

case class Spinner(
    initialValue: Int,
    minimum: Int,
    maximum: Int,
    stepSize: Double
) extends JSpinner(
      new SpinnerNumberModel(initialValue, minimum, maximum, stepSize)
    )
    with Component {
  override val padding: Int = 4
}
