//////////////////////////////////
///    CX7, frame version 1    ///
///   Author: Maxim Buzdalov   ///
///    For "Wow, That Flies"   ///
/// License: WTF Public License //
//////////////////////////////////

// This is a very lightweight coax copter with standard arrangement of flaps.
// Apart from the details presented in this file,
// an important structural part is a piece of plastic bottle
// which forms the duct surface glued to the internal wall of thrust pucks.

// THE DRAWINGS ARE PRESENTED AS THEY ARE!
// THERE WERE NO ATTEMPTS TO OPTIMIZE THE CODE FOR UNDERSTANDING!
// Maybe one day I will do it.

// The motor mount is designed specifically for 
// Ummagawd Aerolite 2004 Motor (2400 kV), so
// you will have to adjust the motor mounts for the actual motors you use.
// Also check the servo holes.

// The batteries in this machine were 12 vape LiPo cells 360 mAh each,
// glued on the outer perimeter of the plastic bottle duct to even out the
// center of gravity. You use what you have, but CoG must be very well centered.

// Meta-parameters
$fa = 1;   // discretization angle 
$fs = 0.1; // minimum segment length

// General geometry
eps = 0.01;   // negligible but positive thickness
inf = 10000;  // a very large length

// Standard holes
m2HoleR = 1.1;
m3HoleR = 1.75;
m4HoleR = 2.2;

// Directions
EX = [+1, +1, -1, -1];
EY = [+1, -1, -1, +1];

propR = 64;

puckInnerR = 64.5;
puckOuterR = puckInnerR + 3;
puckMountR = puckOuterR + 3;
puckHandW = 6;
puckThick = 3;

lowPoint = 40;

wireHoleW = 4;
wireHoleH = 3;
wireHoleR = 2.4;

interRingH = 45;
legThick = 3;
legW = 6;

flapThick = 1.5;
servoHoleH = -lowPoint + legThick + 20;

module puck() {
    linear_extrude(puckThick, convexity = 5) {
        difference() {
            circle(puckOuterR);
            circle(puckInnerR);
        }
        difference() {
            union() {
                square([2 * puckMountR, puckHandW], center = true);
                square([puckHandW, 2 * puckMountR], center = true);
                scale([0.8, 1.3]) circle(10);
            }
            circle(2.5);
            translate([0, +6]) circle(m2HoleR);
            translate([0, -6]) circle(m2HoleR);
        }
    }
}

module legBase() {
    difference() {
        union() {
            // main trunk
            translate([-legW / 2, -lowPoint]) 
                square([legW, lowPoint + interRingH]);
            // bottom part should be thicker
            translate([-legW / 2 - legThick, -lowPoint]) 
                square([legW + 2 * legThick, lowPoint]);
            // bottom puck mount point
            polygon([
                [-puckHandW / 2 - legThick, -puckThick - legThick],
                [-puckHandW / 2 - legThick, +legThick],
                [-legW / 2, +2 * legThick],
                [+legW / 2, +2 * legThick],
                [+puckHandW / 2 + legThick, +legThick],
                [+puckHandW / 2 + legThick, -puckThick - legThick]
            ]);
            // top puck mount point
            translate([0, interRingH]) 
            polygon([
                [-puckHandW / 2 - legThick, +puckThick + legThick],
                [-puckHandW / 2 - legThick, -legThick],
                [-legW / 2, -2 * legThick],
                [+legW / 2, -2 * legThick],
                [+puckHandW / 2 + legThick, -legThick],
                [+puckHandW / 2 + legThick, +puckThick + legThick]
            ]);
        }
        // bottom puck main hole
        translate([0, -puckThick / 2]) 
            square([puckHandW + 0.2, puckThick + 0.2], center = true);
        // bottom wire hole
        translate([0, 2]) 
            circle(wireHoleR);
        // top puck main hole
        translate([0, interRingH + puckThick / 2]) 
            square([puckHandW + 0.2, puckThick + 0.2], center = true);
        // top wire hole
        translate([0, interRingH - 2]) 
            circle(wireHoleR);
    }
}

