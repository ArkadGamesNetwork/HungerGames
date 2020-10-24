package fr.mrcubee.survivalgames;

import fr.mrcubee.survivalgames.listeners.RegisterListeners;
import fr.mrcubee.survivalgames.step.StepManager;
import fr.mrcubee.survivalgames.step.steps.FeastStep;
import fr.mrcubee.survivalgames.step.steps.GameStep;
import fr.mrcubee.survivalgames.step.steps.PvpStep;
import fr.mrcubee.util.FileUtil;
import fr.mrcubee.world.WorldSpawnSetup;
import net.arkadgames.survivalgame.sql.DataBase;

import java.io.File;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SurvivalGames extends JavaPlugin {
	
	private DataBase dataBase;
	private Game game;
	private Timer timer;

	public void onLoad() {
		saveDefaultConfig();
		this.game = new Game(this);
		File worldFile = new File("./" + this.game.getGameSetting().getWorldName());
		File netherFile = new File("./" + this.game.getGameSetting().getWorldName() + "_nether");
		File endFile = new File("./" + this.game.getGameSetting().getWorldName() + "_the_end");
		FileUtil.delete(worldFile);
		FileUtil.delete(netherFile);
		FileUtil.delete(endFile);
	}

	public void onEnable() {
		StepManager stepManager;
		World world;

		this.game.init();

		// **STEPS** //
		stepManager = this.game.getStepManager();
		stepManager.registerStep(PvpStep.create(this.game));
		stepManager.registerStep(GameStep.create(this.game));
		stepManager.registerStep(FeastStep.create(this.game));
		// **END STEPS** //

		this.timer = new Timer(this);

		// **WORLD SETUP** //
		world = this.game.getGameWorld();
		if (world == null || !WorldSpawnSetup.setup(world, this.game.getGameSetting().getLoadSize(), this.getLogger())) {
			getServer().shutdown();
			return;
		}
		this.game.getSpawnTerrainForming().runTaskTimer(this, 0L, 10L);
		// **END WORLD SETUP**//

		RegisterListeners.register(this);
		/*
		try {
			dataBase = new DataBase(this);
		} catch (SQLException e) {
			this.getLogger().severe("Error to connect to DataBase !");
			e.printStackTrace();
			this.getServer().shutdown();
			return;
		}boolean
		*/
		getGame().setGameStats(GameStats.WAITING);
		this.timer.runTaskTimer(this, 0L, 20L);
	}

	public void onDisable() {
		File logsFile = new File("./logs");
		FileUtil.delete(logsFile);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.isOp())
			return false;
		if (getGame().getGameStats() == GameStats.WAITING)
			getGame().forceStart();
		if (getGame().getGameStats() == GameStats.DURING)
			getGame().forcePvp();
		return true;
	}
	
	public DataBase getDataBase() {
		return dataBase;
	}

	public Game getGame() {
		return this.game;
	}
}
