//////////////////////////////////
///    SN1, frame version 2    ///
///   Author: Maxim Buzdalov   ///
///    For "Wow, That Flies"   ///
/// License: WTF Public License //
//////////////////////////////////

// This is a coax copter with an X arrangement of flaps.
// Apart from the details presented in this file,
// an important structural part is a piece of a coke bottle
// which forms what is called the "outer frame".
// The cylindrical propeller containments are designed to be
// tightly inserted in this bottle piece,
// the legs are attached (and glued) to the outside,
// and many non-structural parts are simply glued to this "outer frame".

// Depending on your needs, you may need to adjust some things.
// Apart from the general lengths and other sides of the vehicle,
// one of the frequent adjustment themes are the gaps designed for
// one detail to be inserted in another. 
// Many such points, with comments, are marked with the #adjust tag.

//////////////////////////////////
/// Rendering settings
//////////////////////////////////

// Minimum angle
$fa = 1;
// Minimum fragment size
$fs = 0.1;

//////////////////////////////////
/// General geometry
//////////////////////////////////

// Negligible but positive thickness
// Used mainly to offset what is subtracted
eps = 0.01;
eps2 = 2 * eps; // 2 * eps is very frequently used, this saves some space

// A pair of arrays to create cross-aligned series of structures.
DX = [1, 0, -1, 0];
DY = [0, 1, 0, -1];

//////////////////////////////////
/// Common bolt sizes
//////////////////////////////////
m2Free = 1.05;
m3Tight = 1.3;
m3Free = 1.6;

//////////////////////////////////
/// Global design variables
///
/// Many of this can be changed,
/// and the details will change
/// appropriately. 
//////////////////////////////////

// The distance between the top and the bottom cylinder faces
VH = 200; 

// The distance between the bottom of the leg and the bottom face
lowPoint = 50; 

// Thickness of each leg
legThick = 3;

// Width of (the longer part of) each leg
legW = 5;

// Thickness of the piece of plastic that covers the leg cavity
legCoverThick = 0.8;

// The height below the lowPoint of the landing damper
landingDamperHeight = 10;

// The wall thickness of the landing damper
landingDamperThick = 1.2;

// The Z coordinate of flap rotation axes, where servo holes are
servoHoleH = -lowPoint + legThick + 20;

// By how much to enlarge the leg base
// to protect servos and improve landing
servoProtectionH = 21;

// By how much should wide parts of the legs extend beyond the bottom face
strengthOffset = 20;

// Thickness of each thrust puck
puckThick = 5;

// The radius of the duct part where the propeller should freely fit
propellerR = 45;

// By how much to extend thrust puck outside of the duct
// to account for nonzero shell thickness etc.
puckOutdent = 0.1;

// The total height of each duct
ductTotalHeight = 40;

// The offset of the lowest end of the thinnest part
// (that encloses the propeller)
ductThinnestBegin = 12;

// The offset of the highest end of the thinnest part
ductThinnestEnd = 19;

// The minimum thickness of each duct (both at the top and at the bottom)
ductMinimumThick = 1.2;

// The size of the wire cutout
ductWireCutoutSize = 4;

// Actual cutout angles used
topWireCutoutAngle = -22;
botWireCutoutAngle = +25;

// The defining radii of the overall shape
// This heavily depends on the shape of the outer frame,
// which in my case was made from a coke bottle.
// Definitely #adjust to your conditions.
RTop = 48;
RBot = 49;
R = (RTop + RBot) / 2;

// Central servo wire height, also a reference to many other wire covers
centralServoWireH = VH / 2 - 35.5;

// Thickness of the flaps.
flapThick = 1.5;

// The location for flap mounting points at the servo end
servoMountR = RBot - 10.5;

// The location for flap mounting points opposite to the servo
antiServoMountR = RBot - 3.8;

//////////////////////////////////
/// Thrust puck shape definitions
//////////////////////////////////