module fcMount() {
    for (dy = [(interRingH - 25) / 2 + 5, (interRingH + 25) / 2 + 5]) {
        difference() {
            union() {
                translate([-25 / 2, dy]) circle(2.5);
                translate([+25 / 2, dy]) circle(2.5);
                translate([-25 / 2, dy - 2.5]) square([25, 5]);
            }
            translate([-25 / 2, dy]) circle(m2HoleR);
            translate([+25 / 2, dy]) circle(m2HoleR);
        }
    }
}

module servoMountMinus() {
    translate([-4.2, -lowPoint + legThick + 4.5]) 
        square([8.4, 20]);
    translate([0, -lowPoint + legThick + 2])
        circle(m2HoleR);
    translate([0, -lowPoint + legThick + 27])
        circle(m2HoleR);
}

module antiServoMountMinus() {
    translate([0, servoHoleH])
        circle(m2HoleR);
}

module bwdLeg() {
    linear_extrude(legThick, convexity = 5) {
        difference() {
            legBase();
            antiServoMountMinus();
        }
        translate([-20, interRingH / 2 - 2]) square([40, 4]);
        translate([-20, interRingH / 2 - 10]) square([14, 20]);
        translate([-20, interRingH / 2 - 10]) square([14, 20]);
        translate([+6, interRingH / 2 - 10]) square([14, 20]);
    }
}

module fwdLeg() {
    linear_extrude(legThick, convexity = 5) {
        difference() {
            union() {
                legBase();
                fcMount();
            }
            servoMountMinus();
        }
    }
}

module rightLeg() {
    linear_extrude(legThick, convexity = 3) {
        difference() {
            legBase();
            servoMountMinus();
        }
    }
}

module leftLeg() {
    linear_extrude(legThick, convexity = 3) {
        difference() {
            legBase();
            antiServoMountMinus();
        }
    }
}

module servoAdapter() {
    difference() {
        // body
        union() {
            translate([-4, -11, 0]) 
                cube([8, 12, 5]);
            translate([0, -11, 0])
                cylinder(h = 5, r = 4);
            translate([0, 1, 0])
                cylinder(h = 5, r = 4);
        }
        // cut for the flap
        translate([-flapThick / 2 - 0.1, -15 - eps, -eps]) 
            cube([flapThick + 0.2, 20 + 2 * eps, 2 + eps]);
        // cut for the axis
        translate([0, 0, -eps])
            cylinder(r = 3.1, h = 5 + 2 * eps);
        // cut for the hand, rect part
        translate([-2, -12.5, 4.4])
            cube([4, 12.5, 0.7]);
        // cut for the hand, round part
        translate([0, -12.5, 4.4])
            cylinder(r = 2, h = 0.7);
    }
}

module antiServoAdapter() {
    difference() {
        cylinder(h = 3, r = 3);
        translate([0, 0, -eps]) cylinder(h = 3 + 2 * eps, r = 1.2);
        translate([-flapThick / 2 - 0.1, -3 - eps, -eps])
            cube([flapThick + 0.2, 6 + 2 * eps, 2 + eps]);
    }
}

servoMountR = puckOuterR - 10.5;
antiServoMountR = puckOuterR - 3.8;

module pitchFlap() {
    translate([0, 0, -flapThick / 2])
    linear_extrude(flapThick, convexity = 3)
    polygon([
        [-56, -30],
        [-56, -20],
        [-antiServoMountR, -20],
        [-antiServoMountR, -1.2],
        [-antiServoMountR + 4.2, -1.2],
        [-antiServoMountR + 4.2, +1.2],
        [-antiServoMountR, +1.2],
        [-antiServoMountR, +10],
        [servoMountR, +10],
        [servoMountR, -30],
        [+19, -30],
        [+19, -17],
        [0, 2],
        [-19, -17],
        [-19, -30]
    ]);
}

module rollFlap() {
    translate([0, 0, -flapThick / 2])
    linear_extrude(flapThick, convexity = 3)
    polygon([
        [-56, -30],
        [-56, -20],
        [-antiServoMountR, -20],
        [-antiServoMountR, -1.2],
        [-antiServoMountR + 4.2, -1.2],
        [-antiServoMountR + 4.2, +1.2],
        [-antiServoMountR, +1.2],
        [-antiServoMountR, +10],
        [-15, +10],
        [0, -5],
        [+15, +10],
        [servoMountR, +10],
        [servoMountR, -30],
        [+31, -30],
        [+23, -30],
        [+22, -25],
        [-22, -25],
        [-23, -30],
        [-31, -30],
    ]);
}

