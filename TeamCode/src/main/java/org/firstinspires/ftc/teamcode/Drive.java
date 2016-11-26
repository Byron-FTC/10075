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

        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
       motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorArm = hardwareMap.dcMotor.get("motorArm");

        motorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        //Taking the game pad input and squaring it to give better control
        float rightPower;
        //rightPower = rightPower*rightPower*rightPower;

        float leftPower;
        //leftPower = leftPower*leftPower*leftPower;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            float trigger;
            trigger = gamepad1.right_trigger;

            //Making the speed of he robot be able to be controled by the joysticks and a trigger.
            rightPower = gamepad1.right_stick_y;
            rightPower = (((trigger + 1)/2)*rightPower);
            rightPower = rightPower*Math.abs(rightPower);

            leftPower = gamepad1.left_stick_y;
            leftPower = (((trigger + 1)/2)*leftPower);
            leftPower = leftPower*Math.abs(leftPower);


            telemetry.addData("RightPower",rightPower);
            telemetry.addData("LeftPower",leftPower);

            // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
            motorFrontLeft.setPower(-leftPower);
            motorFrontRight.setPower(-rightPower);
            motorBackLeft.setPower(-leftPower);
            motorBackRight.setPower(-rightPower);

            upS = hardwareMap.touchSensor.get("up");
            downS = hardwareMap.touchSensor.get("down");

            if (upS.isPressed()) {
                if (gamepad2.right_stick_y > 0)
                    motorArm.setPower(0);
                    else{
                    motorArm.setPower(gamepad2.right_stick_y);
                }
            }
                else

                if (downS.isPressed()) {
                    if (gamepad2.right_stick_y < 0)
                        motorArm.setPower(0);
                    else{
                        motorArm.setPower(gamepad2.right_stick_y);
                    }
                }
                else {
                    motorArm.setPower(gamepad2.right_stick_y);
                }



            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }
}
