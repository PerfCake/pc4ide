package org.perfcake.ide.editor.shapes;

import javax.swing.JFrame;

import java.awt.EventQueue;
import java.awt.geom.Point2D;
import java.util.concurrent.TimeUnit;

/**
 * Created by jknetl on 8/1/16.
 */
public class Main {

	private static CircularSector sector1;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.setTitle("Java2d example");
				frame.setSize(300, 200);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Point2D center = new Point2D.Double(500,500);
				sector1 = new CircularSector("generator", center,400, 50, 140, 45);

				frame.add(sector1);
				frame.setVisible(true);

			}
		});

//		sleep();

//		EventQueue.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//
//				int newAlpha = 90;
//				System.out.println("changing alpha to " + newAlpha);
//				sector1.setAngleExtent(newAlpha);
//
//			}
//		});

//		sleep();
//
//		EventQueue.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
//				System.out.println("revalidating");
//				sector1.revalidate();
//
//			}
//
//		});

//		sleep();

//		EventQueue.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
//
//				System.out.println("repainting");
//				sector1.repaint();
//
//			}
//		});
	}

	private static void sleep() {
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
