//////////////////////////////////
///    SC1, frame version 1    ///
///   Author: Maxim Buzdalov   ///
///    For "Wow, That Flies"   ///
/// License: WTF Public License //
//////////////////////////////////

// This is a single-copter with a set of options for flap arrangement.
// This machine has no duct, all components are stacked in the top assembly,
// including the battery which is locked between the last two floors
// and can move with a lot of friction, which can be used for CoG balancing.

// THE DRAWINGS ARE PRESENTED AS THEY ARE!
// THERE WERE NO ATTEMPTS TO OPTIMIZE THE CODE FOR UNDERSTANDING!
// Maybe one day I will do it.

// The floors here are connected using standard M3 stack assemblies.
// The inner/outer parts of the main mount are also connected with M3 screws,
// whereas the outer ring, legs and the bottom constrainer are all glued.

// Flaps are designed to be printed in halves with extremely thin layer thickness
// and then assembled in such a way that the M3 screws are screwed into the flap ring,
// then the flaps are assembled around their heads and servo arms.
// The screws have to be screwed in a way that
// the entire flap assembly is pre-tensioned.
// 
// This is not the best design ever. One known drawback is that these flaps
// cannot deflect independently at sufficient angles, they need to be tapered
// from the inside. The tests showed that the clearances are enough to hover
// and perform reasonably gentle maneuvres. Part of the reason why is that
// all flaps deflect by the same amount to counteract yaw, and this somewhat
// increases the clearances for pitch and roll.
//
// This all may change in subsequent revisions when I finally
// return to do something with this machine.

$fa = 1;
$fs = 0.1;

// General geometry
eps = 0.01;   // negligible but positive thickness
inf = 10000;  // a very large length

// Standard holes
//m2HoleR = 1.1;
//m3HoleR = 1.75;
//m4HoleR = 2.2;

m3HoleFree = 1.6;
m25HoleThread = 1.2;

m3NutOuter = 3.1; // not universal, should be adjusted for particular nuts

rMin = 130;
rPlatformMount = 35;
rBifur = 70;
thick = 5;

module mountInner() {
    mountHoleOffset = 19 / 2;
    
    difference() {
        linear_extrude(thick) difference() {
            offset(r = -3) offset(delta = +3) // this rounds concave corners
            union() {
                // Main motor mount location
                circle(13);

                // Slight strengthening of the mount points
                for (a = [0, 90, 180, 270])
                    rotate([0, 0, a])
                    translate([mountHoleOffset, 0])
                    circle(5);

                // Six inner hands 
                for (a = [0:5])
                    rotate([0, 0, 360 / 6 * (a + 0.25)])
                    translate([10, -5])
                    square([rBifur - 6, 10]);

                // Strengthening around platform mount points
                for (a = [0:5])
                    rotate([0, 0, 360 / 6 * (a + 0.25)])
                    translate([rPlatformMount, 0])
                    circle(6.5);
            }

            // Motor mount holes
            for (a = [0, 90, 180, 270])
                rotate([0, 0, a])
                translate([mountHoleOffset, 0])
                circle(m3HoleFree);

            for (a = [0:5])
                rotate([0, 0, 360 / 6 * (a + 0.25)]) {
                    // Holes to join inner and outer hands
                    translate([rBifur, 0]) circle(m3HoleFree);
                    // Holes to join inner hands and the electronics platform
                    translate([rPlatformMount, 0]) circle(m3HoleFree);
                }
        }

        for (a = [0:5])
            rotate([0, 0, 360 / 6 * (a + 0.25)]) {
                // Gaps to join inner and outer hands
                translate([rBifur - 2.5, -2.5, thick / 2])
                    cube([7 + eps, 5, thick / 2 + eps]);
                // Nut holes to join inner and outer hands
                translate([rBifur, 0, -eps])
                    cylinder(r = m3NutOuter, h = 1, $fa = 60);
                // Nut holes to join inner hands and the electronics platform.
                translate([rPlatformMount, 0, -eps])
                    cylinder(r = m3NutOuter, h = 1, $fa = 60);
            }
    }
}

module mountOuter() {
    angle = 360 / 24;

