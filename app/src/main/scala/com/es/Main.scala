package com.es

import cats.effect.std.Dispatcher
import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    Dispatcher[IO].use { implicit dispatcher =>
      for {
        contextRef <- IO.ref(Context(None)) //FIXME: use context
        form <- MainForm(contextRef)
        _ <- form.show
        _ <- IO(println("bye bye"))
      } yield ExitCode.Success
    }
}
