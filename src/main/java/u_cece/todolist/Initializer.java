package u_cece.todolist;

import net.fabricmc.api.ModInitializer;

public class Initializer implements ModInitializer {

	@Override
	public void onInitialize() {
		ToDoList.INSTANCE.init();
	}
}