/// T3C, frame version 1
/// Author: Maxim Buzdalov
/// For "Wow, That Flies"
/// License: WTF Public License

/// T3C is a tiltrotor tricopter
/// with two motors in the front,
/// one motor in the back,
/// all connected to servos that tilt them
/// in the forward-backward direction.

/// We have five degrees of freedom:
/// 1) Pitch is controlled by difference in thrust
///     between the forward and the backward motor groups,
///     and is usually kept at zero.
/// 2) Roll is controlled by difference in thrust
///     between the left and the right forward motors.
/// 3) Yaw is controlled by tilting the forward motors in opposite directions.
/// 4) Thrust is, as usual, the sum of all motors.
/// 5) Speed forward/backward is controlled
///     by the cumulative tilt of all the motors.

// Big global switches go here
is_for_rendering = false;

// Shaping constants
$fs = is_for_rendering ? 0.1 : 1;
$fa = 1;
eps = 1e-3;

// Duct configuration
propR = 45.5; // the internal radius, with a very small gap to the actual propeller
ductTopR = 8; // the inlet radius
ductBotW = 2; // the outlet face size
ductH = 35; // the total height of the entire assembly
ductTriW = propR + ductTopR;
puckHUp = 6;
puckHDown = 3;
puckW = 8;
ductOuterR = propR + 2 * ductTopR;

// Body configuration
bodyHW = 25;
fwdCenter = ductOuterR + bodyHW + 4;
bwdOffset = -fwdCenter / tan(30);

frameLow = -11;
frameHeight = 24;

// The duct shape without cutouts, centered at (0,0), fully above 0.
module ductShape() {
    rotate_extrude() {
        hull() {
            translate([propR, 0]) square([ductBotW, ductBotW]);
            translate([propR + ductTopR, ductH - ductTopR]) circle(ductTopR);
        }
    }
}

// The duct shape with cutouts, centered at (0,0), fully above 0.
module duct() {
    difference() {
        // The donut
        ductShape();
        // The rectangular cutout along the entire diameter
        translate([-ductOuterR, -puckW / 2 - 0.05, ductH - puckHUp])
            cube([2 * ductOuterR, puckW + 0.1, puckHUp + eps]);
        // The triangular cutout along part of the diameter
        translate([0, 0, ductH + eps - puckHUp])
            scale([1, 1, -1])
            linear_extrude(height = puckHDown, scale = [1, 0])
            square([2 * (propR + ductTopR) + 0.1, puckW + 0.1], center = true);
    }
}

// The common code for both types of arm.
module armBase() {
    difference() {
        union() {
            // main arm
            translate([5, -puckW / 2, -puckHUp]) 
                cube([ductOuterR + fwdCenter, puckW, puckHUp]);
            // the triangular aerodynamic addon
            translate([fwdCenter, 0, -puckHUp])
                scale([1, 1, -1])
                linear_extrude(height = puckHDown, scale = [1, 0])
                square([2 * (propR + ductTopR), puckW], center = true);
            // the limiter and force transmitter for the servo arm force receiver
            translate([ductOuterR + fwdCenter, -puckW / 2 - 2, -puckHUp])
                cube([2, puckW + 4, puckHUp]);
            // the limiter and force transmitter for the servo arm force receiver
            translate([bodyHW + 2, -puckW / 2 - 2, -puckHUp])
                cube([2, puckW + 4, puckHUp]);
            // motor mountpoints
            translate([fwdCenter, 0, -puckHUp])
            for (a = [0:3])
                rotate([0, 0, 45 + 90 * a])
                translate([6, 0, 0])
                cylinder(h = puckHUp, r = 3);
        }
        
        // motor mountpoints
        translate([fwdCenter, 0, -puckHUp - eps])
        for (a = [0:3])
            rotate([0, 0, 45 + 90 * a])
            translate([6, 0, 0])
            cylinder(h = puckHUp + 2 * eps, r = 1.2);

        // space for the motor itself
        translate([fwdCenter, 0, -puckHUp - puckHDown - eps])
            cylinder(h = puckHDown + 2 * eps, r = 11);
        
        // space for the motor axis
        translate([fwdCenter, 0, -puckHUp - eps])
            cylinder(h = 2, r = 3);
        
        // space for the motor wire cover
        translate([fwdCenter - 20, -puckW / 2 - eps, -puckHUp - puckHDown - eps])
            cube([20, puckW + 2 * eps, puckHDown + eps]);
            
        // in-hand wire channel
        translate([0, 0, -puckHUp/2])
            rotate([0, 90, 0])
            cylinder(r = 1.8, h = fwdCenter - 19);
            
        // the slanted channel 
        translate([fwdCenter - 13, 0, -puckHUp - puckHDown])
            rotate([0, -65, 0])
            cylinder(r1 = 3, r2 = 1.8, h = 14);
    }
}

