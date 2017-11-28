/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.text.DecimalFormat;

/**
 * This class encapsulates the data structure and functionality behind a centriod of a cluster.
 * 
 * Essentially, this object represents a point in 2D Euclidean space.
 * 
 * @author Nathan Minor
 */
public class Centroid {
    
    // private local fields
    private double _x;
    private double _y;
    private DecimalFormat _df;
    
    /**
     * Constructs a blank centroid with coordinates (0.0, 0.0).
     */
    public Centroid() {
        this._x = 0.0;
        this._y = 0.0;
        this._df = new DecimalFormat("#.##");
    }
    
    /**
     * Constructs a new Centroid object with assigned values.
     * @param x First coordinate for the centroid's location in 2D Eucliean space.
     * @param y Second coordinate for the centroid's location in 2D Eucliean space.
     */
    public Centroid (double x, double y) {
        
        // assign initial values appropriately
        this._x = x;
        this._y = y;
        this._df = new DecimalFormat("#.##");
    }
    
    /**
     * "Get" method for retrieving the first coordinate for this centroid in 2D Euclidean space
     * @return First coordinate, x.
     */
    public double getX() {
        return this._x;
    }
    /**
     * "Get" method for retrieving the second coordinate for this centroid in 2D Euclidean space
     * @return First coordinate, y.
     */
    public double getY() {
        return this._y;
    }
    
    /**
     * Returns the distance to another centroid in 2D Euclidean using standard Euclidean distance.
     * @param p The centroid to measure the distance to, relative to this centroid.
     * @return The Euclidean distance between this centroid and another centroid, p.
     */
    public double distanceTo(Centroid p) {
        
        double d = 0.0;
        
        try {
            d = Double.valueOf(this._df.format(Math.sqrt(Math.pow((this._x - p.getX()), 2.0) + Math.pow((this._y - p.getY()), 2.0))));
        } catch (Exception ex) {
            d = Double.NaN;
            System.err.print(ex.getMessage());
        }
         
        return d;
    }
    
    /**
     * Overridden method used for determining if two centroids are equal.
     * @param o The centroid to compare with.
     * @return Whether or not the centroids have all three fields the same (x, y, and value).
     */
    @Override
    public boolean equals(Object o) {
        
        // local variables
        boolean eq = true;
        Centroid p;
        
        // use try catch block to trap any exceptions
        try {
            
            if (o instanceof Centroid) {

                // cast input object as Centroid object
                p = (Centroid) o;

                // determine if this is equal to p
                eq = (this._x == p.getX()) & (this._y == p.getY());

            }
        } catch (Exception ex) {
            
            // set determination to fales when an exception was had
            eq = false;
            System.err.print(ex.getMessage());
        }
        
        // return determination
        return eq;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this._x) ^ (Double.doubleToLongBits(this._x) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this._y) ^ (Double.doubleToLongBits(this._y) >>> 32));
        return hash;
    }
}