module cameraMount() {
    hth = 1.2;
    x0 = puckHandW / 2 + 0.2 + hth;
    x1 = 7 + 0.1;
    x2 = x1 + hth;

    rotate([90, 0, 0])
    difference() {
        linear_extrude(10, convexity = 3) {
            polygon([
                [-x0, -3],
                [-x0, +2],
                [-x2, +5],
                [-x2, +22],
                [-x1, +22],
                [-x1, +6],
                [-x0, +3.85],
                [+x0, +3.85],
                [+x1, +6],
                [+x1, +22],
                [+x2, +22],
                [+x2, +5],
                [+x0, +2],
                [+x0, -3],
                [+x0 - hth, -3],
                [+x0 - hth, +0.1],
                [+x0 - 2 * hth, +0.1],
                [+x0 - 2 * hth, +3 - hth],
                [-x0 + 2 * hth, +3 - hth],
                [-x0 + 2 * hth, +0.1],
                [-x0 + hth, +0.1],
                [-x0 + hth, -3],
            ]);
        }
        translate([0, 16.5, 5])
            rotate([0, 90, 0])
            translate([0, 0, -x2 - eps])
            cylinder(h = 2 * (x2 + eps), r = 0.8);
    }
}

module cameraAntMount() {
    hth = 1.2;
    x0 = puckHandW / 2 + 0.2 + hth;

    rotate([90, 0, 0])
    difference() {
        linear_extrude(10, convexity = 2) {
            polygon([
                [-x0, -3],
                [-x0, +5],
                [-4, +6],
                [-4, +15],
                [+4, +15],
                [+4, +6],
                [+x0, +5],
                [+x0, -3],
                [+x0 - hth, -3],
                [+x0 - hth, +0.1],
                [+x0 - 2 * hth, +0.1],
                [+x0 - 2 * hth, +3 - hth],
                [-x0 + 2 * hth, +3 - hth],
                [-x0 + 2 * hth, +0.1],
                [-x0 + hth, +0.1],
                [-x0 + hth, -3],
            ]);
        }
        translate([0, 10, -0.1])
            rotate([0, 0, 0])
            cylinder(h = 5.1, r = 2.7);
        translate([0, 10, 4.9])
            rotate([0, 0, 0])
            cylinder(h = 5.2, r = 1.7);
    }
}

color("blue") {
    // lower puck
    scale([1, 1, -1]) puck();
    // upper puck
    translate([0, 0, interRingH]) puck();
}

// legs
color("orange") 
    translate([0, -puckOuterR, 0])
    rotate([90, 0, 0])
    bwdLeg();
rotate([0, 0, 180])
    translate([0, -puckOuterR, 0])
    rotate([90, 0, 0])
    fwdLeg();
rotate([0, 0, 90])
    translate([0, -puckOuterR, 0])
    rotate([90, 0, 0])
    rightLeg();
rotate([0, 0, -90])
    translate([0, -puckOuterR, 0])
    rotate([90, 0, 0])
    leftLeg();

color("red") {
    // fwd servo adapter
    translate([0, servoMountR - 1.9, servoHoleH])
        rotate([-90, 180, 0])
        servoAdapter();
        
    // right servo adapter    
    translate([servoMountR - 1.9, 0, servoHoleH])
        rotate([-90, 180, -90])
        servoAdapter();

    // bwd antiservo adapter
    translate([0, -antiServoMountR + 1.7, servoHoleH])
        rotate([-90, 180, 180])
        antiServoAdapter();

    // left antiservo adapter
    translate([-antiServoMountR + 1.7, 0, servoHoleH])
        rotate([-90, 180, 90])
        antiServoAdapter();
}

color("green") {
    // pitch flap
    translate([0, 0, servoHoleH])
        rotate([90, 0, 0])
        pitchFlap();

    // roll flap
    translate([0, 0, servoHoleH])
        rotate([90, 0, 90])
        rollFlap();
}

color("green")
    translate([0, 40, interRingH + 0.1 + puckThick])
    cameraMount();
color("green")
    translate([0, -40, interRingH + 0.1 + puckThick])
    cameraAntMount();