// One of the two front arms (the right-hand-side one).
module frontArm() {
    difference() {
        armBase();
        
        // the cutout on the outer side
        difference() {
            translate([fwdCenter + ductOuterR - ductTopR, -puckW / 2 - 2.1, -puckHUp - 0.1])
                cube([ductTopR + 6.1, puckW + 4.2, puckHUp + 0.2]);
            translate([fwdCenter, 0, -ductH]) ductShape();
        }
    
        // the funny thing to fix the inner wheel
        translate([8, 0, -puckHUp / 2])
            rotate([0, 90, 0])
            linear_extrude(4.1, convexity = 2)
            difference() {
                circle(10);
                circle(3);
            }
    }
}

// The only rear arm.
module rearArm() {
    translate([-fwdCenter, 0, 0])
    difference() {
        armBase();
        translate([0, -puckW / 2 - eps, -puckHUp - eps])
            cube([fwdCenter - ductOuterR - 5, puckW + 2 * eps, puckHUp + 2 * eps]);
    }
}

// The front arm is mounted onto the frame via two "wheels" that get attached to the arm
// and then pressed against the matching parts of the frame.
// This is the outer wheel.
module frontArmOuterWheel() {
    rotate([0, 90, 0]) difference() {
        union() {
            // the cylinder that will mainly take the force agains the frame
            cylinder(h = 4, r1 = 6, r2 = 10);
            // the cylinder that will be mostly outside
            translate([0, 0, 4]) cylinder(h = 4, r1 = 10, r2 = 8);
            // the pieces that will take the rods
            for (dx = [-1, +1])
                translate([dx * 12, 0, 6]) cylinder(h = 2, r = 2);
            translate([-12, -2, 6]) cube([24, 4, 2]);
        }
        // the hole for the arm
        translate([-puckHUp / 2 - 0.1, -puckW / 2 - 0.1, -eps])
            cube([puckHUp + 0.2, puckW + 0.2, 8 + 2 * eps]);
        // the hole for the stopper
        translate([-puckHUp / 2 - 0.1, -puckW / 2 - 2.1, 6])
            cube([puckHUp + 0.2, puckW + 4.2, 2 + eps]);
        // the holes for the rods
        for (dx = [-1, +1])
            translate([dx * 12, 0, 6 - eps]) cylinder(h = 2 + 2 * eps, r = 0.6);
    }
}

//... and this is the outer wheel.
module frontArmInnerWheel() {
    rotate([0, 90, 0]) difference() {
        // main body
        cylinder(h = 4, r1 = 9, r2 = 5); // not r1=10 and r2=6 because of manufacturing precision issues
        
        // the hole for the arm
        translate([-puckHUp / 2 - 0.1, -puckW / 2 - 0.1, -eps])
            cube([puckHUp + 0.2, puckW + 0.2, 4 + 2 * eps]);
        // the hole for the arm, rotated 90
        translate([-puckW / 2 - 0.1, -puckHUp / 2 - 0.1, -eps])
            cube([puckW + 0.2, puckHUp + 0.2, 3 + eps]);
    }
}

// A similar mounting logic applies to the rear arm, with an exception that it is mounted onto two holes.
// This is the inner wheel, which is the same for left and right holes.
module rearArmInnerWheel() {
    rotate([0, 90, 0]) difference() {
        // main body
        cylinder(h = 3, r1 = 9.8, r2 = 6.8);
        
        // the hole for the arm
        translate([-puckHUp / 2 - 0.1, -puckW / 2 - 0.1, -eps])
            cube([puckHUp + 0.2, puckW + 0.2, 3 + 2 * eps]);
        // the hole for the stopper
        translate([-puckHUp / 2 - 0.1, -puckW / 2 - 2.1, -eps])
            cube([puckHUp + 0.2, puckW + 4.2, 2 + eps]);
        // the holes for the screws
        for (sx = [-1, +1])
            scale([sx, 1, 1])
            translate([puckHUp / 2 + 2, 0, -eps])
            cylinder(h = 3 + 2 * eps, r = 0.9);
    }
}

