package utils;



import java.util.Arrays;
import java.io.IOException;

import modelvyz.Updateable;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;





public class ConnexionMouse implements Updateable<Integer>{
	public static void main(String []args) {
		try {
			final ConnexionMouse m = new ConnexionMouse();
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						m.update(null);
						m.printCurrentState();
					}
				}
			}).start();
		} catch(Exception e) {}
	}

	public void getNavigator() throws IOException {

		//    	com.sun.opengl.impl.NativeLibLoader.setLoadingAction(new com.sun.opengl.impl.NativeLibLoader.LoaderAction(){
		//    		public void loadLibrary(String libname, String[] preload, boolean doPreload, boolean ignoreError){
		//    			System.out.println("-------------------TESTING-------------------");
		//    			if (doPreload) {
		//    				for (int i=0; i<preload.length; i++) {
		//    					try {
		//    						ResourceLocator.loadLibrary(preload[i]);
		//    					}
		//    					catch (UnsatisfiedLinkError e) {
		//    						if (!ignoreError && e.getMessage().indexOf("already loaded") < 0) {
		//    							throw e;
		//    						}
		//    					}
		//    				}
		//    			}
		//
		//    			ResourceLocator.loadLibrary(libname);
		//    		}
		//    	});


		ControllerEnvironment ce =
				ControllerEnvironment.getDefaultEnvironment();


		//        System.loadLibrary("jinput-linux64");
		// System.loadLibrary("jinput-osx");
		// retrieve the available controllers
		Controller[] controllers = ce.getControllers();
		System.out.println("All controllers: " + Arrays.toString(controllers));
		for(Controller c : controllers)
			if(c.getName().equals("SpaceNavigator")) {
				//            if(c.getName().equals("3Dconnexion SpaceNavigator")) {
				navigator = c;
				break;
			}
		if(navigator == null)
			throw new IOException("Mouse not found");
		System.out.println("All controllers: " + Arrays.toString(navigator.getComponents()));

		//MAC enumerated
		x = navigator.getComponents()[0];
		y = navigator.getComponents()[1];
		z = navigator.getComponents()[2];
		rx = navigator.getComponents()[3];
		ry = navigator.getComponents()[4];
		rz = navigator.getComponents()[5];

		b1 = navigator.getComponents()[6];
		b2 = navigator.getComponents()[7];

		//Linux enumerated
		//        b1 = navigator.getComponents()[0];
		//        b2 = navigator.getComponents()[1];
		//        
		//        x = navigator.getComponents()[2];
		//        y = navigator.getComponents()[3];
		//        z = navigator.getComponents()[4];
		//        rx = navigator.getComponents()[5];
		//        ry = navigator.getComponents()[6];
		//        rz = navigator.getComponents()[7];


	}

	public ConnexionMouse() throws IOException {
		getNavigator();
		printControllerDetails(navigator);
	}

	static Controller navigator = null;
	Component x, y, z, rx, ry, rz, b1, b2;

	float fx, fy, fz, frx, fry, frz;
	int ib1, ib2;



	public float getX() {
		return fx;
	}

	public float getY() {
		return fy;
	}

	public float getZ() {
		return fz;
	}

	public float getRX() {
		return frx;
	}

	public float getRY() {
		return fry;
	}

	public float getRZ() {
		return frz;
	}

	public int getButton1() {
		return ib1;
	}

	public int getButton2() {
		return ib2;
	}

	private void updateState() {
		fx = x.getPollData();
		fy = y.getPollData();
		fz = z.getPollData();

		frx = rx.getPollData();
		fry = ry.getPollData();
		frz = rz.getPollData();

		ib1 = (int)b1.getPollData();
		ib2 = (int)b2.getPollData();
	}


	private void printCurrentState() {
		Component[] cs = {x, y, z, rx, ry, rz, b1, b2};
		System.out.println("<<<<State");
		for(Component c : cs) {
			System.out.println("Button/Axis: " + c.getName() + " has value: " + c.getPollData());
		}
		System.out.println(">>>>State");
	}

	private static void printControllerDetails(Controller c) {
		// shows basic information about this controller
		System.out.println("name: " + c.getName());
		System.out.println("type: " + c.getType());
		System.out.println("port: " + c.getPortType()); 


		// shows information about each Axis instance of this controller

		printAxesDetails(c.getComponents());
	}


	private static void printAxesDetails(Component[] axes) {
		if (axes.length > 0) {
			System.out.println("axes:");


			// iterate through all axes and print their information
			for (int i = 0; i < axes.length; i++) {
				//                if()
				System.out.println(i
						+ " - "  + axes[i].getName());
				//                        + " - "  + axes[i].getIdentifier()
				//                        + " - "  + (axes[i].isRelative() ? "relative" : "absolute")
				//                        + " - "  + (axes[i].isNormalized() ? "normalized" : "arbitrary")
				//                        + " - "  + (axes[i].isAnalog() ? "analog" : "digital")
				//                        + " - "  + axes[i].getDeadZone());
			}
		}
	}


	@Override
	public void update(Integer newdata) {
		navigator.poll();
		//      printCurrentState();
		updateState();
	} 
}
