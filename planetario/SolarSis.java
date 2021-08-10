import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.picking.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class SolarSis extends MouseAdapter {

protected Transform3D viewTransform = new Transform3D();
protected Vector3f viewVector;
protected ViewingPlatform vp = null;
protected TransformGroup viewTG = null;
private TransformGroup riderPos;
private TransformGroup earthRotX;
private TransformGroup solRotX;
private TransformGroup earthTransX;

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

public SolarSis(){	 
	BranchGroup group = new BranchGroup();
	Appearance appsol = new Appearance();
	Appearance appearth = new Appearance();
    Appearance appluna = new Appearance();
    Appearance appmarte = new Appearance();
    TextureLoader tex;

    tex=new TextureLoader("TIERRA.JPG", null);
        appearth.setTexture(tex.getTexture());
    tex=new TextureLoader("MARTE.JPG", null);
	   appmarte.setTexture(tex.getTexture());
	tex=new TextureLoader("SOL.JPG", null);
        appsol.setTexture(tex.getTexture());
	tex=new TextureLoader("LUNA.JPG", null);
	   appluna.setTexture(tex.getTexture());

	Sphere earth = new Sphere(0.045f, Primitive.GENERATE_NORMALS | 		
	Primitive.GENERATE_TEXTURE_COORDS, 32, appearth);
        earth.setUserData("Tierra");

        Sphere luna = new Sphere(0.019f, Primitive.GENERATE_NORMALS | 		
	Primitive.GENERATE_TEXTURE_COORDS, 32, appluna);   
        luna.setUserData("Luna");

        Sphere marte = new Sphere(0.045f, Primitive.GENERATE_NORMALS |		
	Primitive.GENERATE_TEXTURE_COORDS, 32, appmarte);
        marte.setUserData("Marte");

	Sphere sol = new Sphere(0.35f, Primitive.GENERATE_NORMALS | 
	Primitive.GENERATE_TEXTURE_COORDS, 32, appsol);
        sol.setUserData("Sol");

	earthRotX = Posi.rotate(earth, new Alpha(-1, 1250));
	solRotX = Posi.rotate(sol, new Alpha(-1, 1250));
	earthTransX = Posi.translate(earthRotX, new Vector3f(0.0f, 0.0f, 0.7f));

    TransformGroup earthRotGroupX = Posi.rotate(earthTransX, new Alpha(-1, 5000));
    	group.addChild(earthRotGroupX);

    TransformGroup lunaRotX = Posi.rotate(luna, new Alpha(-1, 1250));
    TransformGroup lunaTransX = Posi.translate(lunaRotX, new Vector3f(0.0f, 0.0f, 0.1f));
    TransformGroup lunaRotGroupX = Posi.rotate(lunaTransX, new Alpha(-1, 1000));
    	earthTransX.addChild(lunaRotGroupX);
        TransformGroup marteRotX = Posi.rotate(marte, new Alpha(-1, 1250));
	TransformGroup marteTransX = Posi.translate(marteRotX, new Vector3f(0.0f, 0.0f, 0.5f));
    TransformGroup marteRotGroupX = Posi.rotate(marteTransX, new Alpha(-1, 1000));
    	group.addChild(marteRotGroupX);
    	group.addChild(solRotX);
        //group.addChild(sol);
    	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    	Canvas3D canvas = new Canvas3D(config); canvas.setSize(600,400);
    	universe = new SimpleUniverse(canvas);
    	universe.getViewingPlatform().setNominalViewingTransform();
    	 
	    solRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
        solRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        solRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
        solRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        earthRotX.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
        earthRotX.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        earthRotX.setCapability(Group.ALLOW_CHILDREN_WRITE);
        earthRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        vp = universe.getViewingPlatform();
        viewTG = vp.getViewPlatformTransform();
        universe.addBranchGraph(group); 

    	JFrame f = new JFrame("Planetario");
        f.setSize(1000,600);

        JPanel jp=new JPanel(); // Panel para html
        jp.setSize(600,200);
        
        JPanel panelhtml= new JPanel();
        panelhtml.setSize(400,200);

        JPanel video = new JPanel();
        video.setSize(400,400);
        JLabel vid = new JLabel("Aqui ira el video");
        video.add(vid);

        
    	//f.setLayout(new BorderLayout());
    	Vector nomPlanet=new Vector();
    	nomPlanet.addElement("Sol");
    	nomPlanet.addElement("Tierra");
    	nomPlanet.addElement("Marte");

        jt.setEditable(false);
        jt.addHyperlinkListener(new MicroBrowser(jt));
        JScrollPane pane = new JScrollPane();
        pane.getViewport().add(jt);
    	jcb=new JComboBox(nomPlanet);
    	jcb.addActionListener(new EventHandler());

    	jp.add(jcb);
    	panelhtml.add(pane);

    	f.add("Center",canvas);
    	f.add("South", jp); 
        f.add("West",panelhtml);
        f.add("East",video);

    	pickCanvas = new PickCanvas(canvas, group);
    	pickCanvas.setMode(PickCanvas.BOUNDS);
    	canvas.addMouseListener(this);
	    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
    	f.pack(); 
        f.setVisible(true); 
}
public static void main(String a[]) { new SolarSis();}

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
        if(p.getUserData().equals("Sol"))
        	solRotX.addChild(vp);
	if(p.getUserData().equals("Tierra"))
        	earthRotX.addChild(vp);
        try {
          	
            jt.setPage ("http://localhost:8080/"+p.getUserData()+".html");
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
        if(nombre.equals("Sol"))	
        	solRotX.addChild(vp);       
	   if(nombre.equals("Tierra"))
        	earthRotX.addChild(vp);
        try {
          	jt.setPage ("http://localhost:8080/"+nombre+".html");
        } catch (IOException ex) {
                System.out.println("URL no valido");
        }
    }
}
}                                                                         