// This is the left-hand-side outer wheel, which has no servo linkage mountpoints.
module rearArmLeftOuterWheel() {
    rotate([0, 90, 0]) difference() {
        // main body
        union() {
            cylinder(h = 3, r1 = 9.8, r2 = 6.8);
            cylinder(h = 3.4, r = 6.8);
        }
        
        // the hole for the arm
        translate([-puckHUp / 2 - 0.1, -puckW / 2 - 0.1, -eps])
            cube([puckHUp + 0.2, puckW + 0.2, 3.4 + 2 * eps]);
        // the holes for the screws
        for (sx = [-1, +1])
            scale([sx, 1, 1])
            translate([puckHUp / 2 + 2, 0, -eps])
            cylinder(h = 3.4 + 2 * eps, r = 1.1);
    }
}

// This is the right-hand-side outer wheel, which does have servo linkage mountpoints.
module rearArmRightOuterWheel() {
    rotate([0, 90, 0]) difference() {
        // main body
        union() {
            translate([0, 0, -3]) cylinder(h = 3, r = 9.8);
            cylinder(h = 3, r1 = 9.8, r2 = 6.8);
            cylinder(h = 3.4, r = 6.8);
            // the pieces that will take the rods
            for (dx = [-1, +1])
                translate([dx * 12, 0, -3]) cylinder(h = 2, r = 2);
            translate([-12, -2, -3]) cube([24, 4, 2]);
        }
        
        // the hole for the arm
        translate([-puckHUp / 2 - 0.1, -puckW / 2 - 0.1, -eps])
            cube([puckHUp + 0.2, puckW + 0.2, 3.4 + 2 * eps]);
        // the holes for the screws
        for (sx = [-1, +1])
            scale([sx, 1, 1]) {
                translate([puckHUp / 2 + 2, 0, - eps]) cylinder(h = 3.4 + 2 * eps, r = 1.1);
                translate([puckHUp / 2 + 2, 0, -3 - eps]) cylinder(h = 3 + 2 * eps, r = 2);
            }
        // the holes for the rods
        for (dx = [-1, +1])
            translate([dx * 12, 0, -3 - eps]) cylinder(h = 2 + 2 * eps, r = 0.6);
    }
}

// This is the "box" which is the frame counterpart for (front) arm wheels.
module frontArmWheelbox() {
    rotate([0, 90, 0]) difference() {
        union() {
            linear_extrude(height = 2, scale = [1, 13/11]) square([22, 22], center = true);
            translate([0, 0, 2]) linear_extrude(height = 2, scale = [1, 11/13]) square([22, 26], center = true);
        }
        translate([0, 0, -eps]) cylinder(h = 4 + 2 * eps, r1 = 6.3, r2 = 10.3); // r1=6+eps, r2=10+eps, need a hefty eps to work
    }
}

// A shape to subtract from the frame to place servos.
// This is applied to all three servos.
module servoHoles() {
    scale([1, 1, -1])
    translate([0, 22, -4.5]) {
        translate([2, -0.5, 0]) cube([28, 21.5, 9 + eps]);
        translate([2 + 14, 20.5 / 2, 9]) linear_extrude(1, scale = [1, 0.75]) square([28, 21.5], center = true);

        translate([17, -5, 0]) cube([15, 30.5, 9 + eps]);
        translate([17 + 15 / 2, 20.5 / 2, 9]) linear_extrude(1, scale = [1, 0.85]) square([15, 30.5], center = true);

        translate([10, -2.25, 4.5]) rotate([0, 90, 0]) cylinder(h = 18, r = 0.9);
        translate([10, 25 - 2.25, 4.5]) rotate([0, 90, 0]) cylinder(h = 18, r = 0.9);
    }
}

