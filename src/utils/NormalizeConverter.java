package utils;

public class NormalizeConverter {
/*	void global() {

		g_a = None;
		g_b = None;

		//Calibrate unit->world

		//lightblue/pink/green/darkblue
		
		g_px = numpy.array([0.4, 2.3, 4.2, 2.3]);
		g_py = numpy.array([2.44, 0.4, 2.44, 4.48]);
		//g_px = numpy.array([2.3,  0.4,  2.3, 4.2])
		//g_py = numpy.array([4.48, 2.44, 0.4, 2.44])

		//compute coefficients
		AI = numpy.mat('1 0 0 0;-1 1 0 0;-1 0 0 1;1 -1 1 -1');


		g_a = numpy.dot(AI, g_px);
		g_a = numpy.squeeze(g_a);
		//print "g_a: \n", str(g_a)
		g_b = numpy.dot(AI, g_py);
		g_b = numpy.squeeze(g_b);
		//print "g_b: \n", str(g_b)
	}

	void unit2world(pos) {
		g = pos[0];
		h = pos[1];
		k = g*h;
		unit = numpy.bmat([[[1], [g], [h], [k]]]);
		world_x = numpy.asscalar(numpy.dot(g_a, unit.T));
		world_y = numpy.asscalar(numpy.dot(g_b, unit.T));
		return (world_x, world_y);
	}

	void CalibrateFunc() {
		//for each calibration marker, construct matrix to do least squares with
		numrows = 2*len(self.calibrationmarkers);
		A = numpy.zeros((numrows, 9));
		B = numpy.zeros((numrows, 1));
		// marker 1: 0,0
		// marker 2: 1,0
		// marker 3: 1,1
		// marker 4: 0,1
		//B(0),B(1) = 0   //marker 1
		B[2] = 1; B[5]=1; //marker 2,3
		B[4] = 1;
		B[6] = 0; B[7]=1;
		//B[3] = 1; B[4]=1 //marker 2,3
		//B[6] = 1; B[7]=1 //marker 4


		//create our polygon
		self.px = numpy.array([-1, 8, 13, -4]);
		self.py = numpy.array([-1, 3, 11, 8]);
		i = 0;
		for calib in self.calibrationmarkers {
			self.px[(3+i)%4] = calib.pos[0];
			self.py[(3+i)%4] = -calib.pos[1];
			i = i + 1;
		}

		print "PX: \n", str(self.px);
		print "PY: \n", str(self.py);
		//compute coefficients

		//A=numpy.mat('1 0 0 0;1 1 0 0;1 1 1 1;1 0 1 0')
		//print "A: \n", str(A)
		//AI = numpy.invert(A)
		AI = numpy.mat('1 0 0 0;-1 1 0 0;-1 0 0 1;1 -1 1 -1');

		//print "AI: \n", str(AI)
		//print "shapes: ", str(AI.shape), " and ", str(self.px.T.shape)
		self.a = numpy.dot(AI, self.px);
		self.a = numpy.squeeze(self.a);
		print "a: \n", str(self.a);
		self.b = numpy.dot(AI, self.py);
		self.b = numpy.squeeze(self.b);
		print "b: \n", str(self.b);

		//classify points as internal or external
		//plot_internal_and_external_points(px,py,a,b);

		return self.a, self.b;
	}
	// converts physical (x,y) to logical (l,m)
	void FrameCorrect(self, x, y){
		//quadratic equation coeffs, aa*mm^2+bb*m+cc=0
		//y = 240 - y
		y = -y;
		a = self.a;
		b = self.b;
		aa = a[0,3]*b[0,2] - a[0,2]*b[0,3];
		bb = a[0,3]*b[0,0] -a[0,0]*b[0,3] + a[0,1]*b[0,2] - a[0,2]*b[0,1] + x*b[0,3] - y*a[0,3];
		cc = a[0,1]*b[0,0] -a[0,0]*b[0,1] + x*b[0,1] - y*a[0,1];
		//compute m = (-b+sqrt(b^2-4ac))/(2a)
		det = math.sqrt(bb*bb - 4*aa*cc);
		m = (-bb+det)/(2*aa);

		//compute l
		l = (x-a[0,0]-a[0,2]*m)/(a[0,1]+a[0,3]*m);
		return l,m;
	}*/
}
