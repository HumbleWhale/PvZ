package a10;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Riley Bunderson, u1203430
 * @author Cameron Skidmore, u1087079
 */

public class RodentsvsCats extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Timer timer;
	private static ArrayList<Actor> actors; // Plants and zombies all go in here
	BufferedImage plantImage; // Maybe these images should be in those classes, but easy to change here.
	BufferedImage plant2Image;
	BufferedImage plant3Image;
	BufferedImage zombieImage;
	BufferedImage zombie2Image;
	BufferedImage zombie3Image;
	BufferedImage nutImage;
	BufferedImage projectileImage;
	static JButton greySquirrelButton;
	static JButton chipmunkButton;
	static JButton bazookaHamsterButton;
	private Random rand;
	int numRows;
	int numCols;
	int cellSize;

	/**
	 * Setup the basic info for the example
	 */
	public RodentsvsCats() {
		super();

		// Define some quantities of the scene
		numRows = 4;
		numCols = 9;
		cellSize = 75;
		setPreferredSize(new Dimension(50 + numCols * cellSize, 50 + numRows * cellSize));
		rand = new Random();

		// Store all the plants and zombies in here.
		actors = new ArrayList<>();

		// Load images
		try {
			plantImage = ImageIO.read(new File("src/a10/Animal-Icons/greysquirrel.png"));
			plant2Image = ImageIO.read(new File("src/a10/Animal-Icons/chipmunk.png"));
			plant3Image = ImageIO.read(new File("src/a10/Animal-Icons/bazookahamster.png"));
			zombieImage = ImageIO.read(new File("src/a10/Animal-Icons/tigerkitty.png"));
			zombie2Image = ImageIO.read(new File("src/a10/Animal-Icons/ninjakitty.png"));
			zombie3Image = ImageIO.read(new File("src/a10/Animal-Icons/tankkitty.png"));
			nutImage = ImageIO.read(new File("src/a10/Animal-Icons/nut.png"));
			projectileImage = ImageIO.read(new File("src/a10/Animal-Icons/bullet.png"));
		} catch (IOException e) {
			System.out.println("A file was not found");
			System.exit(0);
		}

		// The timer updates the game each time it goes.
		// Get the javax.swing Timer, not from util.
		timer = new Timer(30, this);
		timer.start();

	}
	
	public static void addActor(Actor a) {
		actors.add(a);
	}

	/***
	 * Implement the paint method to draw the plants
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Actor actor : actors) {
			actor.draw(g, 0);
			actor.drawHealthBar(g);
		}
	}

	/**
	 * 
	 * This is triggered by the timer. It is the game loop of this test.
	 * 
	 * @param e
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		int row = rand.nextInt(numRows);
		int y = 50 + (row * cellSize);

		int column = rand.nextInt(numCols);
		int x = 50 + (column * cellSize);
		
		if (e.getSource() == greySquirrelButton) {
			Plant newPlant = new Plant(new Point2D.Double(x, y),
					new Point2D.Double(plantImage.getWidth(), plantImage.getHeight()), plantImage, 100, 5, 1);
			actors.add(newPlant);
		}
		if (e.getSource() == chipmunkButton) {
			Plant newPlant = new Plant(new Point2D.Double(x, y),
					new Point2D.Double(plant2Image.getWidth(), plant2Image.getHeight()), plant2Image, 50, 5, 2);
			actors.add(newPlant);
		}
		// Special bazookaHamster class. Needs to spawn projectile upon attack.  Currently broken
		if (e.getSource() == bazookaHamsterButton) {
			BazookaHamster newBazookaHamster = new BazookaHamster(new Point2D.Double(x, y),
					new Point2D.Double(plant3Image.getWidth(), plant3Image.getHeight()), plant3Image, 100, 5, 0);
			actors.add(newBazookaHamster);
		}
		// Generates new zombies randomly. Decrease the size of rand to make zombies
		// appear more frequently.
		if (e.getSource() == timer) {

			if (rand.nextInt(400) == 1) {
				int kittySpawn = rand.nextInt(2);

				if (kittySpawn == 0) {
					Zombie newZombie = new Zombie(new Point2D.Double(725, y),
							new Point2D.Double(plantImage.getWidth(), plantImage.getHeight()), zombieImage, 100, 50, -2,
							10);
					actors.add(newZombie);
				} else if (kittySpawn == 1) {
					Zombie newZombie = new Zombie(new Point2D.Double(725, y),
							new Point2D.Double(plantImage.getWidth(), plantImage.getHeight()), zombie2Image, 60, 10, -4,
							2);
					actors.add(newZombie);
				} else {
					Zombie newZombie = new Zombie(new Point2D.Double(725, y),
							new Point2D.Double(plantImage.getWidth(), plantImage.getHeight()), zombie3Image, 200, 40,
							-1, 12);
					actors.add(newZombie);
				}
			}
			/**
			// Generates new plants randomly. May generate on top of eachother.
			if (rand.nextInt(200) == 1) {
				// Chooses which kind of plant to spawn. They both have slightly different
				// stats.
				if (rand.nextInt(2) == 0) {
					Plant newPlant = new Plant(new Point2D.Double(x, y),
							new Point2D.Double(plantImage.getWidth(), plantImage.getHeight()), plantImage, 100, 5, 1);
					actors.add(newPlant);
				} else {
					Plant newPlant = new Plant(new Point2D.Double(x, y),
							new Point2D.Double(plant2Image.getWidth(), plant2Image.getHeight()), plant2Image, 50, 5, 2);
					actors.add(newPlant);
				}
			}*/
		

		// This method is getting a little long, but it is mostly loop code
		// Increment their cooldowns and reset collision status

		for (Actor actor : actors) {
			actor.update();
		}

		// Try to attack
		for (Actor zombie : actors) {
			for (Actor other : actors) {
				zombie.attack(other);
			}
		}

		// Remove plants and zombies with low health
		ArrayList<Actor> nextTurnActors = new ArrayList<>();
		for (Actor actor : actors) {
			if (actor.isAlive())
				nextTurnActors.add(actor);
			else
				actor.removeAction(actors); // any special effect or whatever on removal
		}
		actors = nextTurnActors;

		// Check for collisions between zombies and plants and set collision status
		for (Actor zombie : actors) {
			for (Actor other : actors) {
				zombie.setCollisionStatus(other);
			}
		}

		// Move the actors.
		for (Actor actor : actors) {
			actor.move(); // for Zombie, only moves if not colliding.
		}

		// Redraw the new scene
		repaint();
		}
	}

	/**
	 * Make the game and run it
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame app = new JFrame("Plant and Zombie Test");
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				RodentsvsCats panel = new RodentsvsCats();
				greySquirrelButton = new JButton("Grey Squirrel");
				greySquirrelButton.addActionListener(panel);
				panel.add(greySquirrelButton);
				chipmunkButton = new JButton("Chipmunk");
				chipmunkButton.addActionListener(panel);
				panel.add(chipmunkButton);
				bazookaHamsterButton = new JButton("Bazooka Hamster");
				bazookaHamsterButton.addActionListener(panel);
				panel.add(bazookaHamsterButton);
				app.setContentPane(panel);
				app.pack();
				app.setVisible(true);
			}
		});
	}

}