// A standoff (we need four) to assist in mounting the flight controller, the ESC and the VTX.
module standOff() {
    linear_extrude(7, convexity = 2) difference() {
        circle(3);
        circle(1.7);
    }
}

// A small addition to host the VTX antenna. Should be a part of the frame,
// and currently is, but historically was printed separately.
module vtxAntennaMount() {
    translate([bodyHW - 4, -86, frameLow + frameHeight - 4])
        linear_extrude(4, convexity = 2)
        difference() {
            hull() {
                translate([-3, 0]) circle(3);
                polygon([[-3, -3], [0, -6], [0, +6], [-3, +3]]);
            }
            translate([-3, 0]) circle(1.8);
            translate([-6, -0.6]) square([3, 1.2]);
        }
}

// The frame. Can be printed as a single thing.
module frame() {
    vtxAntennaMount();

    // Mountpoints for the flight controller, the ESC and the VTX.
    for (dx = [-1, +1])
        for (dy = [-1, +1])
            difference() {
                union() {
                    translate([15.25 * dx, 15.25 * dy - 50, frameLow + frameHeight - 3]) 
                        cylinder(r = 3, h = 3);
                    translate([0, -50, frameLow + frameHeight - 3])
                        linear_extrude(3)
                        polygon([
                            [15.25 * dx, dy * 12.25],
                            [15.25 * dx, dy * 18.25],
                            [bodyHW * dx, dy * 18.25],
                            [bodyHW * dx, dy * 12.25],
                        ]);
                }
                translate([15.25 * dx, 15.25 * dy - 50, frameLow + frameHeight - 3 - eps]) 
                    cylinder(r = 1.4, h = 3 + 2 * eps);
            }
        
