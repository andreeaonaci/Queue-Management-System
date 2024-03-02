package org.example;

import Interface.Controller;
import Interface.View;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        View view = new View();
        view.setVisible(true);
        new Controller(view);
    }
}
