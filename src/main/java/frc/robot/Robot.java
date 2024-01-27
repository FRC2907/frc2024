// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {

  double currentTimeStamp = 0;
  double lastTimestamp = 0;
  double dt = 0;
  double matchTime = 0;

  public String m_autoSelected;
  public boolean testinit;

  public static int DRIVE_L1_ID = 3;
  public static int DRIVE_L2_ID = 4;
  public static int DRIVE_R1_ID = 1;
  public static int DRIVE_R2_ID = 2;
  public static int INTAKE_ID = 5;
  public static int SHOOTER_ID = 6;
  public static int ARM_L_ID = 7;
  public static int ARM_R_ID = 8;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {  
    System.gc();
  }

  @Override
  public void autonomousInit() {}
  
  
  // This Might Not Work 
  @Override
  public void autonomousPeriodic() {
    /* 
    if (testinit) {
      // drive->zero_gyro();
      testinit = false;
    }
    
    if (m_autoSelected == "Basic") {
      //this->basic->run();
      this.basic.run();
    } else if (m_autoSelected == "MultiNote") {
      //this->multinote->run();
      this.multinote.run();
    } else if (m_autoSelected == "SendIt") {
      //this->sendit->run();
      this.sendit.run()
    }
    
    else {
      //this->basic->run();
      this.basic.run();
    } W
    */
  }

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {}

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
