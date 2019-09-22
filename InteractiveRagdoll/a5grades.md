# CS349 A4
Student: kthye
Marker: Yuan Chen


Total: 99.0 / 100.0 (99.00%)

Code:
(CO: won’t compile, CR: crashes, FR: UI freezes/unresponsive, NS: not submitted)


Notes:  1.  APK file not submitted  20: tested #1

## Deliverables (5%)

1. [4/5] Code compiles and runs in Android Studio, using the Pixel AVD with API 28.

-5 Code does not compile in Android Studio
-4 There was a problem in the code or project, which the TA had to fix to run the program; details:
-2 Code does not work on the Pixel AVD with API 26; details:
-1 APK file not submitted

## Basic requirements (20%)

2. [5/5] There exists a menu bar at the top of the application window (5).

-5 No menu bar exists at the top of the application window.

3. [5/5] The menu bar contains the "Reset" and "About items".

-3 "Reset" is not in the "File" menu dropdown.
-2 "About" is not in the "File" menu dropdown.

4. [5/5] The "Reset" menu option resets the ragdoll's position entirely to the default.

-5 Reset option does not reset the position of the ragdoll.

5. [5/5] A ragdoll is visible, and is assembled in the rough shape of the doll.

-2 Ragdoll is not visible
-3 Ragdoll is not assembled in the rough shape of the doll.

## Translation (15%)

6. [5/5] The ragdoll's body parts can be translated.

-5 The ragdoll's body parts cannot be translated.

7. [5/5] The ragdoll can be translated with direct manipulation on the torso with movements that match the mouse's movement one-to-one.

-3 The ragdoll cannot be translated with direct manipulation on the torso.
-2 The translations match the mouse's movement one-to-one.

8. [5/5] When the ragdoll is translated by the torso, the child elements of the torso are translated simultaneously (5).

-5 The torso's child elements are not translated with the torso.

## Rotation (20%)

9. [5/5] All body parts of the ragdoll can be rotated (4), except for the torso, which does not rotate (1).

-4 There are body parts of the ragdoll that cannobt be rotated (with the exception of the torso)
-1 The torso can be rotated.

10. [5/5] The parts can be rotated with direct manipulation on the body part (3), with rotations that match the mouse's rotation about the pivot one-to-one.

-3 The ragdoll's parts cannot be rotated with direct manipulation on the body part.
-2 Observed rotations of body parts do not match the mouse's rotation about the pivot one-to-one.

11. [5/5] When the body parts are rotated, the child elements of the body part are rotated simultaneously.

-5 Child elements do not rotate with rotated parent body parts.

12. [5/5] The appropriate body parts have limited rotation, relative to the rotation of their parent element.

-3 Appropriate body parts do not have limited rotation.
-2 Appropriate body parts do not have limited rotation relative to their parent body part / element.

## Scaling (20%)

13. [5/5] The legs of the ragdoll can be scaled, and no other body parts can be.

-4 The ragdoll's legs cannot be scaled.
-1 The ragdoll's other body parts can be scaled.


14. [5/5] The legs can be scaled with direct manipulation, with scaling that matches the mouse's distance from the pivot.

-3 The ragdoll's legs cannot be scaled with direct manipulation.
-2 The scaling of the ragdoll's legs matches the mouse's distance from the pivot.

15. [5/5] When the upper legs are scaled, the lower legs are scaled as well, but the feet are not scaled.

-4 Lower legs do not re-scale when upper legs are re-scaled.
-1 Feet scale with the other body parts.

16. [5/5] The legs scale along their primary axis, even when the lower leg has been rotated relative to the upper leg.

-3 The legs do not scale along their primary axis.
-2 The legs do not scale along their primary axis when the lower leg has been rotated relative to the upper leg.

## Robustness (10%)

17. [2/2] Grabbing a body part for direct manipulation does not cause the body part to change at all until the mouse begins to move.

-2 Grabbing a body part causes it to change without moving the mouse.

18. [5/5] The movement of body parts is smooth while they're being directly manipulated within constraints. (It is acceptable for the body part to "pop" if you move past one constraint and back into range.)

-5 The movement of body parts is not smooth while being directly manipulated.

19. [3/3] When a body part is manipulated, the child elements of the body part are also updated smoothly.

-3 Child elements are not updated smoothly when a parent body part is manipulated.

## Chosen Feature (10%)

20. [10/10] One of the following options is implemented:

    #1 - Custom Textures: The ragdoll has textures applied to all body parts of the ragdoll that transform properly and are aesthetically pleasing.

        -4 The ragdoll does not have textures applied to all body parts.
        -3 The ragdoll's textures do not transform properly.
        -3 The ragdoll's textures are not aesthetically pleasing.

    #2 - Ragdoll / Animation Playback: There is a timeline in the window with a cursor that can be moved (1). There is a keyframe button (1) and a playback button (1). Pressing the keyframe button adds a pose to the timeline somehow, such that moving the playback cursor moves between poses (3). Pressing the playback button moves the ragdoll between the keyframes on the timeline (3). When the cursor is between keyframes, there is a smooth transition between those two keyframes (2).

        -1 There is no timeline in the window with a moveable cursor.
        -1 There is neither a keyframe or a playback button.
        -3 Pressing the keyframe button does not add a pose to the timeline.
        -3 Pressing the playback button does not move the ragdoll between keyframes on the timeline.
        -2 When the cursor is moving between keyframes, the ragdoll's body parts do not smoothly transition.

    #3 - Multiple Ragdolls: There is a second menu (2) containing a list of at least three ragdolls (2), including the initial human-shaped ragdoll (1). The additional ragdolls are not human-shaped (5) and have their own rotation constraints (5).

        -1 There is not a second menu.
        -1 There is not a list of additional ragdolls to select from.
        -1 The "human-shaped" ragdoll is not in the list of available ragdolls to choose from.
        -4 The additional ragdolls are human-shaped.
        -3 The additional ragdolls do not have their own rotation constraints.