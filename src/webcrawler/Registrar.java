/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.util.ArrayList;

/**
 *
 * @author oem
 */
public class Registrar
{
    public ArrayList<Class> classes = new ArrayList();
    
    public void register(Class _class)
    {
        this.classes.add(_class);
    }
}