    outerMidX = rMin * cos(angle);
    outerMidY = rMin * sin(angle);
    outerSideX1 = (rMin + 2) * cos(angle - 1);
    outerSideY1 = (rMin + 2) * sin(angle - 1);
    outerSideX2 = (rMin + 2) * cos(angle + 1);
    outerSideY2 = (rMin + 2) * sin(angle + 1);

    outerMidXLim = (rMin + 40) * cos(angle * 2);
    outerMidYLim = (rMin + 40) * sin(angle * 2);
    
    difference() {
        union() {
            linear_extrude(thick) difference() {
                offset(r = -3) offset(delta = +3) // this rounds concave corners
                offset(r = +1) offset(delta = -1) // this rounds convex corners
                union() {
                    // The inner mountpoint
                    translate([rBifur - 2.45, -5]) square([9.45, 10]);
                    // The outer arc
                    intersection() {
                        difference() {
                            circle(rMin + 5);
                            circle(rMin);
                        }
                        polygon([
                            [0, 0], [outerMidXLim, +outerMidYLim], 
                            [outerMidXLim, -outerMidYLim]
                        ]);
                    }
                    // The actual hand pairs
                    for (side = [-1, +1])
                        polygon([
                            [rBifur + 7, side], [outerSideX1, side * outerSideY1],
                            [outerSideX2, side * outerSideY2],
                            [rBifur + 7, side * 7],
                            [rBifur, side * 5]
                        ]);
                    // The outer mountpoint
                    for (a = [-angle, +angle])
                        rotate([0, 0, a])
                        translate([rMin, -6])
                        square([7.5, 12]);
                }
                // Holes to join inner and outer hands
                translate([rBifur, 0]) circle(m3HoleFree);
                // Outer mountpoint holes
                for (a = [-angle, +angle])
                    rotate([0, 0, a])
                    translate([rMin + 2, -3.1])
                    square([3.1, 6.2]);
                // Cutouts for inner mount points
                translate([rBifur - 6, -5.05]) square([10.05, 2.6]);
                translate([rBifur - 6, 2.45]) square([10.05, 2.6]);
            }    
            // Cross-mounts
            rotate([0, 0, -2 * angle])
                translate([rMin, 0, thick / 2])
                linear_extrude(thick / 2)
                polygon([[1.5, -5], [2.5, +5], [3.5, -5]]);

        }
        // Lower cutout for inner mount points
        translate([10, -5.05, -eps]) cube([rBifur - 6, 10.1, eps + thick / 2]);
        // Cutout for cross-mounts
        rotate([0, 0, 2 * angle])
            translate([rMin, 0, thick / 2])
            linear_extrude(thick / 2 + eps)
            polygon([[1.45, -5.05], [2.5, +5.05], [3.55, -5.05]]);
    }
}

module legOutline() {
    offset(r = -3) offset(delta = +3) // this rounds concave corners
    polygon([
        [5, +5],
        [6, +5],
        [7, +4],
        [65, +4],
        [69, +8],
        [95, +8],
        [103, +8],
        [103, -8],
        [95, -8],
        [69, -8],
        [65, -4],
        [7, -4],
        [6, -5],
        [5, -5],
    ]);
}

module leg() {
    difference() {
        union() {
            linear_extrude(3) polygon([
                [0, +3],
                [5, +3],
                [5, -3],
                [0, -3], 
            ]);
            linear_extrude(3) difference() {
                legOutline();
                translate([74.5, -4.2]) 
                    square([20, 8.4]);
                translate([72, 0])
                    circle(1);
                translate([97, 0])
                    circle(1);
            }
            linear_extrude(5.5) difference() {
                legOutline();
                offset(r = -0.8) legOutline();
            }
            difference() {
                linear_extrude(21) difference() {
                    legOutline();
                    offset(r = -0.8) legOutline();
                }
                rotate([0, -45, 0])
                    translate([0, -20, -37])
                    cube([120, 40, 55]);
            }
        }
        translate([99, +7, 2])
            cube([3 + eps, 2, 2]);
        translate([99, -9, 2])
            cube([3 + eps, 2, 2]);
    }
}

