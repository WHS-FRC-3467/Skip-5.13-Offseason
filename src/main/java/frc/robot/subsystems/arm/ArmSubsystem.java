// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;


import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.CanConstants;
import frc.robot.Constants.DIOConstants;

public class ArmSubsystem extends SubsystemBase {
  /** Creates a new ArmSubsystem. */
  private TalonFX m_lowerJoint = new WPI_TalonFX(CanConstants.LOWER_JOINT_MOTOR);
  private TalonFX m_upperJoint = new TalonFX(CanConstants.UPPER_JOINT_MOTOR);

  private DutyCycleEncoder m_upperEncoder = new DutyCycleEncoder(DIOConstants.UPPER_ENCODER_ARM);
  private DutyCycleEncoder m_lowerEncoder = new DutyCycleEncoder(DIOConstants.LOWER_ENCODER_ARM);

  
  public ArmSubsystem() {    
    //Config Duty Cycle Range for the encoders
    m_lowerEncoder.setDutyCycleRange(ArmConstants.DUTY_CYCLE_MIN, ArmConstants.DUTY_CYCLE_MAX);
    m_upperEncoder.setDutyCycleRange(ArmConstants.DUTY_CYCLE_MIN, ArmConstants.DUTY_CYCLE_MAX);

    //Default Motors
    m_lowerJoint.configFactoryDefault(ArmConstants.TIMEOUT);
    m_upperJoint.configFactoryDefault(ArmConstants.TIMEOUT);

    //Set Neutral Mode to Brake and NeutralDeadBand to prevent need for intentional stalling
    m_lowerJoint.setNeutralMode(NeutralMode.Brake);
    m_upperJoint.setNeutralMode(NeutralMode.Brake);

    m_lowerJoint.configNeutralDeadband(ArmConstants.NEUTRAL_DEADBAND);
    m_upperJoint.configNeutralDeadband(ArmConstants.NEUTRAL_DEADBAND);

    m_lowerJoint.configSelectedFeedbackSensor(FeedbackDevice.PulseWidthEncodedPosition, 0, ArmConstants.TIMEOUT);
    m_upperJoint.configSelectedFeedbackSensor(FeedbackDevice.PulseWidthEncodedPosition, 0, ArmConstants.TIMEOUT);

    m_upperJoint.setInverted(TalonFXInvertType.Clockwise);
    m_lowerJoint.setInverted(TalonFXInvertType.Clockwise);

    m_lowerJoint.configNominalOutputForward(ArmConstants.NOMINAL_OUTPUT_FORWARD, ArmConstants.TIMEOUT);
    m_lowerJoint.configNominalOutputReverse(ArmConstants.NOMINAL_OUTPUT_REVERSE, ArmConstants.TIMEOUT);
    m_lowerJoint.configPeakOutputForward(ArmConstants.PEAK_OUTPUT_FORWARD, ArmConstants.TIMEOUT);
    m_lowerJoint.configPeakOutputReverse(ArmConstants.PEAK_OUTPUT_REVERSE, ArmConstants.TIMEOUT);

    m_upperJoint.configNominalOutputForward(ArmConstants.NOMINAL_OUTPUT_FORWARD, ArmConstants.TIMEOUT);
    m_upperJoint.configNominalOutputReverse(ArmConstants.NOMINAL_OUTPUT_REVERSE, ArmConstants.TIMEOUT);
    m_upperJoint.configPeakOutputForward(ArmConstants.PEAK_OUTPUT_FORWARD, ArmConstants.TIMEOUT);
    m_upperJoint.configPeakOutputReverse(ArmConstants.PEAK_OUTPUT_REVERSE, ArmConstants.TIMEOUT);

    m_lowerJoint.configVoltageCompSaturation(12.9, ArmConstants.TIMEOUT);
    m_upperJoint.configVoltageCompSaturation(12.9, ArmConstants.TIMEOUT);

    m_lowerJoint.configFeedbackNotContinuous(true, ArmConstants.TIMEOUT);
    m_upperJoint.configFeedbackNotContinuous(true, ArmConstants.TIMEOUT);

    m_lowerJoint.configForwardSoftLimitEnable(true, ArmConstants.TIMEOUT);
    m_upperJoint.configForwardSoftLimitEnable(true, ArmConstants.TIMEOUT);

    m_upperJoint.configForwardSoftLimitThreshold(ArmConstants.FORWARD_SOFT_LIMIT_UPPER, ArmConstants.TIMEOUT);
    m_upperJoint.configReverseSoftLimitThreshold(ArmConstants.REVERSE_SOFT_LIMIT_UPPER, ArmConstants.TIMEOUT);
    m_lowerJoint.configForwardSoftLimitThreshold(ArmConstants.FORWARD_SOFT_LIMIT_LOWER, ArmConstants.TIMEOUT);
    m_lowerJoint.configReverseSoftLimitThreshold(ArmConstants.REVERSE_SOFT_LIMIT_LOWER, ArmConstants.TIMEOUT);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    m_upperJoint.setSelectedSensorPosition(dutyCycleToCTREUnits(getUpperJointPos()), 0, ArmConstants.TIMEOUT);
    m_lowerJoint.setSelectedSensorPosition(dutyCycleToCTREUnits(getLowerJointPos()), 0, ArmConstants.TIMEOUT);

    if(Constants.tuningMode){
      SmartDashboard.putNumber("Lower Angle", dutyCycleToDegrees(getLowerJointPos()));
      SmartDashboard.putNumber("Upper Angle", dutyCycleToDegrees(getUpperJointPos()));
      SmartDashboard.putNumber("Upper Percent", m_upperJoint.getMotorOutputPercent());
      SmartDashboard.putNumber("Lower Percent", m_lowerJoint.getMotorOutputPercent());
      SmartDashboard.putNumber("Upper Abs", dutyCycleToCTREUnits(getUpperJointPos()));
      SmartDashboard.putNumber("Lower Abs", dutyCycleToCTREUnits(getLowerJointPos()));
      SmartDashboard.putNumber("Upper Current", m_upperJoint.getStatorCurrent());
      SmartDashboard.putNumber("Lower Current", m_lowerJoint.getStatorCurrent());
    }
    else{
      SmartDashboard.clearPersistent("Lower Angle");
      SmartDashboard.clearPersistent("Upper Angle");
      SmartDashboard.clearPersistent("Upper Percent");
      SmartDashboard.clearPersistent("Lower Percent");
      SmartDashboard.clearPersistent("Upper Abs");
      SmartDashboard.clearPersistent("Lower Abs");
      SmartDashboard.clearPersistent("Upper Current");
      SmartDashboard.clearPersistent("Lower Current");
    }
  }

  public void setPercentOutputUpper(double speed){
    m_upperJoint.set(TalonFXControlMode.PercentOutput, speed);
  }

  public void setPercentOutputLower(double speed){
    m_lowerJoint.set(TalonFXControlMode.PercentOutput, speed);
  }

  public void holdPositionUpper(){
    m_lowerJoint.neutralOutput();
  }

  public void holdPositionLower(){
    m_lowerJoint.neutralOutput();
  }

  public double getLowerJointPos(){
    return m_lowerEncoder.getAbsolutePosition();
  }

  public double getUpperJointPos(){
    return m_upperEncoder.getAbsolutePosition();
  }

  public double getLowerJointDegrees(){
    return dutyCycleToDegrees(getLowerJointPos());
  }
  public double getUpperJointDegrees(){
    return dutyCycleToDegrees(getUpperJointPos());
  }

  public double dutyCycleToCTREUnits(double dutyCyclePos){
    //4096 units per rotation = raw sensor units for Pulse width encoder
    return dutyCyclePos * 4096;
  }

  public double dutyCycleToDegrees(double dutyCyclePos) {
    return dutyCyclePos * 360;
  }
}
