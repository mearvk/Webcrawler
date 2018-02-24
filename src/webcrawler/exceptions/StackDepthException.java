/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.exceptions;

/**
 *
 * @author Max Rupplin
 */
public class StackDepthException extends Exception
{
    public StackDepthException(String msg)
    {
        super(msg);
    }
}
