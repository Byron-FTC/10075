/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Drive", group="Linear Opmode")  // @Magic(...) is the other common choice


public class Drive extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorArm;

    TouchSensor upS;
    TouchSensor downS;

    int iUpperArmPosition;
    int iBallControl;
    int iAquire;
    int iBeacon;
    int iBallControlOffset=200;
    int iAquireOffset=222;
    int iBeaconOffset=400;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        motorBackLeft = this.hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = this.hardwareMap.dcMotor.get("motorBackRight");
        motorFrontLeft = this.hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = this.hardwareMap.dcMotor.get("motorFrontRight");
        motorArm = hardwareMap.dcMotor.get("motorArm");


        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery

        DcMotor.RunMode rMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
        //DcMotor.RunMode rMode = DcMotor.RunMode.RUN_USING_ENCODER;

        motorFrontLeft.setMode(rMode);
        motorFrontRight.setMode(rMode);
        motorBackLeft.setMode(rMode);
        motorBackRight.setMode(rMode);

        motorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // Raise arm to 0 encoder
        motorArm.setDirection(DcMotorSimple.Direction.REVERSE);
        motorArm.setPower(.5);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            float trigger = gamepad1.right_trigger;

            //Making the speed of he robot be able to be controled by the joysticks and a trigger.
            float rightPower = CalculatePower(trigger, gamepad1.right_stick_y);
            float leftPower = CalculatePower(trigger, gamepad1.left_stick_y);

            //right motors to fast so we are slowing it down
            rightPower = rightPower/100;
            rightPower = rightPower*95;

            telemetry.addData("RightPower : Should Be Slower", rightPower);
            telemetry.addData("LeftPower", leftPower);

            // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
            motorFrontLeft.setPower(leftPower);
            motorBackLeft.setPower(leftPower);
            motorBackRight.setPower(rightPower);
            motorFrontRight.setPower(rightPower);

            upS = hardwareMap.touchSensor.get("up");
            downS = hardwareMap.touchSensor.get("down");
            telemetry.addData("ups is pressed?", upS.isPressed());
            telemetry.addData("downs is pressed?", downS.isPressed());

            telemetry.addData("ArmPosition",motorArm.getCurrentPosition());

            if (upS.isPressed()) {
                iUpperArmPosition = motorArm.getCurrentPosition();
                iBallControl = iUpperArmPosition + iBallControlOffset;
                if (iBallControl < 1450) { iBallControl = iBallControl - 1450; }
                iAquire = iUpperArmPosition + iAquireOffset;
                if (iAquire < 1450) { iAquire = iAquire - 1450; }
                iBeacon = iUpperArmPosition + iBeaconOffset;
                if (iBeacon < 1450) { iBeacon = iBeacon - 1450; }
                if (gamepad2.right_stick_y < 0)
                    motorArm.setPower(0);
                else {
                    motorArm.setPower(gamepad2.right_stick_y);
                }
            } else if (downS.isPressed()) {
                if (gamepad2.right_stick_y > 0)
                    motorArm.setPower(0);
                else {
                    motorArm.setPower(gamepad2.right_stick_y);
                }
            } else {
                motorArm.setPower(gamepad2.right_stick_y);
            }



            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }

    public float CalculatePower(float trigger, float stick){
        final int stickDevisor = 4;
        final int trigDevisor = 2;

        // if (trigger > 0) trigBoost = trigger / trigDevisor, else trigBoost = 0
        float trigBoost = (trigger > 0) ? trigger / trigDevisor : 0;

        // if (stick < 0) then trigBoost = - trigBoost, else trigBoost = trigboost.
        trigBoost = (stick < 0) ? -trigBoost : trigBoost;

        // if (stick unequal to 0) then power = (stick / stickDevisor) + trigBoost) else
        //   power = 0
        float power = (stick != 0) ? ((stick / stickDevisor) + trigBoost) : 0;

        return power;
    }
}
