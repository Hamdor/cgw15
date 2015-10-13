/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.applications;

import computergraphics.framework.AbstractCGFrame;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.CuboidNode;
import computergraphics.scenegraph.GroupNode;
import computergraphics.scenegraph.Node;
import computergraphics.scenegraph.RotationNode;
import computergraphics.scenegraph.ScaleNode;
import computergraphics.scenegraph.ShaderNode;
import computergraphics.scenegraph.ShaderNode.ShaderType;
import computergraphics.scenegraph.SphereNode;
import computergraphics.scenegraph.TranslationNode;

/**
 * Application for the first exercise.
 * 
 * @author Philipp Jenke
 */
public class CGFrame extends AbstractCGFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4257130065274995543L;
	private RotationNode helicopterRotator;
	private TranslationNode helicopterTranslater;
	private RotationNode rotorRotator;
	private TranslationNode rotorTranslator;

	/**
	 * Constructor.
	 */
	public CGFrame(int timerInverval) {
		super(timerInverval);

		// Shader node does the lighting computation
		ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
		getRoot().addChild(shaderNode);

		/*
		 * // TODO TASK 1b) Scale node ScaleNode scaleNode = new ScaleNode(new
		 * Vector3(2.0, 2.0, 2.0)); shaderNode.addChild(scaleNode);
		 * 
		 * //TODO TASK 2a) Translation node TranslationNode translationNode =
		 * new TranslationNode(new Vector3(1.0, 0.0, 0.0));
		 * scaleNode.addChild(translationNode);
		 * 
		 * //TODO TASK 2b) Rotation node RotationNode rotationNode = new
		 * RotationNode(120, new Vector3(0.0, 1.0, 0.0));
		 * translationNode.addChild(rotationNode);
		 * 
		 * // Simple triangle SingleTriangleNode triangleNode = new
		 * SingleTriangleNode(); rotationNode.addChild(triangleNode);
		 * 
		 * // Sphere SphereNode sphereNode = new SphereNode(0.25, 20);
		 * shaderNode.addChild(sphereNode);
		 */

		// TODO TASK 3a) Complex Object
		Node helicopterAnimation = addHelicopterAnimation(shaderNode);
		TranslationNode translateComplexObject = new TranslationNode(new Vector3(0, 0, 0));
		helicopterAnimation.addChild(translateComplexObject);
		ScaleNode scaleComplexObject = new ScaleNode(new Vector3(0.25, 0.25, 0.25));
		translateComplexObject.addChild(scaleComplexObject);
		addComplexObject(scaleComplexObject);

		// TODO TASK 3b) Landscape
		TranslationNode translateLandscape = new TranslationNode(new Vector3(0, -1, 0));
		shaderNode.addChild(translateLandscape);
		ScaleNode scaleLandscape = new ScaleNode(new Vector3(1, 1, 1));
		translateLandscape.addChild(scaleLandscape);
		
		addLandscape(scaleLandscape);

	}

	private Node addHelicopterAnimation(Node parent) {
		
		helicopterRotator = new RotationNode(0, new Vector3(0, 1, 0));
		parent.addChild(helicopterRotator);
		
		helicopterTranslater = new TranslationNode(new Vector3(1, 0, 0));
		helicopterRotator.addChild(helicopterTranslater);
		
		
		
		
		
		return helicopterTranslater;
	}

	private void addLandscape(Node scaleLandscape) {
		GroupNode landscape = new GroupNode();
		
		scaleLandscape.addChild(landscape);
		landscape.addChild(new CuboidNode(5, 0.001, 5));
		
		ScaleNode scaleTrees = new ScaleNode(new Vector3(0.5, 0.5, 0.5));
		landscape.addChild(scaleTrees);

		for (int i = 0; i < 15; i++) {
			//generate trees
			double x, y, z;
			x = ((Math.random() * (7)) - 3.5);
			y = 0.5;
			z = ((Math.random() * (7)) - 3.5);
			addTree(scaleTrees, new Vector3(x, y, z));
		}
	}

	private void addTree(Node bottom, Vector3 position) {
		TranslationNode translate = new TranslationNode(position);
		bottom.addChild(translate);
		
		translate.addChild(new SphereNode(0.3, 20));
		TranslationNode trunk = new TranslationNode(new Vector3(0, -0.3, 0));
		translate.addChild(trunk);
		trunk.addChild(new CuboidNode(0.15, 0.5, 0.15));
		
	}

	private void addComplexObject(Node add) {
		
		GroupNode complexObject = new GroupNode();
		add.addChild(complexObject);
		
		complexObject.addChild(new SphereNode(0.5, 25));
		
		TranslationNode base = new TranslationNode(new Vector3(0.0, -0.45, 0.0));
		complexObject.addChild(base);
		TranslationNode rightBase = new TranslationNode(new Vector3(0.38, 0.0, 0.0));
		base.addChild(rightBase);
		rightBase.addChild(new CuboidNode(0.15, 0.15, 2.0));
		TranslationNode leftBase = new TranslationNode(new Vector3(-0.38, 0.0, 0.0));
		base.addChild(leftBase);
		leftBase.addChild(new CuboidNode(0.15, 0.15, 2.0));
		
		TranslationNode rotors = new TranslationNode(new Vector3(0.0, 0.5, 0.0));
		Node rotorAnimation = addRotorAnimation(complexObject);
		rotorAnimation.addChild(rotors);
		RotationNode firstRotor = new RotationNode(45, new Vector3(0.0, 1.0, 0.0));
		rotors.addChild(firstRotor);
		RotationNode secondRotor = new RotationNode(-45, new Vector3(0.0, 1.0, 0.0));
		rotors.addChild(secondRotor);
		firstRotor.addChild(new CuboidNode(0.05, 0.05, 3.0));
		secondRotor.addChild(new CuboidNode(0.05, 0.05, 3.0));
	}

	private Node addRotorAnimation(Node parent) {
		
		rotorRotator = new RotationNode(0, new Vector3(0,1,0));
		parent.addChild(rotorRotator);
		rotorTranslator = new TranslationNode(new Vector3(0, 0, 0));
		rotorRotator.addChild(rotorTranslator);
		return rotorTranslator;
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see computergrafik.framework.ComputergrafikFrame#timerTick()
	 */
	@Override
	protected void timerTick() {
		System.out.println("Tick");
		
		//move helicopter
		helicopterRotator.setAngle(helicopterRotator.getAngle()+2);
		
		//move rotor
		rotorRotator.setAngle(rotorRotator.getAngle() - 20);
		
	}

	public void keyPressed(int keyCode) {
		System.out.println("Key pressed: " + (char) keyCode);
	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {
		// The timer ticks every 1000 ms.
		new CGFrame(100);
	}
}
