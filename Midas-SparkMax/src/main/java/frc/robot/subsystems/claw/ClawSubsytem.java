// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.claw;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CanConstants;

public class ClawSubsytem extends SubsystemBase {
  /** Creates a new ClawSubsytem. */
  private CANSparkMax m_clawMotor = new CANSparkMax(CanConstants.CLAW_MOTOR, MotorType.kBrushless);
  public ClawSubsytem() {
    m_clawMotor.restoreFactoryDefaults();
    m_clawMotor.setOpenLoopRampRate(0.2);
    m_clawMotor.enableVoltageCompensation(12.0);
    m_clawMotor.setSmartCurrentLimit(10, 10);
    m_clawMotor.setIdleMode(IdleMode.kBrake);
    m_clawMotor.setInverted(true);
  }

  
  @Override
  public void periodic() {
    if(Constants.tuningMode){
      SmartDashboard.putNumber("Claw Current", getClawCurrent());
    }
    // This method will be called once per scheduler run
  }
  public void driveClaw(double speed){
    m_clawMotor.set(speed);
  }
  public double getClawCurrent(){
    return m_clawMotor.getOutputCurrent();
  }
  
}
