package com.github.tharun.datareconciler.plumbing

import com.github.tharun.datareconciler.models.CommandConfig

object JobTypes {
  val reconciler = "reconciler"
}

object CommandFactory {

  def getCommand(commandConfig: CommandConfig): Option[Command] = {
    val jobType = commandConfig.qualityCheckType
    jobType.toLowerCase match {
      case JobTypes.reconciler =>Some(new Reconciler(commandConfig))
    }
  }

}
