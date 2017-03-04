/*
 *-----------------------------------------------------------------------------
 * pc4ide
 *
 * Copyright 2017 Jakub Knetl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *-----------------------------------------------------------------------------
 */

package org.perfcake.ide.editor;

import static java.awt.geom.Arc2D.PIE;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Java2d basics demo.
 * @author Jakub Knetl
 */
public class Java2dBasic extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);

        double radius = 400;
        Point2D center = new Point2D.Double(radius / 2, radius / 2);
        Ellipse2D circle = new Ellipse2D.Double(0, 0, radius, radius);
        Ellipse2D smallCircle = new Ellipse2D.Double(radius / 4, radius / 4, radius / 2, radius / 2);

        Point2D linePointA = new Point2D.Double(radius / 4, radius / 2);
        Line2D line = new Line2D.Double(linePointA, center);


        Line2D measurerX = new Line2D.Double(1, 1, radius, 1);
        Line2D measurerY = new Line2D.Double(1, 1, 1, radius);
        //g2d.draw(circle);
        //g2d.draw(smallCircle);
        g2d.draw(measurerX);
        g2d.draw(measurerY);
        Arc2D arc = new Arc2D.Double();
        arc.setArcByCenter(center.getX(), center.getY(), radius / 4, 180, 45, PIE);
        g2d.draw(arc);
        //g2d.draw(line);
        //
        //// rotate point
        //Point2D rotatedPoint = rotatePoint(linePointA, 135, center);
        //Line2D rotatedLine = new Line2D.Double(rotatedPoint, center);
        //g2d.draw(rotatedLine);
        //
        //
        //
        //AffineTransform defaultTransform = g2d.getTransform();
        //g2d.rotate(Math.PI / 2, center.getX(), center.getY());
        //g2d.draw(line);
        //g2d.setTransform(defaultTransform);


    }

    private Point2D rotatePoint(Point2D point, double theta, Point2D center) {

        AffineTransform transform = new AffineTransform();
        transform.setToRotation(Math.toRadians(theta), center.getX(), center.getY());
        Point2D rotatedPoint = new Point2D.Double();

        return transform.transform(point, rotatedPoint);
    }

    /**
     * Simple example of Java2d
     *
     * @param args args.
     */
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setTitle("Java2D basic example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // final GraphicalPanel editor = new GraphicalPanel(model);
        JPanel panel = new Java2dBasic();

        frame.add(panel);
        frame.setVisible(true);


    }

}
