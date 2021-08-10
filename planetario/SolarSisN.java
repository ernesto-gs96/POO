import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.picking.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class SolarSisN extends MouseAdapter{

    protected Transform3D viewTransform = new Transform3D();
    protected Vector3f viewVector;
    protected ViewingPlatform vp;
    protected TransformGroup viewTG;
    private TransformGroup riderPos;
    private TransformGroup solRotX; private TransformGroup mercRotX; private TransformGroup venusRotX;
    private TransformGroup earthRotX; private TransformGroup earthTransX; private TransformGroup marteRotX;
    private TransformGroup jupRotX; private TransformGroup satRotX; private TransformGroup uranoRotX;
    SimpleUniverse universe;
    private PickCanvas pickCanvas;
    private JComboBox jcb;
    private JTextArea jta;
    final JEditorPane jt = new JEditorPane();

    Socket cliente;
    int puerto=8080;
    String host= "localhost";
    ObjectOutputStream salida;
    DataInputStream entrada;
    JFrame f2;

    public SolarSisN(){	 
        BranchGroup group = new BranchGroup();
    	
        // Estrella
        Appearance appsol = new Appearance(); 
        // Planetas 
        Appearance appmercurio = new Appearance(); Appearance appvenus = new Appearance(); Appearance appearth = new Appearance(); 
        Appearance appmarte = new Appearance(); Appearance appjupiter = new Appearance(); Appearance appsaturno = new Appearance();
        Appearance appurano = new Appearance();
        // Lunas 
        Appearance appluna = new Appearance(); Appearance appfobos = new Appearance(); Appearance appdeimos = new Appearance();

        
        // Texturas 
        TextureLoader tex;
        tex=new TextureLoader("SOL.JPG", null);
            appsol.setTexture(tex.getTexture());        
        tex = new TextureLoader("MERCURIO.JPG",null);
            appmercurio.setTexture(tex.getTexture());
        tex = new TextureLoader("VENUS.JPG",null);
            appvenus.setTexture(tex.getTexture());            
        tex = new TextureLoader("TIERRA.JPG", null);
            appearth.setTexture(tex.getTexture());
                tex = new TextureLoader("LUNA.JPG", null);
                    appluna.setTexture(tex.getTexture());        
        tex = new TextureLoader("MARTE.JPG", null);
    	    appmarte.setTexture(tex.getTexture());
                tex = new TextureLoader("FOBOS.JPG",null);
                    appfobos.setTexture(tex.getTexture());
                tex = new TextureLoader("DEIMOS.JPG",null);
                    appdeimos.setTexture(tex.getTexture());
        tex = new TextureLoader("JUPITER.JPG",null);
            appjupiter.setTexture(tex.getTexture());
        tex = new TextureLoader("SATURNO.JPG",null);
            appsaturno.setTexture(tex.getTexture());
        tex = new TextureLoader("URANO.JPG",null);
            appurano.setTexture(tex.getTexture());

    	// Cuerpos celestes 
        Sphere sol = new Sphere(0.10f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appsol);
            sol.setUserData("Sol");
        Sphere mercurio = new Sphere(0.025f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appmercurio);
            mercurio.setUserData("Mercurio");
        Sphere venus = new Sphere(0.024f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appvenus);
            venus.setUserData("Venus");
        Sphere earth = new Sphere(0.030f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appearth);
            earth.setUserData("Tierra");
            Sphere luna = new Sphere(0.019f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appluna);   
                luna.setUserData("Luna");
        Sphere marte = new Sphere(0.033f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appmarte);
            marte.setUserData("Marte");
            Sphere deimos = new Sphere(0.020f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appdeimos);
            Sphere fobos = new Sphere(0.020f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appfobos);
        Sphere jupiter = new Sphere (0.05f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appjupiter);
            jupiter.setUserData("Jupiter");
        Sphere saturno = new Sphere (0.047f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appsaturno);
            saturno.setUserData("Saturno");
        Sphere urano = new Sphere (0.038f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, appurano);
            urano.setUserData("Urano");

        //Rotaciones y Traslaciones 
        solRotX = Posi.rotate(sol, new Alpha(-1, 1250)); 	

        mercRotX = Posi.rotate(mercurio, new Alpha(-1,1250));
        TransformGroup mercTransX = Posi.translate(mercRotX, new Vector3f(0.0f,0.0f,0.2f));
        TransformGroup mercRotGroupX= Posi.rotate(mercTransX, new Alpha(-1,5000));

        venusRotX = Posi.rotate(venus, new Alpha(-1,1250));
        TransformGroup venusTransX = Posi.translate(venusRotX, new Vector3f(0.0f,0.0f,0.3f));
        TransformGroup venusRotGroupX = Posi.rotate(venusTransX, new Alpha(-1,6000));

        earthRotX = Posi.rotate(earth, new Alpha(-1, 1250));
        earthTransX = Posi.translate(earthRotX, new Vector3f(0.0f, 0.0f, 0.4f));
        TransformGroup earthRotGroupX = Posi.rotate(earthTransX, new Alpha(-1, 6000));
        group.addChild(earthRotGroupX);
        
            TransformGroup lunaRotX = Posi.rotate(luna, new Alpha(-1, 1250));
            TransformGroup lunaTransX = Posi.translate(lunaRotX, new Vector3f(0.0f, 0.0f, 0.1f));
            TransformGroup lunaRotGroupX = Posi.rotate(lunaTransX, new Alpha(-1, 4000));
            earthTransX.addChild(lunaRotGroupX);

        marteRotX = Posi.rotate(marte, new Alpha(-1, 1250));
        TransformGroup marteTransX = Posi.translate(marteRotX, new Vector3f(0.0f, 0.0f, 0.5f));
        TransformGroup marteRotGroupX = Posi.rotate(marteTransX, new Alpha(-1, 7000));

            TransformGroup fobosRotX = Posi.rotate(fobos, new Alpha(-1,1250));
            TransformGroup fobosTransX = Posi.translate(fobosRotX, new Vector3f(0.0f, 0.0f, 0.1f));
            TransformGroup fobosRotGroupX = Posi.rotate(fobosTransX, new Alpha(-1,4000));
            marteTransX.addChild(fobosRotGroupX);

            TransformGroup deimosRotX = Posi.rotate(deimos, new Alpha(-1,1250));
            TransformGroup deimosTransX = Posi.translate(deimosRotX, new Vector3f(0.0f, 0.04f, 0.1f));
            TransformGroup deimosRotGroupX = Posi.rotate(deimosTransX, new Alpha(-1,4500));
            marteTransX.addChild(deimosRotGroupX);

        jupRotX = Posi.rotate(jupiter, new Alpha(-1,1250));
        TransformGroup jupTransX = Posi.translate(jupRotX, new Vector3f(0.0f, 0.0f, 0.6f));
        TransformGroup jupRotGroupX = Posi.rotate(jupTransX, new Alpha(-1,7000));

        satRotX = Posi.rotate(saturno, new Alpha (-1,1250));
        TransformGroup satTransX = Posi.translate(satRotX, new Vector3f(0.0f, 0.0f, 0.7f));
        TransformGroup satRotGroupX = Posi.rotate(satTransX, new Alpha(-1,7500));

        uranoRotX = Posi.rotate(urano, new Alpha (-1,1250));
        TransformGroup urTransX = Posi.translate(uranoRotX, new Vector3f(0.0f, 0.0f, 0.9f));
        TransformGroup urRotGroupX = Posi.rotate(urTransX, new Alpha (-1, 8000));



        	group.addChild(solRotX);
            group.addChild(mercRotGroupX);
            group.addChild(venusRotGroupX); 
            group.addChild(marteRotGroupX);
            group.addChild(jupRotGroupX);
            group.addChild(satRotGroupX);
            group.addChild(urRotGroupX);
        	
        	
            GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        	Canvas3D canvas = new Canvas3D(config); canvas.setSize(600,400);
        	
            universe = new SimpleUniverse(canvas);
        	universe.getViewingPlatform().setNominalViewingTransform();
        	 
    	    solRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
            solRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            solRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
            solRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

            mercRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
            mercRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            mercRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
            mercRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

            venusRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
            venusRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            venusRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
            venusRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 

            earthRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
            earthRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            earthRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
            earthRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 

                lunaRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
                lunaRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
                lunaRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
                lunaRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 

            marteRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
            marteRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            marteRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
            marteRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
            
            jupRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
            jupRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            jupRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
            jupRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 

            satRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
            satRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            satRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
            satRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);


            uranoRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
            uranoRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            uranoRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
            uranoRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

            // Universo
            vp = universe.getViewingPlatform();
            viewTG = vp.getViewPlatformTransform();
            universe.addBranchGraph(group); 

        	JFrame f = new JFrame("Planetario"); // Frame para el planetario y el combobox
            f.setLayout(new BorderLayout());
            JPanel jp=new JPanel(); 
            f2 = new JFrame ("Informacion HTML"); // Frame para HTML y video
            JPanel panelhtml = new JPanel();
            //f2.setSize(400,400);
            // panelhtml.setSize(200,200);
            /*
            JPanel video = new JPanel(); // creo que debe cambiarse por un mediapanel
            video.setSize(200,200);
            JLabel vid = new JLabel("Aqui ira el video");
            video.add(vid); 
            */
        	Vector nomPlanet=new Vector();
        	nomPlanet.addElement("Sol"); nomPlanet.addElement("Mercurio"); nomPlanet.addElement("Venus");
        	nomPlanet.addElement("Tierra"); nomPlanet.addElement("Marte"); nomPlanet.addElement("Jupiter");
            nomPlanet.addElement("Saturno"); nomPlanet.addElement("Urano");
            jt.setEditable(false);
            jt.addHyperlinkListener(new MicroBrowser(jt));
            JScrollPane pane = new JScrollPane();
            pane.getViewport().add(jt);
        	jcb=new JComboBox(nomPlanet);
        	jcb.addActionListener(new EventHandler());
        	jp.add(jcb);
        	panelhtml.add(pane);
        	f.add("Center",canvas); f.add("South", jp); 
            f2.add(panelhtml); // f2.add("South",video);
        	pickCanvas = new PickCanvas(canvas, group);
        	pickCanvas.setMode(PickCanvas.BOUNDS);
        	canvas.addMouseListener(this);
    	    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
        	f.pack(); f.setVisible(true); f2.setVisible(false);
    }

    public static void main(String a[]) { new SolarSisN();}

    public void mouseClicked(MouseEvent e){
        pickCanvas.setShapeLocation(e);
        PickResult result = pickCanvas.pickClosest();
        if (result == null) {
           System.out.println("Nothing picked");
        }else {
           Primitive p = (Primitive)result.getNode(PickResult.PRIMITIVE);
           Shape3D s = (Shape3D)result.getNode(PickResult.SHAPE3D);
           if (p != null) {
            System.out.println( "UserData1: "+p.getUserData() );
            System.out.println(p.getClass().getName());  
            viewVector = new Vector3f(.0f,.0f, .65f);
            viewTransform.setTranslation(viewVector);

            try{
                cliente = new Socket (host,puerto);
                try{
                    salida = new ObjectOutputStream(cliente.getOutputStream());
                    System.out.println("Flujo de salida obtenido\n");
                    entrada= new DataInputStream(cliente.getInputStream());
                    System.out.println("Flujo de entrada obetenido\n");
                    System.out.println("Conectado\n");
                }catch(IOException io){
                    io.printStackTrace();
                }
                try{
                    Object sal = (Object)p.getUserData();
                    salida.writeObject(sal);
                    System.out.println("Objeto enviado: "+p.getUserData()+"\n\n");
                }catch(IOException io2){
                    io2.printStackTrace();
                }

            }catch(Exception ex){
                ex.printStackTrace();
            }

    	viewTG.setTransform(viewTransform);
            vp.detach();
            if(p.getUserData().equals("Sol")){
                solRotX.addChild(vp);
                try{
                    File path = new File ("Sol.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}

            }
    	    if(p.getUserData().equals("Tierra")){
            	earthRotX.addChild(vp);
                try{
                    File path = new File ("Tierra.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}

            }
            if(p.getUserData().equals("Marte")){
                marteRotX.addChild(vp);
                try{
                    File path = new File ("Marte.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}

            }
            if(p.getUserData().equals("Mercurio")){
                mercRotX.addChild(vp);
                try{
                    File path = new File ("Mercurio.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}

            }
            if(p.getUserData().equals("Venus")){
                venusRotX.addChild(vp);
                try{
                    File path = new File ("Venus.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
            }
            if(p.getUserData().equals("Jupiter")){
                jupRotX.addChild(vp);
                try{
                    File path = new File ("Jupiter.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
            }
            if(p.getUserData().equals("Saturno")){
                satRotX.addChild(vp);
                try{
                    File path = new File ("Saturno.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
            }
            if(p.getUserData().equals("Urano")){
                uranoRotX.addChild(vp); 
                try{
                    File path = new File ("Urano.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
            }
            try {
              	
                jt.setPage ("http://localhost:8080/"+p.getUserData()+".html");
                f2.pack();
                f2.setVisible(true);
                String aver = (String)("http://localhost:8080/"+p.getUserData()+".html");
                System.out.println(aver);
            } catch (IOException ex) {
                    System.out.println("URL no valido");
            }
           } else if (s != null) {
               System.out.println( "UserData2: "+s.getUserData() );
                 System.out.println(s.getClass().getName());
    	     jta.setText(s.getClass().getName());
           } else{
              System.out.println("null");
           }
        }
    }

    class EventHandler implements ActionListener {  
        public void actionPerformed(ActionEvent e){
    	   String nombre=(String)jcb.getSelectedItem();
            viewVector = new Vector3f(.0f,.0f, .65f);
            viewTransform.setTranslation(viewVector);   
            viewTG.setTransform(viewTransform);
            vp.detach();
            if(nombre.equals("Sol")){	
            	solRotX.addChild(vp);
                try{
                    File path = new File ("Sol.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
            }       
    	   if(nombre.equals("Tierra")){
            	earthRotX.addChild(vp);
                try{
                    File path = new File ("Tierra.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
           }
           if(nombre.equals("Marte")){
                marteRotX.addChild(vp);

                try{
                    File path = new File ("Marte.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
           }
            if(nombre.equals("Mercurio")){
                mercRotX.addChild(vp);
            try{
                    File path = new File ("Mercurio.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
            }
            if(nombre.equals("Venus")){
                venusRotX.addChild(vp);
                try{
                    File path = new File ("Venus.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
            }
            if(nombre.equals("Jupiter")){
                jupRotX.addChild(vp);
                try{
                    File path = new File ("Jupiter.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
            }
            if(nombre.equals("Saturno")){
                satRotX.addChild(vp);
                try{
                    File path = new File ("Saturno.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();}
            }
            if(nombre.equals("Urano")){
                uranoRotX.addChild(vp);
                try{
                    File path = new File ("Urano.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {ex.printStackTrace();} 
            }

            try {
              	jt.setPage ("http://localhost:8080/"+nombre+".html");
                f2.pack();
                f2.setVisible(true);
                String aver = (String)("http://localhost:8080/"+nombre+".html");
                System.out.println(aver);
            } catch (IOException ex) {
                    System.out.println("URL no valido");
            }
        }
    }
}  