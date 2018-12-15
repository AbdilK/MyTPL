/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL.exception;

/**
 *
 * @author Abdil-K
 */
public class DALException extends Exception
{
    public DALException()
    {
        // Constructor from the class we inherit from. (Exception)
        super();
    }
    public DALException(String message)
    {
        super(message);
    }
    public DALException(String message, Exception ex) {
        super(message, ex);
    }
}
