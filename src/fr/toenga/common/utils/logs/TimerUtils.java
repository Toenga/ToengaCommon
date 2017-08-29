package fr.toenga.common.utils.logs;

import java.util.Timer;

import lombok.Getter;
import lombok.Setter;

public class TimerUtils 
{

	@Getter @Setter private static Timer timer = new Timer();

}
