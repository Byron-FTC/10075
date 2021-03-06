/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name = "Sensor: MR Color", group = "Sensor")

public class SensorMRColor extends LinearOpMode {

  ColorSensor colorSensor;    // Hardware Device Object
  TouchSensor touchSensor;  // Hardware Device Object

  @Override
  public void runOpMode() throws InterruptedException {
    boolean bledOn = true;

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F,0F,0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // get a reference to our ColorSensor object.
    colorSensor = hardwareMap.colorSensor.get("color sensor");
    touchSensor = hardwareMap.touchSensor.get("touch sensor");

    // Set the LED in the beginning
    colorSensor.enableLed(bledOn);

    // wait for the start button to be pressed.
    waitForStart();

    // while the op mode is active, loop and read the RGB data.
    // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
    while (opModeIsActive()) {
      bledOn = touchSensor.isPressed();
      colorSensor.enableLed(bledOn);

      int red = colorSensor.red();
      int green = colorSensor.green();
      int blue = colorSensor.blue();
      String promColor = "None";
      // convert the RGB values to HSV values.
      Color.RGBToHSV(red * 8, green * 8, blue * 8, hsvValues);
      if ((red > green) && (red > blue))  { promColor = "Red" ; }
      if ((green > red) && (green > blue))  { promColor = "Green" ; }
      if ((blue > red) && (blue > green))  { promColor = "Blue" ; }


      // send the info back to driver station using telemetry function.
      telemetry.addData("LED", bledOn ? "On" : "Off");
      telemetry.addData("Prominent Color", promColor);
      telemetry.addData("Clear", colorSensor.alpha());
      telemetry.addData("Red  ", colorSensor.red());
      telemetry.addData("Green", colorSensor.green());
      telemetry.addData("Blue ", colorSensor.blue());
      telemetry.addData("Hue", hsvValues[0]);

      telemetry.update();
      idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
    }
  }
}
