/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class. 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.prefs.Preferences;

class IMP implements MouseListener{
   JFrame frame;
   JPanel mp;
   JButton start;
   JScrollPane scroll;
   JMenuItem openItem, exitItem, resetItem;
   Toolkit toolkit;
   File pic;
   ImageIcon img;
   int colorX, colorY;
   int [] pixels;
   int [] results;
   //Instance Fields you will be using below
   
   //This will be your height and width of your 2d array
   int height=0, width=0;
   
   //your 2D array of pixels
    int picture[][];

    /* 
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown 
     * menu is how you will open an image to manipulate. 
     */
   IMP()
   {
      toolkit = Toolkit.getDefaultToolkit();
      frame = new JFrame("Image Processing Software by Hunter");
      JMenuBar bar = new JMenuBar();
      JMenu file = new JMenu("File");
      JMenu functions = getFunctions();
      frame.addWindowListener(new WindowAdapter(){
            @Override
              public void windowClosing(WindowEvent ev){quit();}
            });
      openItem = new JMenuItem("Open");
      openItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ handleOpen(); }
           });
      resetItem = new JMenuItem("Reset");
      resetItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ reset(); }
           });     
      exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ quit(); }
           });
      file.add(openItem);
      file.add(resetItem);
      file.add(exitItem);
      bar.add(file);
      bar.add(functions);
      frame.setSize(600, 600);
      mp = new JPanel();
      mp.setBackground(new Color(0, 0, 0));
      scroll = new JScrollPane(mp);
      frame.getContentPane().add(scroll, BorderLayout.CENTER);
      JPanel butPanel = new JPanel();
      butPanel.setBackground(Color.black);
      start = new JButton("start");
      start.setEnabled(false);
      start.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ fun1(); }
           });
      butPanel.add(start);
      frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
      frame.setJMenuBar(bar);
      frame.setVisible(true);      
   }
   
   /* 
    * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods 
    * for handling the choice, fun1, fun2, fun3, fun4, etc. etc. 
    */
   
  private JMenu getFunctions()
  {
     JMenu fun = new JMenu("Functions");
     
     JMenuItem firstItem = new JMenuItem("Bluify");

    
     firstItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){fun1();}
           });
   
       
      fun.add(firstItem);


      JMenuItem secondItem = new JMenuItem("Rotate90");
      secondItem.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){rotate90();}
      });
      fun.add(secondItem);


      JMenuItem gray = new JMenuItem("Grayscale");
      gray.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){luminosity();}
      });
      fun.add(gray);

      JMenuItem blur = new JMenuItem("Blur");
      blur.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){blur();}
      });
      fun.add(blur);

      JMenuItem edge = new JMenuItem("Edge Detection");
      edge.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){edge();}
      });
      fun.add(edge);

      JMenuItem color = new JMenuItem("Color Tracker");
      color.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){color();}
      });
      fun.add(color);


      JMenuItem quiz1 = new JMenuItem("4th Col");
      quiz1.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){quiz1();}
      });
      fun.add(quiz1);

      JMenuItem quiz2 = new JMenuItem("Quiz2Func");
      quiz2.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){quiz2();}
      });
      fun.add(quiz2);
     
      return fun;   

  }
  
  /*
   * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame. 
   * You don't need to worry about this method. 
   */
    private void handleOpen()
  {  
     img = new ImageIcon();
     JFileChooser chooser = new JFileChooser();
      Preferences pref = Preferences.userNodeForPackage(IMP.class);
      String path = pref.get("DEFAULT_PATH", "");

      chooser.setCurrentDirectory(new File(path));
     int option = chooser.showOpenDialog(frame);
     
     if(option == JFileChooser.APPROVE_OPTION) {
        pic = chooser.getSelectedFile();
        pref.put("DEFAULT_PATH", pic.getAbsolutePath());
       img = new ImageIcon(pic.getPath());
      }
     width = img.getIconWidth();
     height = img.getIconHeight(); 
     
     JLabel label = new JLabel(img);
     label.addMouseListener(this);
     pixels = new int[width*height];
     
     results = new int[width*height];
  
          
     Image image = img.getImage();
        
     PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width );
     try{
         pg.grabPixels();
     }catch(InterruptedException e)
       {
          System.err.println("Interrupted waiting for pixels");
          return;
       }
     for(int i = 0; i<width*height; i++)
        results[i] = pixels[i];  
     turnTwoDimensional();
     mp.removeAll();
     mp.add(label);
     
     mp.revalidate();
  }
  
  /*
   * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
   * So this method changes the one dimensional array to a two-dimensional. 
   */
  private void turnTwoDimensional()
  {
     picture = new int[height][width];
     for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          picture[i][j] = pixels[i*width+j];
      
     
  }
  /*
   *  This method takes the picture back to the original picture
   */
  private void reset()
  {
        for(int i = 0; i<width*height; i++)
             pixels[i] = results[i]; 
       Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
     
       mp.revalidate(); 
    }
  /*
   * This method is called to redraw the screen with the new image. 
   */
  private void resetPicture()
  {
       for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          pixels[i*width+j] = picture[i][j];
      Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
     
       mp.revalidate(); 
   
    }
    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
  private int [] getPixelArray(int pixel)
  {
      int temp[] = new int[4];
      temp[0] = (pixel >> 24) & 0xff;
      temp[1]   = (pixel >> 16) & 0xff;
      temp[2] = (pixel >>  8) & 0xff;
      temp[3]  = (pixel      ) & 0xff;
      return temp;
      
    }
    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer. 
     */
  private int getPixels(int rgb[])
  {
         int alpha = 0;
         int rgba = (rgb[0] << 24) | (rgb[1] <<16) | (rgb[2] << 8) | rgb[3];
        return rgba;
  }
  
  public void getValue()
  {
      int pix = picture[colorY][colorX];
      int temp[] = getPixelArray(pix);
      System.out.println("Color value " + temp[0] + " " + temp[1] + " "+ temp[2] + " " + temp[3]);
    }
  
  /**************************************************************************************************
   * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is 
   * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will 
   * have a 2D array called picture that is holding each pixel from your picture. 
   *************************************************************************************************/
   /*
    * Example function that just removes all red values from the picture. 
    * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array 
    * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
    * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B. 
    * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
    * integer value so you can give it back to the program and display the new picture. 
    */
  private void fun1()
  {
     
    for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
       {   
          int rgbArray[] = new int[4];
         
          //get three ints for R, G and B
          rgbArray = getPixelArray(picture[i][j]);
         
        
           rgbArray[1] = 0;

           //take three ints for R, G, B and put them back into a single int
           picture[i][j] = getPixels(rgbArray);
        } 
     resetPicture();
  }

    private void quiz1()
    {

        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
            {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

                if(j % 4 == 0){
                    rgbArray[1] = 0;
                    rgbArray[2] = 0;
                    rgbArray[3] = 0;
                }

                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    private void quiz2()
    {

        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
            {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

                if(rgbArray[3] <= 200){
                    rgbArray[1] = 0;
                    rgbArray[2] = 0;
                    rgbArray[3] = 0;
                }
                else{
                    rgbArray[1] = 255;
                    rgbArray[2] = 255;
                    rgbArray[3] = 255;
                }

                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    private void color()
    {

        int thresh = 300;
        int[] color = {50, 15, 250};

        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
            {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

                int distance = Math.abs(rgbArray[1]-color[0]) + Math.abs(rgbArray[2]-color[1]) + Math.abs((rgbArray[3]-color[2]));
                if(distance <= thresh){
                    int[] white = {rgbArray[0], 255, 255, 255};
                    rgbArray = white;
                    picture[i][j] = getPixels(rgbArray);
                }else{
                    int[] black = {rgbArray[0], 0, 0, 0};
                    rgbArray = black;
                    picture[i][j] = getPixels(rgbArray);
                }

                //take three ints for R, G, B and put them back into a single int
            }
        resetPicture();
    }

    private int[][] copy2d(int[][] matrix){
        int[][] myInt = new int[matrix.length][];
        for(int i = 0; i < matrix.length; i++)
        {
            myInt[i] = new int[matrix[i].length];
            for (int j = 0; j < matrix[i].length; j++)
            {
                myInt[i][j] = matrix[i][j];
            }
        }
        return myInt;

    }

    private int[][] rotateinit(int[][] matrix){
        int[][] myInt = new int[matrix[0].length][];
        for(int i = 0; i < matrix[0].length; i++)
        {
            myInt[i] = new int[matrix.length];
            for (int j = 0; j < matrix.length; j++)
            {
                myInt[i][j] = matrix[j][i];
            }
        }
        return myInt;

    }

    private void edge()
    {
        luminosity();
        int[][] newpic = copy2d(picture);

        for(int i=1; i<height-1; i++)
            for(int j=1; j<width-1; j++)
            {
                int rgbArray[] = new int[4];


                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

                int ft[] = getPixelArray(picture[i-1][j-1]);
                int sc[] = getPixelArray(picture[i-1][j]);
                int th[] = getPixelArray(picture[i-1][j+1]);
                int fr[] = getPixelArray(picture[i][j-1]);
                int ff[] = getPixelArray(picture[i][j]);
                int sx[] = getPixelArray(picture[i][j+1]);
                int sv[] = getPixelArray(picture[i+1][j-1]);
                int et[] = getPixelArray(picture[i+1][j]);
                int nn[] = getPixelArray(picture[i+1][j+1]);


                rgbArray[3] = -ft[3]/9-sc[3]/9-th[3]/9-fr[3]/9+8*ff[3]/9-sx[3]/9-sv[3]/9-et[3]/9-nn[3]/9;

                //take three ints for R, G, B and put them back into a single int
                newpic[i][j] = getPixels(rgbArray);

            }
        picture = newpic;
        resetPicture();
    }

    private void blur()
    {
        luminosity();
        int[][] newpic = copy2d(picture);

        for(int i=1; i<height-1; i++)
            for(int j=1; j<width-1; j++)
            {
                int rgbArray[] = new int[4];


                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

                int ft[] = getPixelArray(picture[i-1][j-1]);
                int sc[] = getPixelArray(picture[i-1][j]);
                int th[] = getPixelArray(picture[i-1][j+1]);
                int fr[] = getPixelArray(picture[i][j-1]);
                int ff[] = getPixelArray(picture[i][j]);
                int sx[] = getPixelArray(picture[i][j+1]);
                int sv[] = getPixelArray(picture[i+1][j-1]);
                int et[] = getPixelArray(picture[i+1][j]);
                int nn[] = getPixelArray(picture[i+1][j+1]);


                rgbArray[3] = ft[3]/9+sc[3]/9+th[3]/9+fr[3]/9+ff[3]/9+sx[3]/9+sv[3]/9+et[3]/9+nn[3]/9;

                //take three ints for R, G, B and put them back into a single int
                newpic[i][j] = getPixels(rgbArray);

            }
        picture = newpic;
        resetPicture();
    }


    private void luminosity()
    {
        //int[][] newpic = copy2d(picture);

        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
            {
                int rgbArray[] = new int[4];


                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);


                rgbArray[3] = (rgbArray[1]/3)+(rgbArray[2]/3)+(rgbArray[3]/3);
                rgbArray[1]=0;
                rgbArray[2]=0;
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);

            }
        //picture = newpic;
        resetPicture();
    }


    private void rotate90()
    {
        int[][] newpic = rotateinit(picture);

        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
            {
                int rgbArray[] = new int[4];

                int newj = -i + height-1;
                int newi = j;
                //System.out.println("height="+picture[i].length+"f("+i+","+j+") = ("+newi+","+newj+")");


                //get three ints for R, G and B

                //take three ints for R, G, B and put them back into a single int
                newpic[newi][newj] = picture[i][j];
            }
        picture = newpic;

            //System.out.println("HERE");
            height = newpic.length;
            width = newpic[0].length;
        resetPicture();
    }
  



  
  private void quit()
  {  
     System.exit(0);
  }

    @Override
   public void mouseEntered(MouseEvent m){}
    @Override
   public void mouseExited(MouseEvent m){}
    @Override
   public void mouseClicked(MouseEvent m){
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }
    @Override
   public void mousePressed(MouseEvent m){}
    @Override
   public void mouseReleased(MouseEvent m){}
   
   public static void main(String [] args)
   {
      IMP imp = new IMP();
   }
 
}