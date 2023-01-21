// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.limelight;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.LimelightConstants;
import frc.robot.subsystems.drive.DriveSubsystem;

public class AlignWithConeNode extends CommandBase {
  /** Creates a new AlignWithVisionTape. */
  LimelightSubsystem m_limelight;
  DriveSubsystem m_drive;
  PIDController m_pidControllerY, m_pidControllerX;
  boolean m_end;
  double xTrans = 0.0;
  double yTrans = 0.0;
  /**
   * 
   * @param limelight Limelight Subsystem
   * @param drive DriveSubsystem
   * @param level Level of the node valid: "mid", "top"
   */
  public AlignWithConeNode(LimelightSubsystem limelight, DriveSubsystem drive) {
    m_limelight = limelight; 
    m_drive = drive;
    addRequirements(m_limelight, m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_pidControllerY = new PIDController(LimelightConstants.GAINS_VISION_Y.kP, LimelightConstants.GAINS_VISION_Y.kI, LimelightConstants.GAINS_VISION_Y.kD);
    m_pidControllerY.setIntegratorRange(0.0, LimelightConstants.GAINS_VISION_Y.kIzone);
    m_pidControllerY.setTolerance(LimelightConstants.VISION_POS_TOLLERANCE, LimelightConstants.VISION_VEL_TOLLERANCE);

    m_pidControllerX = new PIDController(LimelightConstants.GAINS_VISION_X.kP, LimelightConstants.GAINS_VISION_X.kI, LimelightConstants.GAINS_VISION_X.kD);
    m_pidControllerX.setIntegratorRange(0.0, LimelightConstants.GAINS_VISION_X.kIzone);
    m_pidControllerX.setTolerance(LimelightConstants.VISION_POS_TOLLERANCE, LimelightConstants.VISION_VEL_TOLLERANCE);

    m_end = false;
    m_limelight.setPipeline(2);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_limelight.hasTarget() == false){
      m_end = true;
      System.out.println("no target");
    }

    m_pidControllerY.setSetpoint(LimelightConstants.SETPOINT_DIS_FROM_MID_CONE);
    m_pidControllerX.setSetpoint(LimelightConstants.ALIGNED_CONE_X);
    xTrans = m_pidControllerX.calculate(m_limelight.getX());
    xTrans = Math.max(xTrans, 1.0);
    xTrans = Math.min(xTrans, -1.0);

    yTrans = m_pidControllerY.calculate(m_limelight.getDistanceFromTarget(LimelightConstants.SETPOINT_DIS_FROM_MID_CONE));
    yTrans = Math.max(yTrans, 1.0);
    yTrans = Math.min(yTrans, -1.0);
    m_drive.drive(new Translation2d(xTrans, yTrans), 0.0, true, true);

    if(m_pidControllerX.atSetpoint() && m_pidControllerY.atSetpoint()){
      m_end = true;
    }
    else{
      m_end = false;
    }
      
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.stopDrive();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_end;
  }
}