// The shape of the motor mounting cross.
// Used for both the actual cross
// and for subtracting it from the duct.
//
// @param r the radius of the outer circle
// @param off the offset to control gaps
module puckCrossShape(r, off) {
    for (a = [0, 90])
        linear_extrude(puckThick + off, convexity = 2)
        rotate([0, 0, a]) {
            // 5, 6 and 0.8 are hardcoded here
            // as they are not used elsewhere;
            // 0.8 should be less than ductMinimumThick though
            square([2 * (r + puckOutdent + off), 5 + 2 * off],
                   center = true);
            square([2 * (r - 0.8 + off), 6 + 2 * off],
                   center = true);
        }
}

// The motor mounting structure, or "thrust puck".
//
// @param r the radius of the outer circle
// @param holeRot the angle at which to introduce cutout
//        for motor wires
module puck(r, holeRot) {
    difference() {
        union() {
            // cross
            puckCrossShape(r, 0);
            // cylinder where the motor resides
            cylinder(h = puckThick, r = 11);
        }
        // the central hole for motor's axis
        translate([0, 0, -eps])
            cylinder(h = puckThick + eps2, r = 2.6);
        // holes for bolts
        for (a = [0:3])
            // they are rotated such that
            // the motor wires go to the cutout in the duct
            // using the shortest way.
            rotate([0, 0, holeRot + 45 + 90 * a])
            translate([6, 0, -eps]) {
                // the main M2 bolt hole
                cylinder(h = puckThick + eps2, r = m2Free);
                // the bolt head hole
                // both 3s are the bolt head height,
                // 2 is bold head radius;
                // please #adjust if needed
                translate([0, 0, 3])
                    cylinder(h = puckThick - 3 + eps2, r = 2);
            }
        // side M3 holes, tight to thread in
        for (a = [0:3])
            rotate([0, 0, 90 * a])
            translate([r + puckOutdent + eps, 0, puckThick / 2])
            rotate([0, -90, 0])
            // 9 is hardcoded to exceed bolt height, but not much;
            // please #adjust if needed
            cylinder(h = 9, r = m3Tight);
    }
}

// The duct, which reshapes the shell to enclose the propeller nicely
//
// @param extOuterR the radius of the outer part of the duct,
//                  which is closer to an end of the craft
// @param intOuterR the radius of the inner part of the duct,
//                  which is closer to the center of the craft
// @param holeRot the angle at which to introduce cutout
//        for motor wires
module duct(extOuterR, intOuterR, holeRot) {
    difference() {
        // the exterior cylinder
        cylinder(h = ductTotalHeight, r1 = intOuterR, r2 = extOuterR);
        // the lower/inner part of the duct internals
        translate([0, 0, -eps])
            cylinder(h = ductThinnestBegin + eps2,
                     r1 = intOuterR - ductMinimumThick,
                     r2 = propellerR);
        // the middle part of the duct internals
        translate([0, 0, ductThinnestBegin - eps])
            cylinder(h = ductThinnestEnd - ductThinnestBegin + eps2,
                     r = propellerR);
        // the upper/outer part of the duct internals
        translate([0, 0, ductThinnestEnd - eps])
            cylinder(h = ductTotalHeight - ductThinnestEnd + eps2,
                     r1 = propellerR,
                     r2 = extOuterR - ductMinimumThick);
        // the motor wire cutout
        rotate([0, 0, holeRot])
            translate([extOuterR - ductWireCutoutSize / 2,
                      -ductWireCutoutSize / 2,
                      ductTotalHeight - ductWireCutoutSize])
            cube([ductWireCutoutSize,
                  ductWireCutoutSize,
                  ductWireCutoutSize + eps]);
        // the holes to fit the thrust puck
        // the gap 0.01 here is very small,
        // but it still worked;
        // please #adjust if needed
        translate([0, 0, ductTotalHeight - puckThick])
            puckCrossShape(extOuterR, 0.01);
    }
}

//////////////////////////////////
/// Leg-related definitions
//////////////////////////////////

// The long flat base of each leg (actually 2D)
module legBase() {
    difference() {
        union() {
            // main trunk
            translate([-legW / 2, -lowPoint]) 
                square([legW, lowPoint + VH]);
            // bottom part should be thicker
            translate([-legW / 2 - legThick, -lowPoint]) 
                square([legW + 2 * legThick, lowPoint + strengthOffset]);
        }
        // holes to attach to thrust pucks
        for (dy = [puckThick / 2, VH - puckThick / 2])
            translate([0, dy])
            circle(r = m3Free);
    }
}