    difference() {
        union() {
            translate([0, 0, frameLow]) {
                linear_extrude(frameHeight, convexity = 5) {
                    difference() {
                        union() {
                            // spherical handler for the aft motor
                            difference() {
                                translate([0, bwdOffset]) 
                                    circle(ductOuterR + 5);
                            }
                            // main shape
                            polygon([
                                [-bodyHW, bwdOffset],
                                [-bodyHW, -11.1],
                                [-bodyHW + 2.1, -13.1],
                                [-bodyHW + 4.1, -11.1],
                                [-bodyHW + 4.1, +11.1],
                                [-bodyHW + 2.1, +13.1],
                                [-bodyHW, +11.1],
                                [-bodyHW, 55],
                                [+bodyHW, 55],
                                [+bodyHW, +11.1],
                                [+bodyHW - 2.1, +13.1],
                                [+bodyHW - 4.1, +11.1],
                                [+bodyHW - 4.1, -11.1],
                                [+bodyHW - 2.1, -13.1],
                                [+bodyHW, -11.1],
                                [+bodyHW, bwdOffset],
                                [+bodyHW - 4, bwdOffset],
                                [+bodyHW - 4, -17],
                                [+bodyHW - 8, -13],
                                [+bodyHW - 8, +13],
                                [+bodyHW - 12, +17],
                                [+bodyHW - 12, 51],
                                [-bodyHW + 12, 51],
                                [-bodyHW + 12, +17],
                                [-bodyHW + 8, +13],
                                [-bodyHW + 8, -13],
                                [-bodyHW + 4, -17],
                                [-bodyHW + 4, bwdOffset],
                            ]);
                            // a separate thing to hold the rear servo
                            for (sx = [-1, +1])
                                scale([sx, 1])
                                polygon([
                                    [60, -131],
                                    [60, -80],
                                    [bodyHW, -98.5 + (60 - bodyHW)],
                                    [bodyHW, -103 + (60 - bodyHW)],
                                    [56, -82],
                                    [56, -127]
                                ]);
                        }
                        translate([0, bwdOffset]) 
                            circle(ductOuterR + 1);
                        translate([-ductOuterR - 6, bwdOffset - ductOuterR - 6]) 
                            square([2 * (ductOuterR + 6), ductOuterR + 6]);
                    }
                }
            }

            // cylinders to accomodate aft motor mount
            for (sx = [-1, +1])
                scale([sx, 1, 1])
                translate([ductOuterR + 1, bwdOffset, 1])
                rotate([0, 90, 0])
                cylinder(r = frameHeight / 2, h = 4);

            // camera mount sides
            for (sx = [-1, +1])
                scale([sx, 1, 1])
                translate([9.6, 55, frameLow + frameHeight / 2])
                rotate([0, 90, 0])
                linear_extrude(1.2)
                polygon([
                    [+frameHeight / 2, 0],
                    [+frameHeight / 2 - 5, 7],
                    [-frameHeight / 2 + 5, 7],
                    [-frameHeight / 2, 0],
                ]);

            // overhead bar for forward arms
            translate([-bodyHW, -17, frameLow + frameHeight - 2]) 
                cube([2 * bodyHW, 34, 2]);
                
            // an addition to capture the middle wheel bases
            translate([-bodyHW + 4, -17, frameLow + frameHeight - 4]) 
                cube([2 * bodyHW - 8, 34, 4]);

            // diagonal rays for the rear assembly
            for (sx = [-1, +1])
                scale([sx, 1, 1])
                for (dz = [frameLow, frameLow + frameHeight - 2])
                translate([0, 0, dz])
                linear_extrude(2)
                polygon([
                    [bodyHW, -99], [60, -82], [56, -79], [bodyHW - 4, -96]
                ]);
        }

        // holes for wires from the rear assembly
        translate([-bodyHW - eps, -90, -5])
            cube([2 * (bodyHW + eps), 4, 10]);
        
        translate([-61, -123, -5])
            linear_extrude(10)
            polygon([
                [0, 0],
                [0, -5],
                [10, 11],
                [10, 16]
            ]);
        
        // holes to make the rear assembly more transparent
        for (sx = [-1, +1])
            scale([sx, 1, 1]) {
                translate([bodyHW - 20 - eps, -130, frameLow + 4]) cube([11, 40, frameHeight - 8]);
                translate([bodyHW + eps, -130, frameLow + 4]) cube([11, 70, frameHeight - 8]);
                translate([bodyHW + 20 - eps, -130, frameLow + 4]) cube([11, 70, frameHeight - 8]);
            }
        
        // holes for various side connectors on the FC and HDZ
        translate([-bodyHW - eps, -62.5, frameLow + 6])
            cube([2 * (bodyHW + eps), 25, frameHeight - 10]);
        
        // holes for motor arm continuations
        translate([-bodyHW - eps, 0, 0])
            rotate([0, 90, 0])
            cylinder(h = (bodyHW + eps) * 2, r = 6);
        
        // a shave-off for bottom wheel plate
        translate([-bodyHW - eps, -17, frameLow - eps])
            cube([2 * (bodyHW + eps), 34, 3]);
            
        // holes for securing the bottom wheel plate
        for (dy = [-1, +1])
            for (dx = [-1, +1])
            translate([dx * (bodyHW - 4), dy * 14, frameLow - eps])
            cylinder(h = 6, r = 0.8);
        
        // funny holes for the aft motor
        for (sx = [-1, +1])
            scale([sx, 1, 1])
            translate([ductOuterR, bwdOffset, 0])
            rotate([0, 90, 0]) {
                translate([0, 0, 1 - eps]) cylinder(r1 = 9, r2 = 7, h = 2 + eps);
                translate([0, 0, 3]) cylinder(r1 = 7, r2 = 9, h = 2 + eps);
            }
        
        // holes for the forward servos
        for (sx = [-1, +1])
            scale([sx, 1, 1])
            servoHoles();

        // holes for the rear servo
        translate([ductOuterR + 5 - bodyHW, -130, 0])
            servoHoles();
            
        // hole for the camera
        translate([-9.6, 50, frameLow + frameHeight / 2 - 10])
            cube([19.2, 20, 20]);
       
        // hole for the camera screws     
        translate([-11, 59, frameLow + frameHeight / 2])
            rotate([0, 90, 0])
            cylinder(h = 22, r = 0.8);
        
        // holes for inner forward arm wheels
        for (sx = [-1, +1])
            scale([sx, 1, 1])
            translate([0, 0, frameLow - eps])
            linear_extrude(frameHeight - 2 + 2 * eps)
            offset(r = 0.1) polygon([
                [5, -11], [7, -13], [9, -11],
                [9, +11], [7, +13], [5, +11],
            ]);
    }
}