module legInset() {
    rotate([0, 0, -360 / 24])
        translate([rMin + 4.05, +7, 0])
        cube([1.9, 1.9, 2.9]);
    rotate([0, 0, +360 / 24])
        translate([rMin + 4.05, -8.9, 0])
        cube([1.9, 1.9, 2.9]);
    intersection() {
        difference() {
            cylinder(r = rMin + 6.5, h = 4);
            translate([0, 0, -eps]) cylinder(r = rMin + 2.23, h = 1 + 2 * eps);
            translate([0, 0, 1]) cylinder(r = rMin + 5.7, h = 3 + eps);
            rotate([0, 0, -360 / 24])
                translate([rMin + 1, -8.05, -eps])
                cube([21, 16.1, 4 + 2 * eps]);
            rotate([0, 0, +360 / 24])
                translate([rMin + 1, -8.05, -eps])
                cube([21, 16.1, 4 + 2 * eps]);
        }
        linear_extrude(4) polygon([
            [0, 0],
            [(rMin + 20) * cos(360 / 24), +(rMin + 20) * sin(360 / 24)],
            [(rMin + 20) * cos(360 / 24), -(rMin + 20) * sin(360 / 24)]
        ]);
    }
}

module platformFloor1() {
    linear_extrude(3) difference() {
        // The main polygon
        offset(5) polygon([ 
            for (a = [0:5]) 
                [rPlatformMount * cos(360 / 6 * (a + 0.25)), 
                 rPlatformMount * sin(360 / 6 * (a + 0.25))]
        ]);
        // Holes to join inner hands and the electronics platform
        for (a = [0:5]) 
            rotate([0, 0, 360 / 6 * (a + 0.25)])
            translate([rPlatformMount, 0]) 
            circle(m3HoleFree);
        // Holes to mount the power module
        for (dx = [-1, 1], dy = [-1, 1])
            translate([dx * 30.5 / 2, dy * 30.5 / 2])
            circle(m3HoleFree);
    }
    color("blue") {
        translate([-27 / 2, -9.8, 0]) cube([27, 0.8, 7]);
        translate([-27 / 2, +9, 0]) cube([27, 0.8, 7]);
        translate([27 / 2 - 0.8, -9.8, 0]) cube([0.8, 2 * 9.8, 4.6]);
        translate([-27 / 2, -9, 0]) cube([27, -16.5 / 2 + 9, 4]);
        translate([-27 / 2, 16.5 / 2, 0]) cube([27, 9 - 16.5 / 2, 4]);
        translate([-27 / 2, -9, 0]) cube([5, 1, 4.6]);
        translate([-27 / 2, 8, 0]) cube([5, 1, 4.6]);
    }
}

module platformFloor2() {
    linear_extrude(3) difference() {
        // The main polygon
        offset(5) polygon([ 
            for (a = [0:5]) 
                [rPlatformMount * cos(360 / 6 * (a + 0.25)), 
                 rPlatformMount * sin(360 / 6 * (a + 0.25))]
        ]);
        // Holes to join inner hands and the electronics platform
        for (a = [0:5]) 
            rotate([0, 0, 360 / 6 * (a + 0.25)])
            translate([rPlatformMount, 0]) 
            circle(m3HoleFree);
    }
    color("blue") 
    rotate([0, 0, 360 / 24 * 11]) {
        translate([5, -27 / 2 - 0.8, 0]) cube([30, 0.8, 7]);
        translate([5, +27 / 2, 0]) cube([30, 0.8, 7]);
        translate([5, -27 / 2, 0]) cube([30, 0.8, 5]);
        translate([5, +27 / 2 - 0.8, 0]) cube([30, 0.8, 5]);
    }
}

module platformFloor3() {
    linear_extrude(3) difference() {
        // The main polygon
        offset(5) polygon([ 
            for (a = [0:5]) 
                [rPlatformMount * cos(360 / 6 * (a + 0.25)), 
                 rPlatformMount * sin(360 / 6 * (a + 0.25))]
        ]);
        // Holes to join inner hands and the electronics platform
        for (a = [0,2,3,5]) 
            rotate([0, 0, 360 / 6 * (a + 0.25)])
            translate([rPlatformMount, 0]) 
            circle(m3HoleFree);
    }
    color("blue") {
        translate([-22 - 0.8, -21, 0]) cube([0.8, 42, 7]);
        translate([22, -21, 0]) cube([0.8, 42, 7]);
    }
}