// The additional structures at the bottom of every leg
module legLowerStrengthener() {
    wallThick = 1.2;
    // trapezoidal sides
    for (dx = [-legW / 2 - legThick, legW / 2 + legThick - wallThick])
        translate([dx, -lowPoint, legThick - eps])
        linear_extrude(servoProtectionH, scale = [1, 0.5])
        square([wallThick, lowPoint + strengthOffset]);
    // the very bottom part, to which the landing dampers attach
    translate([-legW / 2 - legThick, -lowPoint, legThick - eps])
        cube([legW + 2 * legThick, wallThick, servoProtectionH]);
}

// The system of holes where servos are inserted (2D).
//
// This is aligned such that the rotational axis is at servoHoleH.
// If servos other than EMAX ES9052MD are used,
// please #adjust.
module servoMountMinus() {
    translate([-4.2, -lowPoint + legThick + 4.5]) 
        square([8.4, 20]);
    translate([0, -lowPoint + legThick + 2])
        circle(0.9);
    translate([0, -lowPoint + legThick + 27])
        circle(0.9);
}

// The hole(s) that should be in the legs opposite the servos (2D).
module antiServoMountMinus() {
    translate([0, servoHoleH])
        circle(1);
}

// The forward leg, which is without a servo. There are two.
module forwardLeg() {
    linear_extrude(legThick, convexity = 2) {
        difference() {
            legBase();
            antiServoMountMinus();
        }
    }
    legLowerStrengthener();
}

// The backward leg, to which a servo is mounted. There are two.
module backwardLeg() {
    linear_extrude(legThick, convexity = 3) {
        difference() {
            legBase();
            servoMountMinus();
        }
    }
    legLowerStrengthener();
}

// The piece of plastic that covers the cavity of (the landing part of) the leg.
module legCover() {
    theSize = lowPoint + strengthOffset;
    angle = atan(servoProtectionH / theSize);

    linear_extrude(legW + 2 * legThick) polygon([
        [0, servoProtectionH],
        [theSize / 2, servoProtectionH],
        [theSize, 0],
        [theSize, legCoverThick / cos(angle)],
        [theSize / 2 + sin(angle) * legCoverThick, servoProtectionH + legCoverThick],
        [0, servoProtectionH + legCoverThick]
    ]);
}

// The damper to dampen the impact of landing and to increase the height
// so that the flaps don't touch the ground. One for each leg. Print in TPU.
module landingDamper() {
    // 0.1 is the mounting gap size
    innerLength = legThick + servoProtectionH + legCoverThick + 0.1;
    innerWidth = legW + 2 * legThick + 0.1;
    outerLength = innerLength + 2 * landingDamperThick;
    outerWidth = innerWidth + 2 * landingDamperThick;
    outerOutdent = 6;
    innerOutdent = outerOutdent / 2;
    onLegHeight = 3.8;
    
    difference() {
        union() {
            linear_extrude(landingDamperHeight, 
                scale=[1, outerLength / (outerLength + outerOutdent)])
                translate([-outerWidth / 2, 0])
                square([outerWidth, outerLength + outerOutdent]);
            translate([-outerWidth / 2, 0, landingDamperHeight])
                cube([outerWidth, outerLength, onLegHeight]);
        }
        translate([-innerWidth / 2, landingDamperThick, landingDamperHeight])
            cube([innerWidth, innerLength, onLegHeight + eps]);
        translate([0, landingDamperThick, -eps])
            linear_extrude(landingDamperHeight - landingDamperThick,
                scale=[1, innerLength / (innerLength + innerOutdent)])
            translate([-innerWidth / 2, 0])
            square([innerWidth, innerLength + innerOutdent]);
    }
}

//////////////////////////////////
/// Flaps and their adaptors
//////////////////////////////////

