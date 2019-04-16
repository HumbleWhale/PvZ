package a10;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class BazookaHamster extends Actor {
	
	private Point2D.Double startingPosition;
	BufferedImage projectileImage;

	
	public BazookaHamster(Point2D.Double startingPosition, Point2D.Double initHitbox, BufferedImage img, int health, int coolDown, int attackDamage) {
		super(startingPosition, initHitbox, img, health, coolDown, 0, attackDamage);
		
		this.startingPosition = startingPosition;
		
		try{
			projectileImage = ImageIO.read(new File("src/a10/Animal-Icons/bullet.png"));
		}
		catch (IOException e) {
			System.out.println("A file was not found");
			System.exit(0);
		}
	}

	/**
	 * An attack means the two hotboxes are overlapping and the
	 * Actor is ready to attack again (based on its cooldown).
	 * 
	 * Plants only attack Zombies.
	 * 
	 * @param other
	 */
	
	@Override
	public void attack(Actor other) {
		if (other instanceof Zombie) {
			Projectile newProjectile = new Projectile(startingPosition,
					new Point2D.Double(projectileImage.getWidth(), projectileImage.getHeight()), projectileImage, 1, 5, 15, 50);
			RodentsvsCats.addActor(newProjectile);
		}
	}
}