module platformFloor4() {
    linear_extrude(3) difference() {
        // The main polygon
        offset(5) polygon([ 
            for (a = [0:5]) 
                [rPlatformMount * cos(360 / 6 * (a + 0.25)), 
                 rPlatformMount * sin(360 / 6 * (a + 0.25))]
        ]);
        // Holes to join inner hands and the electronics platform
        for (a = [0,2,3,5]) 
            rotate([0, 0, 360 / 6 * (a + 0.25)])
            translate([rPlatformMount, 0]) 
            circle(m3HoleFree);
    }
}


module flapRing4() {
    difference() {
        cylinder(h = 6, r = 15);
        translate([0, 0, -eps]) cylinder(h = 6 + 2 * eps, r = 10);
        for (a = [0, 90, 180, 270])
            rotate([0, 0, a])
            translate([0, 0, 3])
            rotate([90, 0, 0])
            cylinder(h = 16, r = m25HoleThread);
    }
}

module flapRing3() {
    difference() {
        cylinder(h = 6, r = 15);
        translate([0, 0, -eps]) cylinder(h = 6 + 2 * eps, r = 10);
        for (a = [0, 120, 240])
            rotate([0, 0, a])
            translate([0, 0, 3])
            rotate([90, 0, 0])
            cylinder(h = 16, r = m25HoleThread);
    }
}

module foilHalf() { // E169-il from airfoiltools.com
    scale([75, 75])
    polygon([
       [1.00000, 0.00000], [0.99640, 0.00022], 
       [0.98598, 0.00115], [0.96948, 0.00290], 
       [0.94737, 0.00514], [0.91970, 0.00771],
       [0.88673, 0.01081], [0.84899, 0.01458], 
       [0.80708, 0.01904], [0.76168, 0.02416], 
       [0.71346, 0.02985], [0.66316, 0.03596],
       [0.61148, 0.04231], [0.55912, 0.04866], 
       [0.50675, 0.05477], [0.45499, 0.06036], 
       [0.40442, 0.06516], [0.35555, 0.06889],
       [0.30884, 0.07127], [0.26456, 0.07202], 
       [0.22289, 0.07110], [0.18408, 0.06858], 
       [0.14839, 0.06450], [0.11605, 0.05896],
       [0.08721, 0.05212], [0.06206, 0.04427], 
       [0.04085, 0.03567], [0.02379, 0.02657], 
       [0.01106, 0.01729], [0.00290, 0.00819],
       [0.00000, 0.00000]
    ]);
}

module foilHalfProtected() {
    foilHalf();
    translate([40, 0]) square([35, 1]);
}

module flapHalf() {
    difference() {
        union() {
            translate([0, 3, 0])
                rotate([90, 0, 0])
                linear_extrude(100 + 3 + 5) foilHalf();

            for (dy = [3, -99])
                translate([0, dy, 0])
                rotate([90, 0, 0])
                linear_extrude(6)
                foilHalfProtected();

            for (dy = [-3, -99])
                translate([0, dy, 0])
                cube([75, 1, 2.5]);

        }
        // inner end holes
        translate([25, -105 - eps, 0])
            rotate([-90, 0, 0])
            cylinder(h = 5, r = 1.4);
        translate([25, -103, 0])
            rotate([-90, 0, 0])
            cylinder(h = 3, r = 3);

        // outer end holes
        translate([25, -1.1, 0])
            rotate([-90, 0, 0])
            cylinder(h = 4.1 + eps, r = 3.2);
        translate([25, 0, -eps])
            cube([14, 1.05, 1.9]);
        
        // alignment holes
        for (dy = [30, 70])
            translate([25, -dy - 2, -eps])
            cube([20, 4, 2]);
    }
}

module flapClip() {
    difference() {
        offset(r = 0.85) {
            foilHalfProtected();
            mirror([0, 1]) foilHalfProtected();
        }
        offset(r = 0.05) {
            foilHalfProtected();
            mirror([0, 1]) foilHalfProtected();
        }
    }
}

module flapSet() {
    translate([2, 0, 0]) flapHalf();
    translate([-2, 0, 0]) mirror([1, 0, 0]) flapHalf();
    
    for (dx = [0, 1])
        translate([85 + 10 * dx, -100 + 100 * dx, 0])
        rotate([0, 0, 90 + 180 * dx]) {
            linear_extrude(3) flapClip();
            translate([10, -2, 0]) cube([19.95, 3.95, 3.5]);
        }
}