module negFlap() {
    translate([0, 0, -flapThick / 2])
    linear_extrude(flapThick)
    polygon([
        [-antiServoMountR, -26],
        [-antiServoMountR, -1.2],
        [-antiServoMountR + 4.2, -1.2],
        [-antiServoMountR + 4.2, +1.2],
        [-antiServoMountR, +1.2],
        [-antiServoMountR, +23],
        [servoMountR, +23],
        [servoMountR, -26],
        [+19, -26],
        [+19, -17],
        [0, 2],
        [-19, -17],
        [-19, -26]
    ]);
}

module posFlap() {
    translate([0, 0, -flapThick / 2])
    linear_extrude(flapThick)
    polygon([
        [-antiServoMountR, -26],
        [-antiServoMountR, -1.2],
        [-antiServoMountR + 4.2, -1.2],
        [-antiServoMountR + 4.2, +1.2],
        [-antiServoMountR, +1.2],
        [-antiServoMountR, +23],
        [-28, +23],
        [0, -5],
        [+28, +23],
        [servoMountR, +23],
        [servoMountR, -26],
        [+31, -26],
        [+23, -26],
        [+22, -25],
        [-22, -25],
        [-23, -26],
        [-31, -26],
    ]);
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

//////////////////////////////////
/// Electronic mounts
//////////////////////////////////

// The structure glued to the shell,
// to which the AIO flight controller is mounted using hidden nuts.
// This is designed for a diamond-shaped FC with 25.5 mm mounting pattern,
// otherwise #adjust.
module aioMount() {
    // the vertical/horizontal distance to the center of a mountpoint
    dist = 25.5 / sqrt(2);
    // the half-width of each arm
    w = 2.5;
    th = 10;
    
    difference() {
        linear_extrude(th, convexity = 3) {
            // the cross
            square([2 * dist, 2 * w], center = true);
            square([2 * w, 2 * dist], center = true);
            // the enclosures for nuts
            for (i = [0:3])
                translate([DX[i] * dist, DY[i] * dist, 0])
                circle(r = w + 1);
        }

        for (i = [0:3])
            translate([DX[i] * dist, DY[i] * dist, -eps]) {
                // the hole for FC's mounting bolts
                cylinder(h = th + eps2, r = m2Free);
                // the hole for hidden glued-in nuts
                translate([0, 0, 0.4 + eps])
                    cylinder(h = th + eps2, r = 2.4, $fa = 60);
            }

        // the placeholder for the shell to form the part
        // that is glued to the shell
        translate([-dist - w - 1 - eps, 0, R + 2.1])
            rotate([0, 90, 0])
            cylinder(h = 2 * (dist + w + 1 + eps), r = R);
    }
}

// The structure glued to the shell
// to which the HDZero Whoop Lite video transmitter is attached.
// The mountpoints are arranged in a X-shaped 25.5mm pattern,
// where nylon standouts are glued in.
// If your video transmitter is not like this, please #adjust
module hdzMount() {
    dist = 25.5 / sqrt(2);
    w = 2.5;
    w0 = 4.2;
    th = 10;
    
    // The whole thing is designed in a + pattern,
    // so that the design is similar to aioMount,
    // then rotated in the final assembly.
    