// The plate which fixes the other ends of the wheelboxes and hardens the corresponding part of the frame.
module bottomArmPlate() {
    linear_extrude(3, convexity = 5) difference() {
        square([2 * bodyHW, 33.8], center = true);
        for (sx = [-1, +1]) scale([sx, 1]) {
            for (dx = [0, bodyHW - 9])
                translate([dx, 0])
                offset(r = 0.1) polygon([
                    [5, -11], [7, -13], [9, -11],
                    [9, +11], [7, +13], [5, +11],
                ]);
        }
        for (dy = [-1, +1])
            for (dx = [-1, +1])
            translate([dx * (bodyHW - 4), dy * 14])
            circle(r = 1.1);
    }
}

// The basic shape for the front leg (looking from the front).
module frontLegShape() {
    polygon([
        [+(bodyHW + 7), -50],
        [+(bodyHW + 7), -45],
        [+(bodyHW + 2), -5],
        [+(bodyHW + 2), 3],
        [-(bodyHW + 2), 3],
        [-(bodyHW + 2), -5],
        [-(bodyHW + 7), -45],
        [-(bodyHW + 7), -50],
    ]);
}

// The actual front leg.
module frontLeg() {
    for (sx = [+1, -1])
        scale([sx, 1, 1])
        translate([bodyHW + 5, 40, frameLow - 50]) {
            // pads for landing dampers
            linear_extrude(1) {
                square(15, center = true);
                translate([0, +7.5]) circle(7.5);
                translate([0, -7.5]) circle(7.5);
            }
            translate([0, 0, 1])
            linear_extrude(3, scale = [4 / 15, 2 / 3]) {
                square(15, center = true);
                translate([0, +7.5]) circle(7.5);
                translate([0, -7.5]) circle(7.5);
            }
        }

    difference() {
        translate([0, 50, frameLow])
            rotate([90, 0, 0])
            difference() {
                linear_extrude(20, convexity = 1)
                    offset(r = 3) offset(delta = -3) frontLegShape();
                translate([0, -2, 0.8])
                    linear_extrude(18.4, convexity = 1)
                    offset(r = 3) offset(delta = -6) frontLegShape();
                translate([0, 0, -eps])
                    linear_extrude(20 + 2 * eps, convexity = 1)
                    offset(r = 15) offset(delta = -21) frontLegShape();
            }
        translate([-bodyHW - 0.2, 0, frameLow - 0.1])
            cube([2 * (bodyHW + 0.2), 60, 10]);
    }
}

// The basic shape for the rear leg (looking from the front).
module rearLegShape() {
    polygon([
        [+(bodyHW + 27), -50],
        [+(bodyHW + 27), -45],
        [+(60 + 2), -5],
        [+(60 + 2), 3],
        [+(56 - 2), 3],
        [+(56 - 2), -8],
        [-(56 - 2), -8],
        [-(56 - 2), 3],
        [-(60 + 2), 3],
        [-(60 + 2), -5],
        [-(bodyHW + 27), -45],
        [-(bodyHW + 27), -50],
    ]);
}

// The actual rear leg.
module rearLeg() {
    for (sx = [+1, -1])
        scale([sx, 1, 1])
        translate([bodyHW + 25, -87, frameLow - 50]) {
            // pads for landing dampers
            linear_extrude(1) {
                square(15, center = true);
                translate([0, +7.5]) circle(7.5);
                translate([0, -7.5]) circle(7.5);
            }
            translate([0, 0, 1])
            linear_extrude(3, scale = [4 / 15, 14 / 30]) {
                square(15, center = true);
                translate([0, +7.5]) circle(7.5);
                translate([0, -7.5]) circle(7.5);
            }
        }
    difference() {
        translate([0, -80, frameLow])
            rotate([90, 0, 0])
            difference() {
                linear_extrude(14, convexity = 2)
                    offset(r = -3) offset(delta = 3)
                    offset(r = 3) offset(delta = -3) rearLegShape();
                translate([0, 0, 0.8])
                    linear_extrude(12.4, convexity = 2)
                    offset(r = 3) offset(delta = -6) rearLegShape();
                translate([0, 0, -eps])
                    linear_extrude(14 + 2 * eps, convexity = 2)
                    offset(r = 14) offset(delta = -20) rearLegShape();
            }
        translate([-60.2, -100, frameLow - 0.1])
            cube([120.4, 30, 10]);
    }
}