module flapAssembly() {
    flapHalf();
    mirror([0, 0, 1]) flapHalf();
    color("blue") {
        for (dy = [1, -99])
        translate([0, dy, 0])
        rotate([90, 0, 0]) 
        linear_extrude(3, convexity = 3)
        flapClip();
    }
}

module legBottom() {
    difference() {
        // main cube
        translate([-5, -9.2, 0]) cube([5 + 2 + 30 + 2, 18.4, 15]);

        // cutout at the logical top
        translate([-5 - eps, -8.1, 1.2]) cube([5, 16.2, 14 + eps]);
        translate([-5 - eps, -7, -eps]) cube([5, 14, 15 + 2 * eps]);

        // left volume cutout
        translate([2, -8, 1.2]) cube([30, 7, 14 + eps]);
        translate([3, -7, -eps]) cube([28, 5, 15 + 2 * eps]);

        // right volume cutout
        translate([2, 1, 1.2]) cube([30, 7, 14 + eps]);
        translate([3, 2, -eps]) cube([28, 5, 15 + 2 * eps]);

        // circular cutout
        translate([17, -2, 30]) rotate([-90, 0, 0]) cylinder(h = 4, r = 15 * sqrt(2));
        
        for (dx = [3, 9, 15, 21, 27]) {
            translate([dx, -10 - eps, 2]) cube([4, 2 + 2 * eps, 10]);
            translate([dx, 8 - eps, 2]) cube([4, 2 + 2 * eps, 10]);
        }
    }
}

module wireLatchInner(halfWidth) {
    polygon([
        [-halfWidth, 0],
        [-halfWidth, thick + 0.1],
        [-halfWidth + 1.5, thick + 0.1],
        [-halfWidth + 1.5, thick + 3],
        [+halfWidth - 1.5, thick + 3],
        [+halfWidth - 1.5, thick + 0.1],
        [+halfWidth, thick + 0.1],
        [+halfWidth, 0],
    ]);
}

module wireLatch(halfWidth) {
    linear_extrude(3) difference() {
        offset(delta = 1.2) wireLatchInner(halfWidth);
        wireLatchInner(halfWidth);
        translate([-halfWidth + 1, -1.2 - eps]) square([2 * halfWidth - 2, 1.2 + 2 * eps]);
    }
}

module allLatches() {
    for (dx = [0:3]) translate([dx * 14, 0, 0]) wireLatch(5);
    for (dx = [0:3]) translate([dx * 14, 12, 0]) wireLatch(5);
    for (dx = [0:3]) translate([dx * 14, 24, 0]) wireLatch(2.5);
    for (dx = [0:3]) translate([dx * 14, 36, 0]) wireLatch(2.8);
}

color("green") render(convexity = 2) mountInner();

color("green")
    for (a = [0:5])
    rotate([0, 0, 360 / 6 * (a + 0.25)])
    render(convexity = 2) mountOuter();

color("green")
    for (a = [0:11])
    rotate([0, 0, 360 / 12 * a])
    translate([rMin + 2, 0, +5])
    rotate([0, 90, 0])
    render(convexity = 3) leg();
    
color("green")
    for (a = [0:11])
    rotate([0, 0, 360 / 12 * (a + 0.5)])
    translate([0, 0, 2.05 - 99])
    render() legInset();

color("green")
    translate([0, 0, 10]) 
    platformFloor1();

color("green")
    translate([0, 0, 30]) 
    platformFloor2();

translate([0, 0, 50]) 
    platformFloor3();

translate([0, 0, 70]) 
    platformFloor4();

translate([0, 0, 90]) 
    platformFloor4();
    
translate([0, 0, -80]) flapRing4();

for (a = [0:11])
    rotate([0, 0, 360 / 12 * a])
    translate([rMin + 24.3, 0, -99])
    rotate([0, 90, 180])
    render() legBottom();

// could be for instance [0, 120, 240] for three flaps; use flapRing3() in this case.
for (a = [0, 90, 180, 270]) 
    rotate([0, 0, a])
    translate([0, 123, -52])
    rotate([0, 90, 0])
    flapAssembly();