    difference() {
        linear_extrude(th, convexity = 3) {
            // the cross
            square([2 * dist, 2 * w], center = true);
            square([2 * w, 2 * dist], center = true);
            // the mount pads for standouts
            for (i = [0:3])
                translate([DX[i] * dist, DY[i] * dist])
                circle(w0);
        }

        // the hexagonal-shaped holes for nylon standouts
        for (i = [0:3])
            translate([DX[i] * dist, DY[i] * dist, -eps])
            cylinder(h = 2 + eps, r = 3.3, $fa = 60);

        // the placeholder for the shell to form the part
        // that is glued to the shell
        rotate([0, 0, 45])
            translate([-dist - w0 - eps, 0, R + 1.5])
            rotate([0, 90, 0])
            cylinder(h = 2 * (dist + w0 + eps), r = R);
    }
}

// The structure glued to the shell
// to which the HDZero Nano v3 camera is attached.
// If your camera is not like this, please #adjust.
module camMount() {
    difference() {
        // the main shape
        translate([-8.4, -9.5, 0])
            cube([16.8, 19.0, 19]);
        // the most internals
        translate([-7.2, -8.3, - eps])
            cube([14.4, 16.6, 17]);
        // the back cutout to glue on the shell
        translate([0, 10, R + 18.2])
            rotate([90, 0, 0])
            cylinder(h = 20, r = R);
        // the holes for mounting bolts
        translate([10, 0, 10])
            rotate([0, -90, 0])
            cylinder(h = 20, r = 0.7);
        // the cutout for the MIPI cable,
        // large enough for the connector to go through.
        translate([-7.2, -10, 14 - eps])
            cube([14.4, 10, 3]);
    }
}

// The structure glued to the shell
// to which the Beitian BN-880 GPS+Compass module is glued.
// If your module is not like this, please #adjust.
module gpsMount() {
    difference() {
        // the big cube to cut parts from
        translate([-10.25, -14, 0])
            cube([20.5, 28, 25]);
        // the cubic space to insert the bottom of the GPS module
        translate([-9.25, -12.75, -eps])
            cube([18.5, 25.5, 3]);
        // the back cutout to glue on the shell
        rotate([0, 45, 0])
            translate([-30, 0, R + 9])
            rotate([0, 90, 0])
            cylinder(h = 40, r = R);
        // the side holes to allow the USB wire to reach
        // the flight controller's USB connector;
        // only one is needed, another is for aesthetics
        translate([0, -20, 5])
            rotate([0, -58, -35])
            cylinder(h = 40, r = 6);
        translate([0, +20, 5])
            rotate([0, -58, +35])
            cylinder(h = 40, r = 6);
    }
}

//////////////////////////////////
/// Various wire-covers
/// All of these is very ad-hoc,
/// so definitely #adjust!
//////////////////////////////////


// A shape for the half-cylinders for covering motor wires on the outer surface.
//
// @param len the length of the cylinder
// @param off the offset to be used in subtraction.
module motorWireCoverShape(len, off) {
    intersection() {
        translate([0, -4 - off, off / 100]) cube([len - off, 8 + 2 * off, 4 + off]);
        union() {
            translate([4, 0, off / 100])
                rotate([0, 90, 0])
                cylinder(h = len + off, r = 4 + off);
            translate([4, 0, off / 100])
                sphere(r = 4 + off);
        }
    }
}

// The hollow half-cylinders that cover motor wires on the outer surface.
//
// @param len the length of the cover.
module motorWireCover(len) {
    difference() {
        motorWireCoverShape(len, 0);
        motorWireCoverShape(len, -0.8);
    }
}

// A very complicated wire cover that keeps wires from the servos covered.
// It has cutouts for many other wires too.
module centralServoWireCover() {
    angle = 45 - asin((legW / 2 + 1) / R);
    angleCut = asin(15 / R);
    
