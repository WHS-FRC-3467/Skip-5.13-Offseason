# Skip-5.13-Offseason
This is the official repo for the code of Midas and Midnight <br>
To get the code to deploy to the robot open just the folder of the robot you want in VS code <br>
If not you won't be able to deploy to the robot because of multiple gradle files <br>
You can push changes for the robot to the remote even if you only have one of the folders open <br>
The constants for offsets are at the top of the constants file <br>
To change the offsets for the swerve module turn the robot on and put it on its side and face all of the bevel gears of towards the side labeled on the bellypan <br>
Use a piece of box tube to make sure all of the wheels are aligned <br>
Then open up the shuffleboard and connect to the robot (Do not enable the robot) <br>
Find the boxes on the shuffleboard that are labeled cancoders 0-3 and copy their values into the constants file <br>
Deploy the code to the robot and check if the wheels are driving straight <br>
If not try and do the offsets again <br>
To change the offsets for the arm turn the robot off and push the upperjoint against its hard stop in the stowed position <br>
Then put the lower joint at vertical and use a piece of box tube to make sure the lower joint is in line with the A-frame <br>
Then turn the robot on and connect to the robot (Do not enable) <br>
Open shuffleboard and find the box with the values for Upper and Lower Joint uncorrected <br>
Copy these values into the offsets file <br>
Upper arm should be between 5-60 degrees (It should never read anything over 300 and likely means that the encoder zero point is not in the right spot) and the lower arm should be between 150-200 <br>
Then deploy code to the robot <br>
If the values for the upper or lower angles corrected are 0 or a negative value do not enable and try to do the offsets again <br>
If they are still negative or zero. then there is an issue with the encoder. <br>
