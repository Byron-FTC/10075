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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


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

@Autonomous(name="Pushbot: Auto Drive By Time", group="Pushbot")

public class Drive_Auto extends LinearOpMode {

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

        DcMotor.RunMode rMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
        //DcMotor.RunMode rMode = DcMotor.RunMode.RUN_USING_ENCODER;

        motorFrontLeft.setMode(rMode);
        motorFrontRight.setMode(rMode);
        motorBackLeft.setMode(rMode); 
        motorBackRight.setMode(rMode);

        motorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // Raise arm to 0 encoder
        /*motorArm.setDirection(DcMotorSimple.Direction.REVERSE);
        motorArm.setPower(.5);*/

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            //Making the speed of he robot be able to be controled by the joysticks and a trigger.
            double rightPower =  -0.25;
            double leftPower =  -0.25;

            telemetry.addData("RightPower : Should Be Slower", rightPower);
            telemetry.addData("LeftPower", leftPower);

            // Step 1:  Drive forward for 2 seconds
            ExecuteStep(leftPower,rightPower,2, "Leg 1, Drive Forward");

            // Step 2:  Turn 1 second
            ExecuteStep(-leftPower,rightPower,1, "Leg 2, Turn");

            // Step 3:  Drive forward for 2 seconds
            ExecuteStep(leftPower,rightPower,2, "Leg 3, Drive Forward");

            // Step 4:  Turn 2 second
            ExecuteStep(-leftPower,rightPower,1, "Leg 4, Turn");

            // Step 5:  Drive forward for 2 seconds
            ExecuteStep(leftPower,rightPower,2, "Leg 5, Drive Forward");

            // Step 6:  Turn 2 second
            ExecuteStep(-leftPower,rightPower,1, "Leg 6, Turn");

            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }

    private void SetMotorPowers(double left, double right){
        //telemetry.addData("power",  );
        motorBackLeft.setPower(left);
        motorFrontLeft.setPower(left);
        motorBackRight.setPower(right);
        motorFrontRight.setPower(right);
    }

    public void ExecuteStep(double left, double right, double seconds, String description){
        // Step 1:  Drive forward for 2 seconds
        ElapsedTime stepTime = new ElapsedTime();
        SetMotorPowers(left,right);
        stepTime.reset();
        while (opModeIsActive() && (stepTime.seconds() < seconds)) {
            telemetry.addData(description, "Elapsed Time: %2.5f S Elapsed", stepTime.seconds());
            telemetry.update();
            Thread.yield();  // If other threads of work need a chance to do something, let them.
        }


    }
}
