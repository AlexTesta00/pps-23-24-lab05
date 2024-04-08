package ex

import scala.annotation.targetName

// Express a second degree polynomial
// Structure: secondDegree * X^2 + firstDegree * X + constant
trait SecondDegreePolynomial:
  def constant: Double
  def firstDegree: Double
  def secondDegree: Double
  @targetName("plus")
  def +(polynomial: SecondDegreePolynomial): SecondDegreePolynomial
  @targetName("minus")
  def -(polynomial: SecondDegreePolynomial): SecondDegreePolynomial


object SecondDegreePolynomial:
  def apply(secondDegree: Double, firstDegree: Double, constant: Double): SecondDegreePolynomial =
    SecondDegreePolynomialImpl(secondDegree, firstDegree, constant)
  private case class SecondDegreePolynomialImpl(override val secondDegree: Double,
                                                override val firstDegree: Double,
                                                override val constant: Double) extends SecondDegreePolynomial:
    @targetName("plus")
    override def +(polynomial: SecondDegreePolynomial): SecondDegreePolynomial =
      SecondDegreePolynomialImpl(this.secondDegree + polynomial.secondDegree,
                                 this.firstDegree + polynomial.firstDegree,
                                 this.constant + polynomial.constant)

    @targetName("minus")
    override def -(polynomial: SecondDegreePolynomial): SecondDegreePolynomial =
      SecondDegreePolynomialImpl(this.secondDegree - polynomial.secondDegree,
                                 this.firstDegree - polynomial.firstDegree,
                                 this.constant - polynomial.constant)

    override def toString: String =
      s"${this.secondDegree} * X^2 + ${this.firstDegree} * X + ${this.constant}"


@main def checkComplex(): Unit =
  val simplePolynomial = SecondDegreePolynomial(1.0, 0, 3)
  val anotherPolynomial = SecondDegreePolynomial(0.0, 1, 0.0)
  val fullPolynomial = SecondDegreePolynomial(3.0, 2.0, 5.0)
  val sum = simplePolynomial + anotherPolynomial
  println((sum, sum.secondDegree, sum.firstDegree, sum.constant)) // 1.0 * X^2 + 1.0 * X + 3.0
  val multipleOperations = fullPolynomial - (anotherPolynomial + simplePolynomial)
  println((multipleOperations, multipleOperations.secondDegree, multipleOperations.firstDegree, multipleOperations.constant)) // 2.0 * X^2 + 1.0 * X + 2.0