/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

/**
 *
 * @author Abdil-K
 */
public class Path 
{
    private String path;
    
    public Path(String namepath)
    {
        this.path = namepath;
    }
    
    /**
     * Returns a path string.
     * @return 
     */
    public String getPath()
    {
        return path;
    }
}