    intersection() {
        linear_extrude(5) polygon([
            [0, 0],
            [(R + 30) * cos(angle), (R + 30) * +sin(angle)],
            [(R + 30) * cos(angle), (R + 30) * -sin(angle)],
        ]);
        difference() {
            translate([0, 0, 0])
                cylinder(h = 5, r = R + 3);
            translate([0, 0, -eps])
                cylinder(h = 5 + eps2, r = R);
            difference() {
                translate([0, 0, 1.2])
                    cylinder(h = 5, r = R + 2.2);
                translate([0, 0, 1.2 - eps])
                    cylinder(h = 5 + eps2, r = R + 0.8);
            }
            for (a = [45 - botWireCutoutAngle, -angleCut])
                rotate([0, 0, a])
                translate([R + 0.9, -2, -eps])
                cube([5, 4, 5 + eps2]);
            for (a = [-angle, +angle])
                rotate([0, 0, a])
                translate([R - 0.9, 0, -eps])
                linear_extrude(3, scale=[1, 0])
                translate([0, -2])
                square([3, 4]);
        }
    }
}

// A small piece of plastic that keeps the ELRS antenna
// fixed to the outer frame.
module elrsAntennaLock() {
    difference() {
        translate([-0.5, 0, 0]) cube([6.5, 6, 8]);
        translate([-R, 3, -eps]) cylinder(r = R, h = 8 + eps2);
        translate([3, 3, -eps]) cylinder(r = 2, h = 8 + eps2);
        translate([1, 3, -eps]) cube([4, 5, 8 + eps2]);
    }
}

// A cover for the part of the servo wires that go vertically along their legs.
module midServoWireCover() {
    length = centralServoWireH - strengthOffset - 1;
    cube([length, 1, legThick + 1]);
    cube([length, 5, 1]);
}

// A 2D shape for the following module to produce the outline of ELRS wire cover.
// @param off the offset to be used in subtraction.
module elrsWireCoverOutline(off) {
    polygon([
        [19.6 - off, -off],
        [-off, -off],
        [-off, 19.4 - off],
        [5 + off, 19.4 - off],
        [5 + off, 3 + off],
        [19.6 - off, 3 + off]
    ]);
}

// An angled wire cover that protects and guides ELRS wires
// just before connection to the ELRS module itself.
// The module is glued to the outer frame.
module elrsWireCover() {
    difference() {
        linear_extrude(6) elrsWireCoverOutline(0.8);
        translate([0, 0, 1.2]) linear_extrude(5) elrsWireCoverOutline(0);
        translate([-5, 9, R + 5])
            rotate([0, 90, 0])
            cylinder(h = 25, r = R);
        translate([9, -1.2, 1.2])
            cube([5.5, 8, 5]);
    }
}

// A mount that is glued to the outer surface and keeps a battery
// from one of the ends with the help of a piece of catgut/rubber.
// Ideally, some sticky pad should be glued in, so that the battery does not slip.
// Two per battery, for each of the two batteries.
module batteryMountA() {
    difference() {
        union() {
            translate([-10, 0, 0]) cube([20, 8, 5]);
            linear_extrude(2, convexity = 2) polygon([
                [-12, 7], [-12, 5], [-11, 5], [-11, 6],
                [+11, 6], [+11, 5], [+12, 5], [+12, 7]
            ]);
        }
        translate([0, -R + 1, -eps]) cylinder(h = 5 + eps2, r = R);
        translate([-8, 2.5, -eps]) cube([16, 8, 5 + eps2]);
    }
}

// A mount that keeps a battery from slipping and falling down to the ground.
// This will probably be replaced by something better.
module batteryMountB() {
    difference() {
        linear_extrude(10, scale = [0.1, 1])
            translate([-3, 0]) 
            square([6, 10]);
        translate([0, -R + 1, -eps]) cylinder(h = 10 + eps2, r = R);
    }
}

// A piece that contains an XT30 female connector and keeps it fixed
// when glued to the outer frame. There are two of them.
module powerConnectorHolder() {
    translate([0, 2.5, -5])
    difference() {
        linear_extrude(10, convexity = 2) polygon([
            [0, +3.8],
            [20, +3.8],
            [20, +2.5],
            [19, +2.4],
            [19, +2.6],
            [9, +2.6],
            [9, -2.6],
            [19, -2.6],
            [19, -2.4],
            [20, -2.5],
            [20, -3.8],
            [0, -3.8],
        ]);
        translate([-R + 1, 0, 5])
            rotate([90, 0, 0])
            translate([0, 0, -2.6])
            cylinder(h = 5.2, r = R + 3.1);
        translate([-R + 1, 0, 5])
            rotate([90, 0, 0])
            translate([0, 0, -6])
            cylinder(h = 12, r = R);
    }
}

//////////////////////////////////
/// Auxiliary stuff
/// that has once been useful.
/// Surely #adjust to your needs
//////////////////////////////////

// Modules of a construction
// that can be put on top of SN1 and stay there,
// so that some load can be put on top of it

// The vertical part, there are four of them
module weightAirGapLeg() {
    linear_extrude(5) polygon([
        [-3, 125],
        [-3, 120],
        [-5, 120],
        [-3, 115],
        [-3, 15],
        [-4, 15],
        [-8.1, 10],
        [-8.1, 0],
        [-3.1, 0],
        [-3.1, 5],
        [+3.1, 5],
        [+3.1, 0],
        [+8.1, 0],
        [+8.1, 10],
        [+4, 15],
        [+3, 15],
        [+3, 115],
        [+5, 120],
        [+3, 120],
        [+3, 125]
    ]);
}

// The top cross that keeps the legs at their places.
module weightAirGapCross() {
    linear_extrude(5.1, convexity = 4) difference() {
        union() {
            square([2 * RTop, 6], center = true);
            square([6, 2 * RTop], center = true);
            for (a = [0:3])
                rotate([0, 0, a * 90])
                translate([-6, 37])
                square([12, 11]); 
        }
        for (a = [0:3])
            rotate([0, 0, a * 90])
            translate([-3.05, 39.95])
            square([6.1, 5.1]); 
    }
}

// The bottom cross that only partially constrains the legs.
module weightAirGapLowerCross() {
    intersection() {
        weightAirGapCross();
        cylinder(h = 5.1, r = 45);
    }
}


//////////////////////////////////
//////////////////////////////////
//////////////////////////////////
///  Now actually drawing
///
///  The first if-branch is 
///  about running single modules
///  to be rendered to STL
///  and sent to a 3D-printer.
///
///  The second if-branch is
///  the physical layout of all
///  the modules.
//////////////////////////////////
//////////////////////////////////
//////////////////////////////////

// If animation is enabled, these produce a rotating view.
$vpt = [0, 0, VH / 2 - lowPoint / 2];
$vpr = [90 + sin($t * 360) * 10, 0, $t * 360];
$vpd = 800;

// Flaps also move nicely during animation.
posFlapAngle = 45 * sin($t * 720);
negFlapAngle = 45 * sin($t * 1080);

if (false) {
    // Everything is to be printed with PETG
    // except landingDamper() which is to be printed with TPU.

    // top module
    //duct(RTop, RTop - 0.25, topWireCutoutAngle);
    //translate([100, 0, 0]) puck(RTop, topWireCutoutAngle);

    // bottom module
    //duct(RBot, RBot - 0.25, botWireCutoutAngle);
    //translate([100, 0, 0]) puck(RBot, botWireCutoutAngle);

    //forwardLeg();
    //translate([12, 0, 0]) backwardLeg();

    //aioMount();
    //hdzMount();
    //gpsMount();
    //camMount();

    //motorWireCover(centralServoWireH - 5);
    //motorWireCover(VH / 2 - 20);
    //centralServoWireCover();
    //elrsAntennaLock();
    //midServoWireCover();
    //elrsWireCover();
    //legCover();
    //batteryMountA();
    //batteryMountB();
    
    //weightAirGapLeg();
    //weightAirGapCross();
    //weightAirGapLowerCross();
    //landingDamper();
} else {
    /////////////////////////////////
    /// The main assembly drawing
    /////////////////////////////////
    
    // top module
    translate([0, 0, VH - ductTotalHeight])
        duct(RTop, RTop - 0.25, topWireCutoutAngle);
    color("green")
        translate([0, 0, VH - puckThick])
        puck(RTop, topWireCutoutAngle);
    
    // bottom module
    translate([0, 0, ductTotalHeight])
        rotate([180, 0, 0])
        duct(RBot, RBot - 0.25, botWireCutoutAngle);
    color("green")
        translate([0, 0, puckThick])
        rotate([180, 0, 0])
        puck(RBot, botWireCutoutAngle);
    
    // legs
    color("#80FF00") {
        for (a = [0, 90])
            rotate([0, 0, a])
            translate([0, -R, 0])
            rotate([90, 0, 0])
            backwardLeg();
        for (a = [180, 270])
            rotate([0, 0, a])
            translate([0, -R, 0])
            rotate([90, 0, 0])
            forwardLeg();
    }
    
    // flaps
    color("yellow") {
        translate([0, 0, servoHoleH])
            rotate([90 + negFlapAngle, 0, 0])
            negFlap();

        translate([0, 0, servoHoleH])
            rotate([0, 0, -90])
            rotate([90 + posFlapAngle, 0, 0])
            posFlap();
    }

    // flap mount adapters
    color("blue") {
        for (a = [0, 1])
            rotate([0, 0, 90 * a])
            translate([0, -servoMountR + 2, servoHoleH])
            rotate([0, a ? -negFlapAngle : -posFlapAngle, 0])
            rotate([90, 0, 0])
            servoAdapter();

        for (a = [0, 1])
            rotate([0, 0, 180 + 90 * a])
            translate([0, -antiServoMountR + 2, servoHoleH])
            rotate([0, a ? negFlapAngle : posFlapAngle, 0])
            rotate([90, 0, 0])
            antiServoAdapter();
    }

    // leg dampers
    for (a = [0:3])
        rotate([0, 0, 90 * a])
        translate([R - landingDamperThick, 0, -lowPoint - landingDamperHeight])
        rotate([0, 0, -90])
        landingDamper();
    
    // AIO mount
    rotate([0, 0, -45])
        translate([R + 2.1, 0, VH / 2])
        rotate([0, -90, 0])
        aioMount();
    
    // HDZero mount
    rotate([0, 0, 135])
        translate([R + 1.5, 0, VH / 2])
        rotate([45, 0, 0])
        rotate([0, -90, 0])
        hdzMount();
    
    // GPS mount
    rotate([0, 0, 135])
        translate([-R - 9, 0, VH / 2 + 45])
        rotate([180, 0, 0])
        rotate([0, 45, 0])
        gpsMount();
    
    // camera mount
    rotate([0, 0, 45])
        translate([0, R + 18.2, VH / 2 - 45])
        rotate([90, 0, 0])
        rotate([0, 0, 180])
        camMount();
    
    // battery holders
    color("blue")
        for (a = [110, -20])
        rotate([0, 0, a]) {
            for (dz = [VH - 15, VH - 85])
                translate([0, R - 1, dz])
                batteryMountA();
            translate([0, R - 1, VH - 95])
                rotate([-90, 0, 0])
                batteryMountB();
        }
    
    // wire covers
    color("red") {
        // top motor wire cover
        rotate([0, 0, topWireCutoutAngle])
            translate([R - 0.7, 0, VH + 0.5])
            rotate([0, 90, 0])
            render(convexity = 2, $fs = 1) {
                motorWireCover(VH / 2 - 20);
            }
        // bottom
        rotate([0, 0, -botWireCutoutAngle])
            translate([R + 0.5, 0, -0.5])
            rotate([180, 0, 0])
            rotate([0, 90, 0])
            render(convexity = 2, $fs = 1) {
                motorWireCover(centralServoWireH - 5);
            }
        // central servo wire cover
        translate([0, 0, centralServoWireH])
            rotate([0, 0, -45])
            render(convexity = 2, $fs = 1) {
                centralServoWireCover();
            }
            
        // side servo wire covers
        for (a = [+1, -1])
            rotate([0, 0, -45 * (1 - a)])
            translate([R + legThick + 1, -a * 5.5, strengthOffset])
            rotate([0, -90, 0])
            scale([1, a, 1])
            midServoWireCover();
            
        // ELRS antenna lock
        rotate([0, 0, 20])
            translate([R + 7, 0, centralServoWireH - 11.5])
            rotate([0, 0, 180])
            elrsAntennaLock();
            
        // ELRS wire cover
        rotate([0, 0, -53])
            translate([R + 5, -10, centralServoWireH - 9.25])
            rotate([0, -90, 0])
            elrsWireCover();
        
        // Leg cavity covers
        for (a = [0:3])
            rotate([0, 0, a * 90])
            translate([(legW + 2 * legThick) / 2, R + legThick, -lowPoint])
            rotate([0, -90, 0])
            legCover();
        
        // Holders for power connectors
        for (a = [-15, -75])
            rotate([0, 0, a])
            translate([R, 0, centralServoWireH]) 
            rotate([90, 0, 0])
            powerConnectorHolder();
    }
    
    // weight air gap, disabled by default
    if (true) color("red", 0.2) {
        for (a = [0:3])
            rotate([0, 0, 90 * a])
            translate([0, 40, VH - 4.5])
            rotate([90, 0, 0]) weightAirGapLeg();
        translate([0, 0, VH + 115.5])
            weightAirGapCross();
        translate([0, 0, VH + 10.5])
            weightAirGapLowerCross();
    }
}