// This is used to indicate where the batteries are.
module battery() {
    // batteries are 21350
    translate([-18, 0, frameLow - 30])
        rotate([0, 90, 0])
        cylinder(r = 21 / 2, h = 36);
}

// The frame for the battery assembly.
module batteryFrame() {
    difference() {
        union() {
            // main shape
            linear_extrude(36 + 1.6) intersection() {
                union() {
                    square([12, 22 * 5]);
                    translate([12, 0]) circle(12);
                    translate([12, 22 * 5]) circle(12);
                }
                translate([0, -12]) square([12, 22 * 5 + 24]);
            }
            // ribs along the length
            for (dz = [0, 9, 18.4, 27.8, 36.8])
                translate([-5, -5.2, dz])
                cube([15, 22 * 5 + 17, 0.8]);
            // base plates for adhesion
            translate([-5, -5.2, 0])
                cube([0.8, 18, 37.6]);
            translate([-5, 22 * 5 + 17 - 5.2 - 9, 0])
                cube([0.8, 9, 37.6]);
        }
        // cylindrical holes for cells
        for (dy = [0:5])
            translate([12.2, dy * 22, 0.8]) cylinder(h = 36, r = 11.2);
        // holes for cell soldered joints
        translate([1, 0, 0.8]) cube([12, 22 * 5, 5]);
        translate([1, 0, 31 + 0.8]) cube([12, 22 * 5, 5]);
        // mounting hole for the forward leg
        translate([-5 - eps, 117.9, -eps])
            cube([3.2, 1.0, 36 + 1.6 + 2 * eps]);
        translate([-5 - eps, 117.9, -eps])
            cube([7, 1.0, 2]);
        translate([-5 - eps, 117.9, 35.6 + eps])
            cube([7, 1.0, 2]);
        // mounting hole for the aft leg
        translate([-5 - eps, 7.1, -eps])
            cube([3.2, 1.0, 36 + 1.6 + 2 * eps]);
    }
}

// All ducts are rendered transparent.
translate([0, 0, -ductH + puckHUp / 2]) {
    #translate([-fwdCenter, 0, 0]) duct();
    #translate([+fwdCenter, 0, 0]) duct();
}

#translate([0, bwdOffset, 0])
    rotate([0, 0, 0])
    translate([0, 0, -ductH + puckHUp / 2])
    duct();

// Arms, wheels and wheelboxes.
translate([0, 0, puckHUp / 2]) {
    translate([0, bwdOffset, 0]) rearArm();

    for (sx = [-1, +1]) scale([sx, 1, 1]) {
        frontArm();
        translate([fwdCenter - ductOuterR - 8, 0, -puckHUp / 2]) {
            color("black") frontArmOuterWheel();
            frontArmWheelbox();
        }
        translate([5, 0, -puckHUp / 2]) {
            color("black") frontArmInnerWheel();
            translate([4, 0, 0]) scale([-1, 1, 1]) frontArmWheelbox();
        }
        
        translate([ductOuterR + 0.2, bwdOffset, -puckHUp / 2])    
            color("black") rearArmInnerWheel();
    }
    translate([-ductOuterR - 6, bwdOffset, -puckHUp / 2])
        color("black") rearArmLeftOuterWheel();
    translate([ductOuterR + 6, bwdOffset, -puckHUp / 2])
        scale([-1, 1, 1])
        color("black") rearArmRightOuterWheel();
}

// The frame and the arm plate.
frame();
translate([0, 0, frameLow]) bottomArmPlate();

// Location of legs and the battery assembly.
// not too forward because otherwise the ELRS antenna
//     cannot be placed in the front vertically
// not too aft because otherwise the aft motor assembly cannot move freely
translate([0, -3, 0]) {
    frontLeg();
    rearLeg();

    translate([18 + 0.8, -88, frameLow - 42])
    rotate([0, -90, 0])
    batteryFrame();

    color("green", 0.5) 
        for (dy = [-3:2])
        translate([0, dy * 22 - 22, 0])
        battery();
}
