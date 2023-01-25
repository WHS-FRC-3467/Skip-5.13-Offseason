// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ArmConstants;

public class ArmRioPID extends CommandBase {
  /** Creates a new ArmRioPID. */
  ArmSubsystem m_arm;
  double m_position;

  TrapezoidProfile.Constraints m_trapProfile = new TrapezoidProfile.Constraints(ArmConstants.MOTION_CRUISE_VELOCITY_UPPER, ArmConstants.MOTION_ACCELERATION_UPPER);

  private ProfiledPIDController m_controller = new ProfiledPIDController(ArmConstants.GAINS_UPPER_JOINT.kP, ArmConstants.GAINS_UPPER_JOINT.kI, ArmConstants.GAINS_UPPER_JOINT.kD, m_trapProfile);
  
  public ArmRioPID(ArmSubsystem arm, double position) {
    m_arm = arm;
    m_position = position;
    addRequirements(m_arm);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_controller.setGoal(m_position);
    m_controller.disableContinuousInput();
    m_controller.setTolerance(10);
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double encoderVal = m_arm.dutyCycleToDegrees(m_arm.getUpperJointPos());
    double outPut = m_controller.calculate(encoderVal);
    double error = m_controller.getPositionError();
    MathUtil.clamp(outPut, -0.2, 0.2);
    m_arm.setPercentOutputUpper(-outPut);
    
    System.out.println("Encoder Value Degrees " + encoderVal);
    System.out.println("PID Output " + outPut);
    System.out.println("Arm error " + error);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_arm.holdPositionUpper();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_controller.atGoal();
  }
}
